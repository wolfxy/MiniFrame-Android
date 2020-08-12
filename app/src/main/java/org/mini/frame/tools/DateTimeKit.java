package org.mini.frame.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by HUANFENG on 2015/12/2.
 * @author  hucheng
 * @content 日期工具类
 */
public class DateTimeKit {
    /**
     *  以下为月的状态
     */
    public final static int DAY_COUNT_January   = 1;
    public final static int DAY_COUNT_February  = 2;
    public final static int DAY_COUNT_March     = 3;
    public final static int DAY_COUNT_April     = 4;
    public final static int DAY_COUNT_May       = 5;
    public final static int DAY_COUNT_June      = 6;
    public final static int DAY_COUNT_July      = 7;
    public final static int DAY_COUNT_August    = 8;
    public final static int DAY_COUNT_September = 9;
    public final static int DAY_COUNT_October   = 10;
    public final static int DAY_COUNT_November  = 11;
    public final static int DAY_COUNT_December  = 12;
    /**
     *  日期格式
     */
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    //如果是12月就动态生成下一年
    public static String[] getYearStringByMonth(int year, int month){
        if(month==DAY_COUNT_December){
            return new String[] {String.valueOf(year-1), String.valueOf(year), String.valueOf(year+1)};
        }else{
            return new String[] {String.valueOf(year-1), String.valueOf(year)};
        }
    }
    //根据年和日返回滑轮需要的天的字符串
    public static String[] getDayStringByYearAndMonth(int year, int month){
        switch(month){
            case DAY_COUNT_January:
                return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
            case DAY_COUNT_February:
                if(year%4 == 0){
                    //闰年
                    return new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29"};
                }else{
                    return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};
                }
            case DAY_COUNT_March:
                return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
            case DAY_COUNT_April:
                return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
            case DAY_COUNT_May:
                return new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
            case DAY_COUNT_June:
                return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
            case DAY_COUNT_July:
                return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
            case DAY_COUNT_August:
                return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
            case DAY_COUNT_September:
                return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
            case DAY_COUNT_October:
                return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
            case DAY_COUNT_November:
                return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
            case DAY_COUNT_December:
                return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
            default:
                break;
        }
        return null;
    }


    public static Date str2Date(String str) {
        return str2Date(str, null);
    }

    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Calendar str2Calendar(String str) {
        return str2Calendar(str, null);

    }

    public static Calendar str2Calendar(String str, String format) {

        Date date = str2Date(str, format);
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;

    }

    public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
        return date2Str(c, null);
    }

    public static String date2Str(Calendar c, String format) {
        if (c == null) {
            return null;
        }
        return date2Str(c.getTime(), format);
    }

    public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
        return date2Str(d, null);
    }

    public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }

    public static String getCurDateStr() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String month = "";
        String day = "";
        String hour = "";
        String minute = "";
        String second = "";
        if((c.get(Calendar.MONTH)+1) < 10){
            month = "0"+ (c.get(Calendar.MONTH)+1);
        }else {
            month = String.valueOf(c.get(Calendar.MONTH)+1);
        }
        if(c.get(Calendar.DAY_OF_MONTH) < 10){
            day = "0"+ c.get(Calendar.DAY_OF_MONTH);
        }else {
            day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        }
        if(c.get(Calendar.HOUR_OF_DAY) < 10){
            hour = "0"+ c.get(Calendar.HOUR_OF_DAY);
        }else {
            hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        }

        if(c.get(Calendar.MINUTE) < 10){
            minute = "0"+ c.get(Calendar.MINUTE);
        }else {
            minute = String.valueOf(c.get(Calendar.MINUTE));
        }

        if(c.get(Calendar.SECOND) < 10){
            second = "0"+ c.get(Calendar.SECOND);
        }else {
            second = String.valueOf(c.get(Calendar.SECOND));
        }

        return c.get(Calendar.YEAR) + "-" + month + "-"
                + day + "-"
                + hour + ":" + minute
                + ":" + second;
    }

    public static String getCurOnlyDateStr() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
                + c.get(Calendar.DAY_OF_MONTH);
    }

    public static String getCurOnlyDateTime() {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String hour = "";
        String minute = "";

        if(c.get(Calendar.HOUR_OF_DAY) < 10){
            hour = "0"+ c.get(Calendar.HOUR_OF_DAY);
        }else {
            hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        }

        if(c.get(Calendar.MINUTE) < 10){
            minute = "0"+ c.get(Calendar.MINUTE);
        }else {
            minute = String.valueOf(c.get(Calendar.MINUTE));
        }

        return  hour + ":" + minute;
    }
    public static String getCurWeekend(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return "星期"+mWay;
    }

    /**
     * 获得当前日期的字符串格式
     *
     * @param format
     * @return
     */
    public static String getCurDateStr(String format) {
        Calendar c = Calendar.getInstance();
        return date2Str(c, format);
    }

    // 格式到秒
    public static String getMillon(long time) {

        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);

    }

    // 格式到天
    public static String getDay(long time) {

        return new SimpleDateFormat("yyyy-MM-dd").format(time);

    }

    // 格式到毫秒
    public static String getSMillon(long time) {

        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);

    }


    /*
     输入的是String，格式诸如20120102，实现加一天的功能，返回的格式为String，诸如20120103
     */
    public static String stringDatePlus(String row) throws ParseException {
        String year=row.substring(0, 4);
        String month=row.substring(4,6);
        String day=row.substring(6);
        String date1=year+"-"+month+"-"+day;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date startDate=sdf.parse(date1);
        Calendar cd = Calendar.getInstance();
        cd.setTime(startDate);
        cd.add(Calendar.DATE, 1);
        String dateStr =sdf.format(cd.getTime());
        String year1=dateStr.substring(0,4);
        String month1=dateStr.substring(5,7);
        String day1=dateStr.substring(8);
        return year1+month1+day1;
    }

    /*
     输入的是String，格式诸如20120102，实现减一天的功能，返回的格式为String，诸如20120101
     */
    public static String stringDateDecrease(String row) throws ParseException {
        String year=row.substring(0, 4);
        String month=row.substring(4,6);
        String day=row.substring(6);
        String date1=year+"-"+month+"-"+day;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date startDate=sdf.parse(date1);
        Calendar cd = Calendar.getInstance();
        cd.setTime(startDate);
        cd.add(Calendar.DATE, -1);
        String dateStr =sdf.format(cd.getTime());
        String year1=dateStr.substring(0,4);
        String month1=dateStr.substring(5,7);
        String day1=dateStr.substring(8);
        return year1+month1+day1;
    }

    /*
     输入的格式为String，诸如20120101，返回的格式为String，诸如2012-01-01
     */
    public static String stringDateChange(String date)
    {
        if(date.length()=="20120101".length()){
            String year=date.substring(0, 4);
            String month=date.substring(4,6);
            String day=date.substring(6);
            return year+"-"+month+"-"+day;
        }else{
            return date;
        }


    }
    /**
     * 日期向后推一天
     * @param date 格式：20120101
     * @return  20120102
     */
    public static String tonextday(String date){
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day+1);
        Date newdate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String da = format.format(newdate);
        return da;
    }

    /**
     * 获取当前日期上一周的开始日期 （周日）
     */
    public static String previousWeekByDate(String date) {
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        Date newdate = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(newdate);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int s = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-s);//根据日历的规则，给当前日期减往星期几与一个星期第一天的差值
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        String imptimeBegin = sdf.format(cal.getTime());
//	    System.out.println("所在周星期日的日期："+imptimeBegin);
        return imptimeBegin;
    }


    /**
     * 获取当前日期上一周的结束日期 （周六）
     */
    public static String previousWeekEndDayByDate(String date) {
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        Date newdate = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(newdate);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int s = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()+(6-s));
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        String imptimeBegin = sdf.format(cal.getTime());
//	    System.out.println("星期六的日期："+imptimeBegin);
        return imptimeBegin;
    }


    /**
     * 获取当前日期当前一周的开始日期 （周日）
     */
    public static String getCurrentWeekFirstDayByDate(String date) {
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        Date newdate = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(newdate);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int s = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-s);//根据日历的规则，给当前日期减往星期几与一个星期第一天的差值

        String imptimeBegin = sdf.format(cal.getTime());
        //  System.out.println("所在周星期日的日期："+imptimeBegin);
        return imptimeBegin;
    }
    /**
     * 获取当前日期当前一周的结束日期 （周六）
     */
    public static String getCurrentWeekEndDayByDate(String date) {
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        Date newDate = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(newDate);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int s = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()+(6-s));

        String imptimeBegin = sdf.format(cal.getTime());
        return imptimeBegin;
    }

    /**
     *
     * @param date
     * @return
     */
    public static String previousMonthByDate(String date) {
        // TODO Auto-generated method stub
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 2, 1);
        Date newdate = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(newdate);
        String imptimeBegin = sdf.format(cal.getTime());
