package org.mini.http;

import com.google.gson.Gson;

import org.mini.frame.log.MiniLogger;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {

    private final static String TAG  = HttpClient.class.getSimpleName();
    private Map<String, String> headers = null;

    public interface HttpCallback<T>
    {
        void onResponse(Exception e, HttpObject<T> object);
    }

    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }

    public <T> void get(String url, Map<String, String> params, final Class<T> tClass, final HttpClient.HttpCallback<T> callback) {
        this.request("GET", url, params, tClass, callback);
    }

    public <T> void get(String url, final Class<T> tClass, final HttpClient.HttpCallback<T> callback) {
        this.request("GET", url, null, tClass, callback);
    }

    public <T> void post(String url, final Class<T> tClass, final HttpClient.HttpCallback<T> callback) {
        this.request("POST", url, null, tClass, callback);
    }

    public <T> void post(String url, Map<String, String> params, final Class<T> tClass, final HttpClient.HttpCallback<T> callback) {
        this.request("POST", url, params, tClass, callback);
    }

    public <T> void upload(String url, Map<String, String> params, String filePath, final Class<T> tClass, final HttpClient.HttpCallback<T> httpCallback) throws Exception {

        File file = new File(filePath);
        if (file.exists()) {
            MiniLogger.get(TAG).d("upload file : %s", filePath);
        }
        else {
            MiniLogger.get(TAG).e("upload file not found");
        }

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody);
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (value != null) {
                    bodyBuilder.addFormDataPart(key, params.get(key));
                }
            }
        }
        MultipartBody requestBody = bodyBuilder.build();
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);
        request(builder, tClass, httpCallback);
    }

    public <T> void request(String method, String url, Map<String, String> params, final Class<T> tClass, final HttpClient.HttpCallback<T> callback) {
        MiniLogger.get().d("%s, request url : %s", TAG, url);

        Request.Builder requestBuilder;
        if ("POST".equals(method)) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    if (value != null) {
                        bodyBuilder.add(key, params.get(key));
                    }
                }
            }
            requestBuilder = new Request.Builder().post(bodyBuilder.build()).url(url);
        }
        else {
            if (params != null && params.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    try {
                        stringBuilder.append(key).append("=").append(URLEncoder.encode(value,"utf-8")).append("&");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (url.indexOf("?") != -1) {
                    url = url + "&" + stringBuilder.toString();
                }
                else {
                    url = url + "?" + stringBuilder.toString();
                }
            }
            requestBuilder = new Request.Builder().get().url(url);
        }
        request(requestBuilder, tClass, callback);
    }

    public class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;
        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }
        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }
        @Override
        public Type getRawType() {
            return raw;
        }
        @Override
        public Type getOwnerType() {return null;}
    }

    private <T> void request(Request.Builder requestBuilder, final Class<T> tClass, final HttpClient.HttpCallback<T> httpCallback) {
        if (this.headers != null && this.headers.size() > 0) {
            for(String key : this.headers.keySet()) {
                String value = this.headers.get(key);
                if (value != null) {
                    requestBuilder.addHeader(key, value);
                }
            }
        }
        Request request = requestBuilder.build();
        okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient client  = httpBuilder
                //设置超时
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MiniLogger.get().e(e);
                httpCallback.onResponse(e, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                MiniLogger.get(TAG).d("%s, response body: %s", call.request().url().toString(), res);
                //Type jsonType = new TypeToken<ServerObject<T>>() {}.getType();
                Type type = new ParameterizedTypeImpl(HttpObject.class, new Type[]{tClass});
                HttpObject<T> obj = new Gson().fromJson(res, type);
                if (obj.code != 0) {
                    Exception exception = new Exception(obj.desc);
                    httpCallback.onResponse(exception, obj);
                }
                else {
                    httpCallback.onResponse(null, obj);
                }
            }
        });
    }

}
