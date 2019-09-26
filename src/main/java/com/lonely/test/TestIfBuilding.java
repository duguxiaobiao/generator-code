package com.lonely.test;

import com.lonely.bean.ConditionBean;
import com.lonely.util.BudingIfUtil;

import java.util.ArrayList;
import java.util.List;

public class TestIfBuilding {
    public static void main(String[] args) {
        List<List<ConditionBean>> val = new ArrayList<>();
        List<ConditionBean> conditionBeans = new ArrayList<ConditionBean>();
        List<ConditionBean> conditionBeans2 = new ArrayList<ConditionBean>();
        ConditionBean conditionBean = new ConditionBean();
        conditionBean.setFixed(true);
        conditionBean.setJudge("=");
        conditionBean.setLeftExist("2");
        conditionBean.setVal("3");
        conditionBean.setLeftVarMarkType("String");
        conditionBean.setRightExist("2");
        conditionBean.setParam("3");
        conditionBean.setRightMarkType("String");
        conditionBeans.add(conditionBean);
        ConditionBean conditionBean2 = new ConditionBean();
        conditionBean2.setFixed(true);
        conditionBean2.setJudge("=");
        conditionBean2.setLeftExist("2");
        conditionBean2.setVal("3");
        conditionBean2.setLeftVarMarkType("Integer");
        conditionBean2.setRightExist("2");
        conditionBean2.setParam("3");
        conditionBean2.setRightMarkType("Integer");
        //层级并且
        //conditionBeans.add(conditionBean2);
        conditionBeans2.add(conditionBean2);
        val.add(conditionBeans);
        //同级或者
       // val.add(conditionBeans2);
       String vue =  BudingIfUtil.BudingIf(val,"System.out.println(123);");
       System.out.print(vue);
    }
}
