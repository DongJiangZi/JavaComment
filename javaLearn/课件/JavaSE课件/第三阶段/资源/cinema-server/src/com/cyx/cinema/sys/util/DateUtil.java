package com.cyx.cinema.sys.util;

import com.cyx.cinema.sys.entity.FilmPlan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
    /**
     * 日期转字符串
     * @param date
     * @return
     */
    public static String date2str(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 字符串转日期
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date str2Date(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(dateStr);
    }

    /**
     * 播放计划是否冲突
     * @param plan1
     * @param plan2
     * @return
     */
    public static boolean isConflictPlan(FilmPlan plan1, FilmPlan plan2){
        Date begin1 = plan1.getBegin();
        Date begin2 = plan2.getBegin();

        Date end1 = plan1.getEnd();
        Date end2 = plan2.getEnd();
        //2020-10-10 10:00:00 ~ 2020-10-10 12:00:00

        //2020-10-10 09:00:00 ~ 2020-10-10 11:00:00
        //2020-10-10 10:20:00 ~ 2020-10-10 11:50:00
        //2020-10-10 11:00:00 ~ 2020-10-10 13:00:00
        //首先保证是同一个影厅
        if(plan1.getFilmHall().getId().equals(plan2.getFilmHall().getId())){
            boolean case1 = begin2.before(begin1) && end2.after(begin1) && end2.before(end1);
            boolean case2 = begin2.after(begin1) && begin2.before(end2) && end2.before(end1);
            boolean case3 = begin2.after(begin1) && begin2.before(end2) && end2.after(end1);
            return case1 || case2 || case3;
        }
        return false;
    }
}
