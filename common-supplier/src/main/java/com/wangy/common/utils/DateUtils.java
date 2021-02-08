package com.wangy.common.utils;


import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * Date转换为LocalDateTime  当前时间
     */
    public static LocalDateTime getLocalDateTime() {

        return getNowDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
//        return DateFormatUtils.format(now, "yyyy/MM/dd");
        return new SimpleDateFormat("yyyy/MM/dd").format(now);
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return new SimpleDateFormat("yyyyMMdd").format(now);
    }

    /**
     * 日期路径 即年/月/日 如2018-08-08
     */
    public static final String dateTimeOne(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(YYYY_MM_DD).format(date);
    }

    /**
     * 获取一段时间的每一天日期
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static List<String> getBetweenDate(String start, String end) {
        List<String> list = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> list.add(f.toString()));
        return list;
    }

    /**
     * 拿到时间段内 每个月的 YYYY-MM 格式时间
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static List<String> getBetweenDate2(String start, String end) {
        List<String> list = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        long distance = ChronoUnit.MONTHS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> d.plusMonths(1)).limit(distance + 1).forEach(f -> list.add(f.format(DateTimeFormatter.ofPattern("yyyy-MM"))));
        return list;
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    private static Date parseDate(final String str,  final String[] parsePatterns) throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }

        final TimeZone tz = TimeZone.getDefault();
        final Locale lcl = Locale.getDefault();
        final Calendar calendar = Calendar.getInstance(tz, lcl);
        calendar.setLenient(true);

        for (final String parsePattern : parsePatterns) {
            calendar.clear();
            try {
                calendar.setTime(new SimpleDateFormat(parsePattern).parse(str));
                return calendar.getTime();
            } catch(final IllegalArgumentException ignore) {
                // leniency is preventing calendar from being set
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 字符串时间 转date
     *
     * @param time
     * @return
     */
    public static Date getDateOne(String time) throws ParseException {
        if (time.isEmpty()) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(time);
    }

    /**
     * String 时间转 date 时间
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }

    /**
     * 将Date 类型的时间格式转换未 yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String getDateTwo(Date time, String simple) {
        if (time == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(simple);

        return simpleDateFormat.format(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }
}
