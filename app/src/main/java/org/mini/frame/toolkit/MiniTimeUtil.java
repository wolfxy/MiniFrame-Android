package org.mini.frame.toolkit;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Wuquancheng on 15/4/8.
 */
public class MiniTimeUtil {
    public static final String datetimeFormat = "yyyy年MM月dd日 HH:mm";
    public static final String homeWorkFormat = "MM月dd日 HH:mm";
    public static final String scoreFormat = "yyyy年MM月dd日";
    public static final String homeWorkSubmitTimeFormat = "yyyy年MM月dd日";
    public static final String search_Format = "yyyy-MM-dd";

    /**
     * 下拉获取系统当前时间
     *
     * @return
     */
    public static String getPullToRefreshTime(long createTime) {
        Date date = new Date(createTime);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String fromTimestamp(Long time) {
        String format;
        Calendar current = Calendar.getInstance();
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);

        if (calendar.after(current)) { // is today
            format = "HH:mm";
        } else {
            current.add(Calendar.DAY_OF_MONTH, -1);
            if (calendar.after(current)) { // is yesterday
                format = "昨天 HH:mm";
            } else if (current.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                format = "MM-dd";
            } else { // before yesterday
                format = "yyyy-MM-dd";
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(calendar.getTime());
    }


    /**
     * 显示今天、昨天 日期
     *
     * @param createTimeInSec
     * @return
     */
    public static String getClassFormatTime(long createTimeInSec) {
        return getDateFormatDescription(createTimeInSec);
    }

    public static String getDateFormatDescription(long createTimeInSec) {
        Date createDate = new Date(createTimeInSec * 1000);
        Calendar createCalendar = Calendar.getInstance();
        createCalendar.setTime(createDate);

        Calendar today = Calendar.getInstance(); // 今天
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance(); // 昨天
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        if (createCalendar.after(today)) {
            String todayTime = convertTimeFormat(createTimeInSec + "", "HH:mm");
            return todayTime;
        } else if (createCalendar.before(today) && createCalendar.after(yesterday)) {
            return "昨天  " + convertTimeFormat(createTimeInSec + "", "HH:mm");
        } else if (createCalendar.get(Calendar.YEAR) < today.get(Calendar.YEAR)) {
            String defaultTime = convertTimeFormat(createTimeInSec + "", "yyyy-MM-dd HH:mm");
            return defaultTime;
        } else {
            String defaultTime = convertTimeFormat(createTimeInSec + "", "MM-dd HH:mm");
            return defaultTime;
        }
    }

    /**
     * 作业提交时间格式
     * @param time
     * @return
     */
    public static String getHomeWorkFormat(Long time) {
        String format;
        Calendar current = Calendar.getInstance();
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);

        if (calendar.after(current)) { // is today
            format = "MM月dd日 HH:mm";
        } else {
            current.add(Calendar.DAY_OF_MONTH, -1);
            if (calendar.after(current)) { // is yesterday
                format = "MM月dd日 HH:mm";
            } else if (current.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                format = "MM月dd日 HH:mm";
            } else { // before yesterday
                format = "yyyy年MM月dd日 HH:mm";
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(calendar.getTime());
    }


    /**
     * 成绩时间
     * @param time
     * @return
     */
    public static String ScoreTimestamp(Long time) {
        String format;
        Calendar current = Calendar.getInstance();
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);

        if (calendar.after(current)) { // is today
            format = "MM-dd";
        } else {
            current.add(Calendar.DAY_OF_MONTH, -1);
            if (calendar.after(current)) { // is yesterday
                format = "MM-dd";
            } else if (current.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                format = "MM-dd";
            } else { // before yesterday
                format = "yyyy-MM-dd";
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(calendar.getTime());
    }


    public static String formatBirthday(long dataInSecond) {
        return formatTime(dataInSecond, "yyyy-MM-dd");
    }

    public static String formatTime(long dataInSecond, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dataInSecond * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(calendar.getTime());
    }


    /**
     * 获取当前时间
     * @return
     */
    public static String currentTime() {
        long timeMillis = System.currentTimeMillis();
        Date createDate = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(search_Format);
        String currentTime = dateFormat.format(createDate);
        return currentTime;
    }

    /**
     * 获取昨天的时间
     * @return
     */
    public static String lastMonthTime(){
        SimpleDateFormat sdf=new SimpleDateFormat(search_Format);
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(new Date());
        lastDate.add(Calendar.YEAR, -1);//
        return sdf.format(lastDate.getTime());
    }







    /**
     * 根据String类型2014-01-02 15:45 转换为秒
     *
     * @param time
     * @return
     */
    public static Long getTimeByStringTime(String time) {
        Long timeValue;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimeFormat, Locale.CHINA);
        try {
            Date parse = simpleDateFormat.parse(time);
            timeValue = (parse.getTime() / 1000);
        } catch (java.text.ParseException e) {
            timeValue = 0l;
            e.printStackTrace();
        }
        return timeValue;
    }

    public static Long getTimeByStringTime(String time, String datetimeFormat) {
        Long timeValue;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimeFormat, Locale.CHINA);
        try {
            Date parse = simpleDateFormat.parse(time);
            timeValue = (parse.getTime() / 1000);
        } catch (java.text.ParseException e) {
            timeValue = 0l;
            e.printStackTrace();
        }
        return timeValue;
    }

    /**
     * 作业的提交时间
     *
     * @return
     */
    public static String getSubjectTimeByStringTime(long ...homeWorkSubmitTime) {
        SimpleDateFormat format = new SimpleDateFormat(homeWorkSubmitTimeFormat);
        long tomorrowTime;
        if (homeWorkSubmitTime!=null&&homeWorkSubmitTime.length>0){
            tomorrowTime=homeWorkSubmitTime[0];
        }else{
            long currentTime = System.currentTimeMillis();
            tomorrowTime = currentTime + 24 * 60 * 60 * 1000;
        }
        Date date = new Date(tomorrowTime);
        String tomorrowDate = format.format(date);
        return tomorrowDate + "  08:00";
    }


    /**
     * 发作业的
     *
     * @param timeStamp
     * @return
     */
    public static int getWeek(Long timeStamp) {
        Date date = new Date(timeStamp);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String weekDay = new SimpleDateFormat("EEEE").format(date);
        if ("星期五".equals(weekDay)) {
            return 3;
        } else if ("星期六".equals(weekDay)) {
            return 2;
        } else if ("星期日".equals(weekDay)) {
            return 1;
        } else {
            return 0;
        }
    }


    /**
     * 将存储date.getTime()返回值的字符串转化为指定的格式
     *
     * @param longstr
     * @param formartString
     * @return
     */
    public static String convertTimeFormat(String longstr, String... formartString) {
        if (longstr == null || longstr.equals(""))
            return "";
        try {
            long timeStamp = Long.valueOf(longstr);
            Date date = new Date(timeStamp * 1000);
            if (formartString.length > 0) {
                return formatDate(date, formartString[0]);
            } else {
                return formatDate(date);
            }
        } catch (Exception e) {
            return "";
        }

    }


    /**
     * 将时间输出为特定格式的字符串
     *
     * @param date          时间
     * @param formartString 自定义的格式字符串
     * @return 转换后的时间字符串
     */
    public static String formatDate(Date date, String formartString) {
        SimpleDateFormat df = new SimpleDateFormat(formartString);
        return df.format(date);
    }

    /**
     * 将时间输出为特定格式的字符串(格式yyyy-MM-dd HH:mm:ss)
     *
     * @param date 需要转换的时间
     * @return 指定格式的字符串
     */
    public static String formatDate(Date date) {
        String result = "";
        SimpleDateFormat df = new SimpleDateFormat(datetimeFormat);
        try {
            result = df.format(date);
        } catch (Exception e) {
            result = "";
        } finally {
            return result;
        }

    }

    public static Date parseDateByStringTime(String time) throws java.text.ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimeFormat, Locale.CHINA);
        return simpleDateFormat.parse(time);
    }


