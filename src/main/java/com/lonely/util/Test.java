package com.lonely.util;

/**
 * @author ztkj-hzb
 * @Date 2019/9/24 11:39
 * @Description
 */
public class Test {


    public static void main(String[] args) {

        String a = "school[A1].name";
        String b = "school[1].name[2]";

        System.out.println(b.matches("school\\[\\d?].name"));
        System.out.println(b.matches("[\\S\\s]*\\[\\d?][\\S\\s]*"));

        String regex =  "[\\S\\s]*\\[[A,L,?]\\d?][\\S\\s]*";
        System.out.println(a.matches(regex));


        //String a = "school[?0].name[?1]";
        //System.out.println(StringUtil.subStringListFormat(a,"[?1]"));


    }



}
