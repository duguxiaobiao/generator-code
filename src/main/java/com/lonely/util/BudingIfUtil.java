package com.lonely.util;

import com.lonely.bean.ConditionBean;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BudingIfUtil {
    public static String BudingIf(List<List<ConditionBean>> Exsit, String ifBodys) {
        StringBuffer javaBuffer = new StringBuffer("");
        javaBuffer.append(" if (");
        String expCode = Exsit.stream().map(x -> {
            String andCode = x.stream().map(ex -> {
                if (ex.isFixed()) {
                    ex.setVal(ex.getVal());
                } else {
                    ex.setVal(expTransition(ex.getRightExist(), ex.getRightMarkType()));
                }
                ex.setParam(expTransition(ex.getLeftExist(), ex.getLeftVarMarkType()));
                String exps = "";
                String preCase = "";
                String valPreCase = "";

                if (ex.getLeftVarMarkType().equals("Integer")) {
                    preCase = ".intValue()";
                    valPreCase = ".intValue()";
                } else if (ex.getLeftVarMarkType().equals("Double")) {
                    preCase = ".doubleValue()";
                    valPreCase = ".doubleValue()";
                } else if (ex.getLeftVarMarkType().equals("Boolean")) {
                    preCase = ".booleanValue()";
                    valPreCase = ".booleanValue()";
                }

                if (patternMatcher(ex.getVal())) {
                    //如果结果值是整数或者小数,则不需要进行后缀
                    valPreCase = "";
                }

                if (ex.getJudge().equals("=")) {

                    if (StringUtils.isNotEmpty(ex.getRightMarkType()) && StringUtils.isNotEmpty(ex.getLeftVarMarkType())) {
                        if (ex.getLeftVarMarkType().equals("String")) {
                            exps = ex.getParam() + preCase + ".equals(" + ex.getVal() + valPreCase + ")";
                        } else {
                            exps = ex.getParam() + preCase + "==" + ex.getVal() + valPreCase;
                        }

                    } else {
                        //结果是非变量
                        if (ex.getLeftVarMarkType().equals("String")) {
                            exps = ex.getParam() + preCase + ".equals(" + ex.getVal() + valPreCase + ")";
                        } else {
                            exps = ex.getParam() + preCase + "==" + ex.getVal() + "";
                        }
                    }
                    return exps.replace("\\", "");
                } else {
                    return ex.getParam() + preCase + ex.getJudge() + ex.getVal() + valPreCase;
                }
            }).collect(Collectors.joining("&&"));
            return "(" + andCode + ")";
        }).collect(Collectors.joining("||"));
        javaBuffer.append(expCode);
        javaBuffer.append(")");
        javaBuffer.append("{");
        javaBuffer.append(ifBodys);
        javaBuffer.append("}");
        return javaBuffer.toString();
    }

    public static String expTransition(String val, String type) {
        String[] vals = val.split("\\.");
        String headValMap = vals[0];
        String tempValue = "";
        if (vals.length == 1) {
            return headValMap;
        } else if (vals.length > 2) {
            for (int i = 1; i <= vals.length - 2; i++) {
                //取到赋值字段前一个
                tempValue = "((java.util.Map)" + headValMap + ".get(\"" + vals[i] + "\"))";
                headValMap = tempValue;
            }
        }
        //获取最后一个字段进行取值
        String valKey = vals[vals.length - 1];
        headValMap = headValMap + ".get(\"" + valKey + "\")";
        if (type.equals("Integer")) {
            headValMap = "((java.lang.Integer)" + headValMap + ")";
        } else if (type.equals("Double")) {
            headValMap = "((java.lang.Double)" + headValMap + ")";
        } else if (type.equals("Boolean")) {
            headValMap = "((java.lang.Boolean)" + headValMap + ")";
        } else if (type.equals("String")) {
            headValMap = "((java.lang.String)" + headValMap + ")";
        }
        return headValMap;
    }

    private static boolean patternMatcher(String val) {
        //非负整数
        String feifuzhengshu = "^[1-9]\\d*|0$";
        //负整数
        String fuzhengshu = "^-[1-9]\\d*$";
        //正小数
        String zhengxiaoshu = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
        //负小数
        String fuxiaoshu = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";
        Pattern pattern = Pattern.compile(feifuzhengshu);
        Pattern pattern2 = Pattern.compile(fuzhengshu);
        Pattern pattern3 = Pattern.compile(zhengxiaoshu);
        Pattern pattern4 = Pattern.compile(fuxiaoshu);
        return (pattern.matcher(val).matches() || pattern2.matcher(val).matches() || pattern3.matcher(val).matches() || pattern4.matcher(val).matches());
    }
}