    /**
     * 计算两个时间之间的秒数差值
     *
     * @param date_pre
     * @param date_last
     * @return
     */
    public static float between_seconds(Date date_pre, Date date_last) {
        long second = date_last.getTime() - date_pre.getTime();
        double minute = second / 1000;
        minute = round(minute, 0);
        return (float) minute;
    }

    /**
     * 采用四舍五入算法，将v1压缩到小数点后指定位数
     *
     * @param v1    想要压缩的double
     * @param scale 小数点后位数
     * @return
     */
    public static double round(double v1, int scale) {
        BigDecimal b = new BigDecimal(Double.toString(v1));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

    }


    public static int getWeekInt(String week) {
        if (week.equals("周一")) {
            return 0;
        } else if (week.equals("周二")) {
            return 1;
        } else if (week.equals("周三")) {
            return 2;
        } else if (week.equals("周四")) {
            return 3;
        } else if (week.equals("周五")) {
            return 4;
        } else if (week.equals("周六")) {
            return 5;
        } else if (week.equals("周日")) {
            return 6;
        }
        return 0;
    }

    public static String getWeekString(int index) {
        if (index == 0) {
            return "周一";
        } else if (index == 1) {
            return "周二";
        } else if (index == 2) {
            return "周三";
        } else if (index == 3) {
            return "周四";
        } else if (index == 4) {
            return "周五";
        } else if (index == 5) {
            return "周六";
        } else if (index == 6) {
            return "周日";
        }
        return null;
    }

}
