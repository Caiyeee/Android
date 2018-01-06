package com.example.yuying.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private LocationClient mLocationClient = null;
    private LocationClientOption mOption;
    private boolean isPermitted = false;
    private final int BAIDU_READ_PHONE_STATE = 1;
    private String mLocation;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationClient = new LocationClient(this);
        mOption = new LocationClientOption();

        intent = new Intent(MainActivity.this, EditActivity.class);

        if (Build.VERSION.SDK_INT >= 23){
            showContacts();
        }
        else{
            isPermitted = true;
        }

        if(isPermitted) {
            getMyLocation();

            FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    String mTime = getTime();
                    intent.putExtra("time", mTime);
                    getMyLocation();
                    startActivityForResult(intent, 0);
                }
            });
        }
    }

    //获取日期和时间
    String getTime() {
        Calendar c = Calendar.getInstance();

        //取得系统日期
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        //取得系统时间：
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String h = String.valueOf(hour) ;
        String m = String.valueOf(minute);

        if(hour < 10){
            h = "0" + String.valueOf(minute);
        }
        if(minute < 10){
            m = "0" + String.valueOf(minute);
        }

        String date = String.valueOf(year) + "年" + String.valueOf(month) + "月" + String.valueOf(day) + "日";
        String nowtime = h + ":" + m ;

        String result = date + " " + nowtime;
        return result;
    }

    //获取位置
    void getMyLocation() {
        mOption.setIsNeedAddress(true);
        mOption.setOpenGps(true);
        mOption.setCoorType("bd09ll");
        mOption.setScanSpan(1000);
        mLocationClient.setLocOption(mOption);
        mLocationClient.start();
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if(bdLocation == null)
                    return;

                String addr = bdLocation.getAddrStr();    //获取详细地址信息
                String country = bdLocation.getCountry();    //获取国家
                String province = bdLocation.getProvince();    //获取省份
                String city = bdLocation.getCity();    //获取城市
                String district = bdLocation.getDistrict();    //获取区县
                String street = bdLocation.getStreet();    //获取街道信息

                if(country == null){
                    mLocation = "";
                }
                else{
                    mLocation = country + "·" + province + city + district;
                }
                intent.putExtra("location", mLocation);
            }
        });
        mLocationClient.start();
    }

    //动态权限申请
    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理
                    isPermitted = true;
                }
                else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        if(mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
            mLocationClient=null;
        }
        super.onDestroy();
    }
}
