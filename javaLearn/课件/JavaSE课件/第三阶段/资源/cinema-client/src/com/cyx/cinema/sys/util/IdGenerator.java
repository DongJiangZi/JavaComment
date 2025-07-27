package com.cyx.cinema.sys.util;//util包通常都是工具类包

import java.util.Random;

/**
 * ID生成器
 */
public class IdGenerator {

    private static Random RANDOM = new Random();

    private static final char[] characters = {
            'A','B','C','D','E','F','G','H','I','J','K','L',
            'M','N','O','P','Q','R','S','T','U','V','W','X',
            'Y','Z','0','1','2','3','4','5','6','7','8','9'
    };

    /**
     * 跟给定的字符串长度生成ID
     * @param length 长度
     * @return
     */
    public static String generateId(int length){
        StringBuilder sb = new StringBuilder("CYX_");
        for(int i=0; i<length; i++){
            int index = RANDOM.nextInt(characters.length);
            sb.append(characters[index]);
        }
        return sb.toString();
    }

}
