package com.example.yuying.finalproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Yuying on 2018/1/6.
 */

public class EditActivity extends AppCompatActivity {
    private TextView mTimeTv;
    private TextView mLocationTv;
    private ImageView mWeatherIv;
    private String searchCity;
    private String mTime;
    private int hour;
    private String mLocation;
    private String city;
    private LocationClient mLocationClient = null;
    private LocationClientOption mOption;
    private static final String url = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";
    private static final int UPDATE_CONTENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mTimeTv = (TextView) findViewById(R.id.timetv);
        mLocationTv = (TextView) findViewById(R.id.locationtv);
        mWeatherIv = (ImageView) findViewById(R.id.weatheriv);

        mLocationClient = new LocationClient(this);
        mOption = new LocationClientOption();

        //时间
        getTime();
        mTimeTv.setText(mTime);

        //地点，由地点搜天气
        getMyLocation();
    }

    //获取日期和时间
    void getTime() {
        Calendar c = Calendar.getInstance();

        //取得系统日期
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        //取得系统时间：
        hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String h = String.valueOf(hour) ;
        String m = String.valueOf(minute);

        if(hour < 10){
            h = "0" + String.valueOf(hour);
        }
        if(minute < 10){
            m = "0" + String.valueOf(minute);
        }

        String date = String.valueOf(year) + "年" + String.valueOf(month) + "月" + String.valueOf(day) + "日";
        String nowtime = h + ":" + m ;

        mTime = date + " " + nowtime;

        /* 数据库存储时间 */

    }

    //获取位置
    void getMyLocation() {
        mOption.setIsNeedAddress(true);
        mOption.setOpenGps(true);
        mOption.setCoorType("bd09ll");
        mOption.setScanSpan(0);
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
                city = bdLocation.getCity();    //获取城市
                String district = bdLocation.getDistrict();    //获取区县
                String street = bdLocation.getStreet();    //获取街道信息

                if(country == null){
                    mLocation = "";
                }
                else{
                    if(country.equals("中国"))
                        mLocation =  province.substring(0, province.length() - 1) + "·" + city + district;
                    else{
                        mLocation = country + "·" + province;
                    }
                }
                mLocationTv.setText(mLocation);

                /* 数据库存储定位 */

                //由定位查询天气，由于每天只有50次的查询机会，因此暂时注释，勿删！！！
             /*   searchCity = city.substring(0, city.length() - 1);
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                sendRequestWithHttpURLConnection();*/
            }
        });
        mLocationClient.start();
    }

    //  子线程中不能直接修改 UI 界面，需要 handler 进行UI 界面的修改
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case UPDATE_CONTENT:
                    List<String> data = (List<String>) message.obj;
                    String first_day = (data).get(7);
                    String[] tag_first = first_day.split("[ ]");
                    String weatherDescribe = tag_first[0];

                    /*weatherDescribe 数据库存储天气情况*/

                    //获取天气图标
                    for(int i = 0; i < weatherDescribe.length(); i++) {
                        if(weatherDescribe.charAt(i) == '晴' && hour >= 6 && hour <= 18){
                            mWeatherIv.setImageResource(R.mipmap.sun);
                        }
                        else if(weatherDescribe.charAt(i) == '晴' && (hour < 6 || hour > 18)){
                            mWeatherIv.setImageResource(R.mipmap.moon);
                        }
                        else if(weatherDescribe.charAt(i) == '云' && hour >= 6 && hour <= 18){
                            mWeatherIv.setImageResource(R.mipmap.cloudday);
                        }
                        else if(weatherDescribe.charAt(i) == '云' && (hour < 6 || hour > 18)){
                            mWeatherIv.setImageResource(R.mipmap.cloudnight);
                        }
                        else if(weatherDescribe.charAt(i) == '雨' && hour >= 6 && hour <= 18){
                            mWeatherIv.setImageResource(R.mipmap.rainday);
                        }
                        else{
                            mWeatherIv.setImageResource(R.mipmap.rainnight);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //  http 请求需要开启子线程，然后由子线程执行请求，所以我们之前所写代码都是在子线程中完成的，并且使用 XmlPullParser 进行解析从而得到我们想要的数
    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //  使用 HttpURLConnection 新建一个 http 连接，新建一个 URL 对象，打开连接即可，并且设置访问方法以及时间设置
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) ((new URL(url).openConnection()));
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setRequestMethod("POST");

                    //  将我们需要请求的字段以流的形式写入 connection 之中，这一步相当于将需要的参数提交到网络连接，并且请求网络数据（类似于 html 中的表单操作，将 post 数据提交到服务器）
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes("theCityCode=" + URLEncoder.encode(searchCity, "utf-8") + "&theUserID="+"");
                    //  注意中文乱码解决

                    //  网页获取 xml 转化为字符串
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    //  Message消息传递
                    Message message = new Message();
                    message.what = UPDATE_CONTENT;
                    message.obj = parseXMLWithPull(response.toString());
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //  关闭 connection
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public ArrayList<String> parseXMLWithPull(String xml) throws XmlPullParserException, IOException {
        //  首先获取 XmlPullParser 对象实例，然后设置需要解析的字符串，最后按照 tag 逐个获取所需要的 string
        //  获取实例
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();

        //  设置所需要解析的string
        parser.setInput(new StringReader(xml));

        int eventType = parser.getEventType();
        ArrayList<String> list = new ArrayList<>();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("string".equals(parser.getName())) {
                        String str = parser.nextText();
                        list.add(str);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        return list;
    }
}