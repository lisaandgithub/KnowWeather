package com.example.administrator.knowweather.util;

import android.text.TextUtils;

import com.example.administrator.knowweather.db.City;
import com.example.administrator.knowweather.db.County;
import com.example.administrator.knowweather.db.Province;
import com.example.administrator.knowweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/11 0011.
 * 由于服务器返回的省市县的数据都是Json格式的，所以此类用于解析和处理简单的json数据
 *
 */
public class Utility {

    /**
     * 解析和处理返回的省级数据
     * 省级数据是JSON数组
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                //首先定义一个JsonArray对象来存储省级数据
                JSONArray allProvince = new JSONArray(response);
                //使用循环从JsonArray中提取出JsonObject对象
                for (int i=0;i<allProvince.length();i++){
                    //将JsonObject对象封装成实体类对象
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    //最后将实体类对象保存到数据库中
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 解析和处理返回的市级数据
     * 省级数据是JSON数组
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)) {
            try {
                //首先定义一个JsonArray对象来存储市级数据
                JSONArray allCities = new JSONArray(response);
                //使用循环从JsonArray中提取出JsonObject对象
                for (int i=0;i<allCities.length();i++){
                    //将JsonObject对象封装成实体类对象
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    //最后将实体类对象保存到数据库中
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 解析和处理返回的县级数据
     * 县级数据是JSON数组
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)) {
            try {
                //首先定义一个JsonArray对象来存储县级数据
                JSONArray allCounties = new JSONArray(response);
                //使用循环从JsonArray中提取出JsonObject对象
                for (int i=0;i<allCounties.length();i++){
                    //将JsonObject对象封装成实体类对象
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    //最后将实体类对象保存到数据库中
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

