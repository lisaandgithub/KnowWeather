package com.example.administrator.knowweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.administrator.knowweather.gson.Weather;
import com.example.administrator.knowweather.util.HttpUtil;
import com.example.administrator.knowweather.util.Utility;
import com.example.administrator.knowweather.util.parseXMLWithPull;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //更新天气
        updateWeather();
        //更新必应每日一图
        updateBingPic();
        //创建定时任务
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int eightHourMilliseconds = 8 * 60 * 60 * 1000;//8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + eightHourMilliseconds;
        //8小时后。AutoUpdateService的onStartCommand方法会重新执行，实现后台定时更新的功能
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        //因为针对同一个PendingIntent，AlarmManager.set 不能够设置多个闹钟，所以先取消旧闹钟，再设置新闹钟
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    private void updateWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather",null);
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid="+
                    weatherId + "&key=1dbf8818598d4ee19fa0ee2aacbbf95d";
            //请求天气信息
            HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weatherFromResponse = Utility.handleWeatherResponse(responseText);
                    if (weatherFromResponse != null && "ok".equals(weatherFromResponse.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    /**
     * 更新必应每日一图
     */
    //访问这个网址获取到bing 背景图片的xml文件
    public static final String BingPicXmlUrl = "http://cn.bing.com/HPImageArchive.aspx?idx=0&n=1";
    public static final String BingUrl = "http://cn.bing.com";

    private void updateBingPic(){
        HttpUtil.sendOKHttpRequest(BingPicXmlUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();//得到图片的xml文件
                try {
                    String bingPic = BingUrl + parseXMLWithPull.parseBingPicForUrl(responseData);
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("bing_pic",bingPic);
                    editor.apply();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
