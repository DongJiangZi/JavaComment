package com.cyx.cinema.platform.server.util;

import com.cyx.cinema.platform.entity.FilmPlan;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 影片计划校验器：主要检测时间是否冲突
 */
public class FilmPlanValidator {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 检测两个影片计划是否存在时间冲突
     * @param p1
     * @param p2
     * @return
     */
    public static boolean isConflictPlan(FilmPlan p1, FilmPlan p2){
        String owner1 = p1.getOwner().getUsername();
        String owner2 = p2.getOwner().getUsername();
        String playDate1 = p1.getPlayDate();
        String playDate2 = p1.getPlayDate();
        //同一商家同一天才可能出现时间冲突
        if(owner1.equals(owner2) && playDate1.equals(playDate2)){
            String begin1 = playDate1 + " " + p1.getBeginTime();
            String begin2 = playDate2 + " " + p2.getBeginTime();

            String end1 = playDate1 + " " + p1.getEndTime();
            String end2 = playDate2 + " " + p2.getEndTime();

            try {
                //10-20
                //9-11  11-13  18-22
                long beginTime1 = FORMAT.parse(begin1).getTime();
                long beginTime2 = FORMAT.parse(begin2).getTime();

                long endTime1 = FORMAT.parse(end1).getTime();
                long endTime2 = FORMAT.parse(end2).getTime();
                boolean case1 = beginTime2 <= beginTime1 && beginTime2 <= endTime2 && endTime2 >= beginTime1 && endTime2 <= endTime1;
                boolean case2 = beginTime2 >= beginTime1 && beginTime2 < endTime2 && endTime2 <= endTime1;
                boolean case3 = beginTime2 >= beginTime1 && beginTime2 <= endTime1 && endTime2 > endTime1;
                return case1 || case2 || case3;
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }
}
