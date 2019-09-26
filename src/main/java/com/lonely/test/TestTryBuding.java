package com.lonely.test;

import com.lonely.util.BudingTryUtil;

public class TestTryBuding {
    public static void main(String[] args) {
        String val = "if ((2.equals(3))||(2.equals(3))){System.out.println(123);}";
        String vue = BudingTryUtil.BudingTry(val);
        System.out.print(vue);
    }
}
