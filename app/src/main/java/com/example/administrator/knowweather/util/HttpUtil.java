package com.example.administrator.knowweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/2/11 0011.
 * 由于okhttp库优秀的功能封装，我们发送http请求非常简单，只需要调用sendHttpRequest()传入要访问的url，并且注册一个回调来处理服务器的响应
 */

public class HttpUtil {
    public static void sendOKHttpRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
