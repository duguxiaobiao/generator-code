package com.lonely.test;

import com.lonely.bean.ModelDesc;
import com.lonely.bean.ParamTypeDesc;
import com.lonely.generator.DefaultModelHandler;
import com.lonely.util.ClassUtil;
import com.lonely.util.DefaultModelBuildUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztkj-hzb
 * @Date 2019/9/25 10:30
 * @Description 测试数组场景
 */
public class TestArrayBeanGenera {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        testArray();
    }


    public static void testArray() throws IllegalAccessException, InstantiationException {
        List<ParamTypeDesc> paramTypeDescs = new ArrayList<>();

        /*ParamTypeDesc paramTypeDesc1 = new ParamTypeDesc();
        paramTypeDesc1.setParamName("name");
        paramTypeDesc1.setCommonTypeName("java.lang.String");
        paramTypeDesc1.setExistsT(false);

        ParamTypeDesc paramTypeDesc2 = new ParamTypeDesc();
        paramTypeDesc2.setParamName("age");
        paramTypeDesc2.setCommonTypeName("java.lang.Integer");
        paramTypeDesc2.setExistsT(false);


        //来一个基础数据类型的数组
        ParamTypeDesc paramTypeDesc3 = new ParamTypeDesc();
        paramTypeDesc3.setParamName("hobbys");
        paramTypeDesc3.setCommonTypeName("java.lang.String");
        paramTypeDesc3.setExistsT(false);
        paramTypeDesc3.setArray(true);*/

        //来一个对象类型的数组
        /*ParamTypeDesc paramTypeDesc4 = new ParamTypeDesc();
        paramTypeDesc4.setParamName("clazz");
        paramTypeDesc4.setCommonTypeName("java.util.HashMap");
        List<ParamTypeDesc> childParams = new ArrayList<>();
        ParamTypeDesc child1 = new ParamTypeDesc();
        child1.setParamName("className");
        child1.setCommonTypeName("java.lang.String");
        child1.setExistsT(false);

        ParamTypeDesc child2 = new ParamTypeDesc();
        child2.setParamName("student");
        child2.setCommonTypeName("java.util.HashMap");
        List<ParamTypeDesc> childParams2 = new ArrayList<>();
        ParamTypeDesc child2_1 = new ParamTypeDesc();
        child2_1.setParamName("studentName");
        child2_1.setCommonTypeName("java.lang.String");
        child2_1.setExistsT(false);
        ParamTypeDesc child2_2 = new ParamTypeDesc();
        child2_2.setParamName("studentAge");
        child2_2.setCommonTypeName("java.lang.Integer");
        child2_2.setExistsT(false);
        childParams2.add(child2_1);
        childParams2.add(child2_2);
        child2.setChildParams(childParams2);
        child2.setExistsT(false);

        childParams.add(child1);
        childParams.add(child2);

        paramTypeDesc4.setChildParams(childParams);
        paramTypeDesc4.setExistsT(false);
        paramTypeDesc4.setArray(true);*/

        //嵌套数组测试  user[].student[]
        ParamTypeDesc paramTypeDesc4 = new ParamTypeDesc();
        paramTypeDesc4.setParamName("clazz");
        paramTypeDesc4.setCommonTypeName("java.util.List");
        List<ParamTypeDesc> childParams = new ArrayList<>();
        ParamTypeDesc child1 = new ParamTypeDesc();
        child1.setParamName("className");
        child1.setCommonTypeName("java.lang.String");
        child1.setExistsT(false);

        ParamTypeDesc child2 = new ParamTypeDesc();
        child2.setParamName("student");
        child2.setCommonTypeName("java.util.HashMap");
        List<ParamTypeDesc> childParams2 = new ArrayList<>();
        ParamTypeDesc child2_1 = new ParamTypeDesc();
        child2_1.setParamName("studentName");
        child2_1.setCommonTypeName("java.lang.String");
        child2_1.setExistsT(false);
        ParamTypeDesc child2_2 = new ParamTypeDesc();
        child2_2.setParamName("studentAge");
        child2_2.setCommonTypeName("java.lang.Integer");
        child2_2.setExistsT(false);

        childParams2.add(child2_1);
        childParams2.add(child2_2);
        child2.setChildParams(childParams2);
        child2.setExistsT(false);
        child2.setArray(true);

        childParams.add(child1);
        childParams.add(child2);

        paramTypeDesc4.setChildParams(childParams);
        paramTypeDesc4.setExistsT(true);
        paramTypeDesc4.setArray(true);


        //paramTypeDescs.add(paramTypeDesc1);
        //paramTypeDescs.add(paramTypeDesc2);
        //paramTypeDescs.add(paramTypeDesc3);
        paramTypeDescs.add(paramTypeDesc4);


        //1.构建Model
        ModelDesc modelDesc = DefaultModelBuildUtil.buildModelDescByParamType(paramTypeDescs);

        //2.构建代码
        String generator = DefaultModelHandler.generator(modelDesc);
        System.out.println(generator);

        //3.编译
        Class<?> compile = ClassUtil.compile(modelDesc.getMainClassName(), generator);

        //4.赋值
        Object newInstance = compile.newInstance();
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(newInstance);
        beanWrapper.setAutoGrowNestedPaths(true);
        beanWrapper.setPropertyValue("clazz[0][0].className", "终极一班");
        beanWrapper.setPropertyValue("clazz[0][0].student[0].studentName", "学生1");
        beanWrapper.setPropertyValue("clazz[0][0].student[0].studentAge", 18);
        beanWrapper.setPropertyValue("clazz[0][0].student[1].studentName", "学生2");
        beanWrapper.setPropertyValue("clazz[0][0].student[1].studentAge", 19);

        beanWrapper.setPropertyValue("clazz[0][1].className", "终极二班");
        beanWrapper.setPropertyValue("clazz[0][1].student[0].studentName", "学生1");
        beanWrapper.setPropertyValue("clazz[0][1].student[0].studentAge", 18);
        beanWrapper.setPropertyValue("clazz[0][1].student[1].studentName", "学生2");
        beanWrapper.setPropertyValue("clazz[0][1].student[1].studentAge", 19);


        //beanWrapper.setPropertyValue("clazz.student.studentName", "stu_lonely");
        //beanWrapper.setPropertyValue("clazz.student.studentAge", "26");

        System.out.println(newInstance);
    }


}
