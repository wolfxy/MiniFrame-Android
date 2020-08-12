package org.mini.frame.tools;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YXL on 2015/12/9.
 */
public class MiniToolKit {
    public static void print(Object... args){
        if (args != null && args.length > 0){
            String str = "";
            for (Object obj :
                    args) {
                str += obj + ", ";
            }
            if(str.length() > 0){
                str = str.substring(0,str.length() - 1);
                Log.v("print", str);
            }
        }
    }

    public static void printStackTrace(){
        new Exception().printStackTrace();
    }

    public  static int parseInt(Object obj){
        try {
            return Integer.parseInt(obj.toString());
        }catch (Exception ex){

        }
        return 0;
    }

    public  static long parseLong(Object obj){
        try {
            return Long.parseLong(obj.toString());
        }catch (Exception ex){

        }
        return 0;
    }

    public  static float parseFloat(Object obj){
        try {
            return Float.parseFloat(obj.toString());
        }catch (Exception ex){

        }
        return 0;
    }

    /**
     * 电话号码验证
     *
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null,p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if(str.length() >9)
        {   m = p1.matcher(str);
            b = m.matches();
        }else{
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * 手机号验证
     *
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * MD5数据
     * *
     */
    public static String MD5(String string, int len) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder builder = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0) i += 256;
                if (i < 16)
                    builder.append("0");
                builder.append(Integer.toHexString(i));
            }
            if (len == 32) {
                return builder.toString();
            } else if (len == 16) {
                return builder.toString().substring(8, 24);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatDecimal(double d,int len){
    	String format = "0.";
    	for (int i = 0; i < len; i++) {
			format += "0";
		}
    	DecimalFormat df = new DecimalFormat(format);
    	String db = df.format(d);
    	return db;
    }

    public static int getDaysSince1970(Long timeMillis){
    	long t = (timeMillis + 8 * 60 * 60 * 1000) / 1000 / 60 / 60 / 24;
    	return (int)t;
    }

    public static String formatDateTimeString(Date date,String format){
        return new SimpleDateFormat(format).format(date);
    }
    
    public static Date formatDateTime(String date,String format){
        try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new Date();
    }
    
    public static String getTimeDistance(Calendar main,Calendar calendar){
    	String result = "";
    	int y1 = main.get(Calendar.YEAR);
    	int y2 = calendar.get(Calendar.YEAR);
    	int m1 = main.get(Calendar.MONTH);
    	int m2 = calendar.get(Calendar.MONTH);
    	int d1 = main.get(Calendar.DAY_OF_YEAR);
    	int d2 = calendar.get(Calendar.DAY_OF_YEAR);
    	int h1 = main.get(Calendar.HOUR_OF_DAY);
    	int h2 = calendar.get(Calendar.HOUR_OF_DAY);
    	int mm1 = main.get(Calendar.MINUTE);
    	int mm2 = calendar.get(Calendar.MINUTE);
    	int s1 = main.get(Calendar.SECOND);
    	int s2 = calendar.get(Calendar.SECOND);
    	
    	if(y1 != y2){
    		result = Math.abs(y1 - y2) + "年";
    	}else if(m1 != m2){
    		result = Math.abs(m1 - m2) + "月";
    	}else if(d1 != d2){
    		result = Math.abs(d1 - d2) + "天";
    	}else if(h1 != h2){
    		result = Math.abs(h1 - h2) + "小时";
    	}else if(mm1 != mm2){
    		result = Math.abs(mm1 - mm2) + "分钟";
    	}else if(s1 != s2){
    		result = Math.abs(s1 - s2) + "秒钟";
    	}else{
    		result = "现在";
    		return result;
    	}
    	if(main.getTimeInMillis() > calendar.getTimeInMillis()){
    		result += "以前";
    	}else{
    		result += "以后";
    	}
    	return result;
    }

    public static boolean isStringNullOrEmpty(String str){
        return str == null || str.length() == 0;
    }
    
    public static boolean isArrayNullOrEmpty(List list){
        return list == null || list.size() == 0;
    }

    public static boolean isIntegerNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!(c >= '0' && c < '9')) {
                return false;
            }
        }
        return true;
    }

    public static int[] shuffle(int from, int to) {
        if (from > to) {
            int n = from;
            from = to;
            to = n;
        }
        int size = to-from+1;
        int[] list = new int[size];
        if (size == 1) {
            list[0] = from;
            return list;
        }
        int seed = from;
        int d = from -1;
        for (int i = 0; i < size; i++) {
            list[i] = d;
        }
        int count = 0;
        int mod = 2 * size;
        do {
            int random = (int) (Math.random() * mod) % size;
            if (list[random] == d) {
                list[random] = seed;
                count++;
                seed ++;
            }
        }
        while (count < size);
        return list;
    }

    public static List<String> shuffle(String string) {
        int length = string.length();
        List<String> list = new ArrayList<>(length);
        int[] r = shuffle(0, length-1);
        for (int i = 0; i < r.length; i++) {
            list.add(string.charAt(r[i])+"");
        }
        return list;
    }
}
