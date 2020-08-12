package org.mini.frame.toolkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Wuquancheng on 2018/1/3.
 */

public class MiniCURL {
    private static void addHeader(HttpURLConnection connection, Map<String,String> headers) {
        for (String key: headers.keySet()) {
            connection.setRequestProperty(key,headers.get(key));
        }
    }

    public static String get(String address, Map<String,String> headers) throws Exception {
        return get(address,null,headers);
    }

    public static String get(String address, Map<String,Object> params, Map<String,String> headers) throws Exception {
        return get(address,params,headers,20000,10000);
    }

    public static String get(String address, Map<String,Object> params, Map<String,String> headers, int readTimeout, int connectTimeout) throws Exception {
        InputStream in = null;
        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
        try {

            if (params != null && params.size()>0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String key: params.keySet()) {
                    stringBuilder.append(key).append("=").append(URLEncoder.encode(params.get(key).toString(),"utf-8")).append("&");
                }
                if (stringBuilder.length()>0) {
                    stringBuilder.setLength(stringBuilder.length()-1);
                    if (address.indexOf("?")!=-1) {
                        address = address + "&" + stringBuilder.toString();
                    }
                    else {
                        address = address + "?" + stringBuilder.toString();
                    }
                }
            }
            java.net.URL url = new java.net.URL(address);
            connection = (HttpURLConnection)url.openConnection();
            addHeader(connection,headers);
            connection.setReadTimeout(readTimeout);
            connection.setConnectTimeout(connectTimeout);
            int code = connection.getResponseCode();
            StringBuilder stringBuilder = new StringBuilder();
            in = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(in,"utf-8"));
            String line;
            while ((line=bufferedReader.readLine())!=null) {
                stringBuilder.append(line);
            }
            in.close();
            String response = stringBuilder.toString();
            if (code!=200) {
                throw new Exception("["+code+"] service inner error !");
            }
            return response;
        }
        catch (IOException e) {
            throw new Exception(e);
        }
        finally {
            try {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e) {

                    }
                    bufferedReader = null;
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {

                    }
                    in = null;
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            catch (Exception e) {

            }
        }

    }
}