//	    System.out.println(imptimeBegin);
        return imptimeBegin;
    }

    /**
     * 返回下一个月的第一天
     * @param date 20120304
     * @return  20120401
     */
    public static String nextMonthByDate(String date) {
        // TODO Auto-generated method stub
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        Date newdate = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(newdate);
        String imptimeBegin = sdf.format(cal.getTime());
//	    System.out.println(imptimeBegin);
        return imptimeBegin;
    }
    /**
     * 返回当前月的第一天
     * @return 20120101
     */
    public static String getCurrentMonthFirstDayByDate(String date) {
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, 1);
        Date newdate = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(newdate);
        String imptimeBegin = sdf.format(cal.getTime());
        return imptimeBegin;
    }

    public static int getLastMonth(){

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if(c.get(Calendar.MONTH) + 1 == 1){
            return c.get(Calendar.YEAR) - 1;
        }
        return c.get(Calendar.YEAR);

    }

    public static int getLastMonthYear(){

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if(c.get(Calendar.MONTH) + 1 == 1){
            return 12;
        }
        return c.get(Calendar.MONTH);

    }

    public static int getMaxDayByYearMonth(int year,int month) {
        int maxDay = 0;
        int day = 1;
        /**
         * 与其他语言环境敏感类一样，Calendar 提供了一个类方法 getInstance，
         * 以获得此类型的一个通用的对象。Calendar 的 getInstance 方法返回一
         * 个 Calendar 对象，其日历字段已由当前日期和时间初始化：
         */
        Calendar calendar = Calendar.getInstance();
        /**
         * 实例化日历各个字段,这里的day为实例化使用
         */
        calendar.set(year,month - 1,day);
        /**
         * Calendar.Date:表示一个月中的某天
         * calendar.getActualMaximum(int field):返回指定日历字段可能拥有的最大值
         */
        maxDay = calendar.getActualMaximum(Calendar.DATE);
        return maxDay;
    }

    public static String getStartDateByYearAndMonth(int year, int month, int days){
       String strMonth = "";
       if(month < 10){
           strMonth = "0"+ month;
       }else {
           strMonth = String.valueOf(month);
       }
        return year + "-" + strMonth + "-"
                + "01" + "-"
                + "00" + ":" + "00"
                + ":" + "00";
    }

    public static String getEndDateByYearAndMonth(int year, int month, int days){

        String strMonth = "";
        if(month < 10){
            strMonth = "0"+ month;
        }else {
            strMonth = String.valueOf(month);
        }
        return year + "-" + strMonth + "-"
                + days + "-"
                + "23" + ":" + "59"
                + ":" + "59";
    }

    public static String getWeekendByYearMonthDay(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        String mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="星期天";
        }else if("2".equals(mWay)){
            mWay ="星期一";
        }else if("3".equals(mWay)){
            mWay ="星期二";
        }else if("4".equals(mWay)){
            mWay ="星期三";
        }else if("5".equals(mWay)){
            mWay ="星期四";
        }else if("6".equals(mWay)){
            mWay ="星期五";
        }else if("7".equals(mWay)){
            mWay ="星期六";
        }
        return mWay;
    }

    public static boolean  getIsWorkDayByYearMonthDay(int year, int month, int day){

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        String mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));

        if("1".equals(mWay)){
            return false;
        }else if("2".equals(mWay)){
            return true;
        }else if("3".equals(mWay)){
            return true;
        }else if("4".equals(mWay)){
            return true;
        }else if("5".equals(mWay)){
            return true;
        }else if("6".equals(mWay)){
            return true;
        }else if("7".equals(mWay)){
            return false;
        }

        return true;

    }

    public static String timerFormat(int second) {
        int hour = second/3600;
        int minute = (second%3600)/60;
        int s = second%60;
        return String.format("%02d:%02d:%02d", hour, minute, s);
    }
}
