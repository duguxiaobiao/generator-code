package com.lonely.bean;

import lombok.Data;

@Data
public class ConditionBean {

    //左边条件
    private String leftExist;

    private String param;
    //左边条件类型
    private String leftVarMarkType;
    //右边条件
    private String rightExist;

    private String val;
    //右边边条件类型
    private String rightMarkType;
    //中间判断
    private String judge;
    //是否整数类型
    private boolean fixed;

}
