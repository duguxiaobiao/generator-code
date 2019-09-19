package com.lonely.util;

/**
 * @author ztkj-hzb
 * @Date 2019/9/19 11:03
 * @Description
 */
public class StringUtil {

    /**
     * 首字符大写
     *
     * @param str
     * @return
     */
    public static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

}
