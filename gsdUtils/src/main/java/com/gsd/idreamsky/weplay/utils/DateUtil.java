package com.gsd.idreamsky.weplay.utils;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.gsd.utils.R;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by magical.zhang on 2017/3/28.
 * Description :
 */

public class DateUtil {

    private static final String TAG = DateUtil.class.getSimpleName();

    public static String getDuration(long second) {
        StringBuilder time = new StringBuilder();
        long hour = second / 60 / 60;
        if (hour > 0) {
            time.append(String.format("%02d:", hour));
        }
        second -= hour * 60 * 60;
        time.append(String.format("%02d:%02d", second / 60, second % 60));
        return time.toString();
    }


    public static boolean isSameDay(long time1,long time2){
        String day1 = getFormatDate2(time1);
        String day2 = getFormatDate2(time2);
        if (day1.equals(day2)){//不是同一天 则弹窗
//            LogUtil.i(TAG,lastDate+" and "+today+" is not the same day");
            return true;
        }
        return false;
    }

    /**
     * 返回 06-21 09:45 的时间格式
     *
     * @param timeStamp 时间戳 单位 秒
     */
    public static String getFormatDate2(long timeStamp) {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timeStamp);
        return dateFormater.format(date);
    }

    /**
     * 返回 06-21 09:45 的时间格式
     * php 那边返回数据是精确到秒的 这边手动处理成 毫秒
     *
     * @param timeStamp 时间戳 单位 秒
     */
    public static String getFormatDate(long timeStamp) {
        SimpleDateFormat dateFormater = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(timeStamp * 1000);
        return dateFormater.format(date);
    }

    /**
     * 返回 06-21 09:45 的时间格式
     * php 那边返回数据是精确到秒的 这边手动处理成 毫秒
     *
     * @param timeStamp 时间戳 单位 秒
     */
    public static String getFullFormatDate(long timeStamp) {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(timeStamp * 1000);
        return dateFormater.format(date);
    }

    /**
     * 时间转化为显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        if (calendar.before(inputTime)) {
            //今天23:59在输入时间之前，解决一些时间误差，把当天时间显示到这里
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + UtilsApplication.getContext()
                    .getResources()
                    .getString(R.string.time_year) + "MM" + UtilsApplication.getContext()
                    .getResources()
                    .getString(R.string.time_month) + "dd" + UtilsApplication.getContext()
                    .getResources()
                    .getString(R.string.time_day));
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            return UtilsApplication.getContext().getResources().getString(R.string.time_yesterday);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + UtilsApplication.getContext()
                        .getResources()
                        .getString(R.string.time_month) + "d" + UtilsApplication.getContext()
                        .getResources()
                        .getString(R.string.time_day));
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + UtilsApplication.getContext()
                        .getResources()
                        .getString(R.string.time_year) + "MM" + UtilsApplication.getContext()
                        .getResources()
                        .getString(R.string.time_month) + "dd" + UtilsApplication.getContext()
                        .getResources()
                        .getString(R.string.time_day));
                return sdf.format(currenTimeZone);
            }
        }
    }

    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getChatTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        if (!calendar.after(inputTime)) {
            //当前时间在输入时间之前
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + UtilsApplication.getContext()
                    .getResources()
                    .getString(R.string.time_year) + "MM" + UtilsApplication.getContext()
                    .getResources()
                    .getString(R.string.time_month) + "dd" + UtilsApplication.getContext()
                    .getResources()
                    .getString(R.string.time_day));
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return UtilsApplication.getContext().getResources().getString(R.string.time_yesterday)
                    + " "
                    + sdf.format(currenTimeZone);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + UtilsApplication.getContext()
                        .getResources()
                        .getString(R.string.time_month) + "d" + UtilsApplication.getContext()
                        .getResources()
                        .getString(R.string.time_day) + " HH:mm");
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + UtilsApplication.getContext()
                        .getResources()
                        .getString(R.string.time_year) + "MM" + UtilsApplication.getContext()
                        .getResources()
                        .getString(R.string.time_month) + "dd" + UtilsApplication.getContext()
                        .getResources()
                        .getString(R.string.time_day) + " HH:mm");
                return sdf.format(currenTimeZone);
            }
        }
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return days;
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取格式的时间
     */
    public static String getFormatString(String raw) {

        if (TextUtils.isEmpty(raw)) {
            return "";
        }
        long rawTimeStamp = Long.valueOf(raw).longValue();
        long nowTimeStamp = System.currentTimeMillis();
        //Log.d("magical", "raw : " + rawTimeStamp);
        //Log.d("magical", "now : " + nowTimeStamp);

        long diff = nowTimeStamp - rawTimeStamp;
        long secondDiff = diff / 1000;
        if (secondDiff < 60) {
            return "刚刚";
        }

        long minute = secondDiff / 60;
        if (minute < 60) {
            return String.format("%s分钟前", String.valueOf(minute));
        }

        long hour = minute / 60;
        if (hour < 24) {
            return String.format("%s小时前", String.valueOf(hour));
        }

        long day = hour / 24;
        if (day < 30) {
            return String.format("%s天前", String.valueOf(day));
        }

        long month = day / 30;
        if (month < 12) {
            return String.format("%s月前", String.valueOf(month));
        }

        long year = month / 12;
        return String.format("%s年前", String.valueOf(year));
    }

    /**
     * 获取格式的时间
     */
    public static String getFormatString(long rawTimeStamp) {

        long nowTimeStamp = System.currentTimeMillis();

        long diff = nowTimeStamp - rawTimeStamp;
        long secondDiff = diff / 1000;
        if (secondDiff < 60) {
            return "刚刚";
        }

        long minute = secondDiff / 60;
        if (minute < 60) {
            return String.format("%s分钟前", String.valueOf(minute));
        }

        long hour = minute / 60;
        if (hour < 24) {
            return String.format("%s小时前", String.valueOf(hour));
        }

        long day = hour / 24;
        if (day < 30) {
            return String.format("%s天前", String.valueOf(day));
        }

        long month = day / 30;
        if (month < 12) {
            return String.format("%s月前", String.valueOf(month));
        }

        long year = month / 12;
        return String.format("%s年前", String.valueOf(year));
    }

    private static long string2Timestamp(@NonNull String formatDate) {

        Timestamp ts;
        try {
            ts = Timestamp.valueOf(formatDate);
            return ts.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取某天0点的时间戳
     *
     * @param type 明天1 昨天-1 今天0
     * @return 时间戳
     */
    public static long getZeroTimeMillis(@IntRange(from = -1, to = 1) int type) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (type != 0) {
            calendar.add(Calendar.DATE, type);
        }

        long timeInMillis = calendar.getTimeInMillis();

        //String ss = getFormatDate(timeInMillis / 1000);
        //LogUtil.d(TAG, " raw time : " + timeInMillis + " format : " + ss);
        return timeInMillis;
    }
}
