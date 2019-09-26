package com.lonely.test;

import com.lonely.util.BudingLogUtil;

public class TestLogFactoryUtil {
    public static void main(String[] args) {
        String serviceName = "TestLogFactoryUtil";
        String vue = BudingLogUtil.BudingLogFactory(serviceName);
        System.out.print(vue);
    }
}
