package com.lonely.util;

public class BudingLogUtil {
    public static String BudingLogFactory(String serviceName){
        StringBuffer vue = new StringBuffer("private static final org.slf4j.Logger logger = LoggerFactory.getLogger(");
        vue.append(serviceName);
        vue.append(".class");
        vue.append(");");
        return vue.toString();
    }
}
