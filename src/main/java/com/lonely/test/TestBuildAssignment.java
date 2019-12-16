package com.lonely.test;

import com.lonely.bean.ModelDesc;
import com.lonely.bean.ParamTypeDesc;
import com.lonely.generator.DefaultModelHandler;
import com.lonely.util.ClassUtil;
import com.lonely.util.DefaultBuildAssignmentExpression;
import com.lonely.util.DefaultModelBuildUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztkj-hzb
 * @Date 2019/9/25 12:01
 * @Description 测试表达式
 */
public class TestBuildAssignment {

    public static void main(String[] args) {
        //test1();
        test2();
        //test3();
        //test4();
        //test5();
    }

    /**
     * 构建一个List中 有List的对象的场景
     */
    public static void test1() {
        List<ParamTypeDesc> paramTypeDescs = new ArrayList<>();


        //list 对象类型
        ParamTypeDesc paramTypeDesc1 = new ParamTypeDesc();
        paramTypeDesc1.setParamName("teachers");
        paramTypeDesc1.setCommonTypeName("java.util.List");

        List<ParamTypeDesc> childParams1 = new ArrayList<>();
        ParamTypeDesc childParams1_1 = new ParamTypeDesc();
        childParams1_1.setParamName("teacherName");
        childParams1_1.setCommonTypeName("java.lang.String");
        childParams1_1.setExistsT(false);

        ParamTypeDesc childParams1_2 = new ParamTypeDesc();
        childParams1_2.setParamName("students");
        childParams1_2.setCommonTypeName("java.util.List");


        childParams1_2.setExistsT(true);

        List<ParamTypeDesc> childParamsList1_1 = new ArrayList<>();
        ParamTypeDesc childParamsList1_1_1 = new ParamTypeDesc();
        childParamsList1_1_1.setParamName("stuName");
        childParamsList1_1_1.setCommonTypeName("java.lang.String");
        childParamsList1_1_1.setExistsT(false);

        ParamTypeDesc childParamsList1_1_2 = new ParamTypeDesc();
        childParamsList1_1_2.setParamName("stuAge");
        childParamsList1_1_2.setCommonTypeName("java.lang.String");
        childParamsList1_1_2.setExistsT(false);
        childParamsList1_1.add(childParamsList1_1_1);
        childParamsList1_1.add(childParamsList1_1_2);

        childParams1_2.setChildParams(childParamsList1_1);

        childParams1.add(childParams1_1);
        childParams1.add(childParams1_2);

        paramTypeDesc1.setChildParams(childParams1);
        paramTypeDesc1.setExistsT(true);


        paramTypeDescs.add(paramTypeDesc1);


        //构建Map
        Map<String, Object> map = new HashMap<>();

        List<Map> teachersMap = new ArrayList<>();
        Map<String, Object> teacher1 = new HashMap<>();
        teacher1.put("teacherName", "teacher1");

        List<Map> studentsMap1 = new ArrayList<>();
        Map<String, Object> stu1 = new HashMap<>();
        stu1.put("stuName", "aa");
        stu1.put("stuAge", "18");
        studentsMap1.add(stu1);

        Map<String, Object> stu2 = new HashMap<>();
        stu2.put("stuName", "aa");
        stu2.put("stuAge", "18");
        studentsMap1.add(stu2);

        teacher1.put("students", studentsMap1);
        teachersMap.add(teacher1);

        Map<String, Object> teacher2 = new HashMap<>();
        teacher2.put("teacherName", "teacher2");

        List<Map> studentsMap2 = new ArrayList<>();
        Map<String, Object> stu3 = new HashMap<>();
        stu3.put("stuName", "aa");
        stu3.put("stuAge", "18");
        studentsMap2.add(stu3);

        Map<String, Object> stu4 = new HashMap<>();
        stu4.put("stuName", "aa");
        stu4.put("stuAge", "18");
        studentsMap2.add(stu4);

        teacher2.put("students", studentsMap2);
        teachersMap.add(teacher2);

        map.put("teachers", teachersMap);


        Map<String, Object> resultMap = DefaultBuildAssignmentExpression.buildAssignmentExpression(paramTypeDescs, map);
        System.out.println(resultMap);


        //1.构建Model
        ModelDesc modelDesc = DefaultModelBuildUtil.buildModelDescByParamType(paramTypeDescs);

        //2.构建代码
        String generator = DefaultModelHandler.generator(modelDesc);
        System.out.println(generator);

        //3.编译
        Class<?> compile = ClassUtil.compile(modelDesc.getMainClassName(), generator);

        //4.赋值
        try {
            Object newInstance = compile.newInstance();
            BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(newInstance);
            beanWrapper.setAutoGrowNestedPaths(true);
            beanWrapper.setPropertyValues(resultMap);
            System.out.println(newInstance);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 需要构建一个 List中 有对象的场景
     */
    public static void test2() {
        List<ParamTypeDesc> paramTypeDescs = new ArrayList<>();


        //list 对象类型
        ParamTypeDesc paramTypeDesc1 = new ParamTypeDesc();
        paramTypeDesc1.setParamName("teachers");
        paramTypeDesc1.setCommonTypeName("java.util.List");

        List<ParamTypeDesc> childParams1 = new ArrayList<>();
        ParamTypeDesc childParams1_1 = new ParamTypeDesc();
        childParams1_1.setParamName("teacherName");
        childParams1_1.setCommonTypeName("java.lang.String");
        childParams1_1.setExistsT(false);

        ParamTypeDesc childParams1_2 = new ParamTypeDesc();
        childParams1_2.setParamName("students");
        //childParams1_2.setCommonTypeName("java.util.HashMap");
        childParams1_2.setCommonTypeName("java.lang.Object");
        childParams1_2.setExistsT(false);

        List<ParamTypeDesc> childParamsList1_1 = new ArrayList<>();
        ParamTypeDesc childParamsList1_1_1 = new ParamTypeDesc();
        childParamsList1_1_1.setParamName("stuName");
        childParamsList1_1_1.setCommonTypeName("java.lang.String");
        childParamsList1_1_1.setExistsT(false);

        ParamTypeDesc childParamsList1_1_2 = new ParamTypeDesc();
        childParamsList1_1_2.setParamName("stuAge");
        childParamsList1_1_2.setCommonTypeName("java.lang.String");
        childParamsList1_1_2.setExistsT(false);
        childParamsList1_1.add(childParamsList1_1_1);
        childParamsList1_1.add(childParamsList1_1_2);

        childParams1_2.setChildParams(childParamsList1_1);

        childParams1.add(childParams1_1);
        childParams1.add(childParams1_2);

        paramTypeDesc1.setChildParams(childParams1);
        paramTypeDesc1.setExistsT(true);


        paramTypeDescs.add(paramTypeDesc1);


        //构建Map
        Map<String, Object> map = new HashMap<>();

        List<Map> teachersMap = new ArrayList<>();
        Map<String, Object> teacher1 = new HashMap<>();
        teacher1.put("teacherName", "teacher1");

        Map<String, Object> stu1 = new HashMap<>();
        stu1.put("stuName", "aa");
        stu1.put("stuAge", "18");

        teacher1.put("students", stu1);
        teachersMap.add(teacher1);

        Map<String, Object> teacher2 = new HashMap<>();
        teacher2.put("teacherName", "teacher2");

        Map<String, Object> stu3 = new HashMap<>();
        stu3.put("stuName", "aa");
        stu3.put("stuAge", "18");

        teacher2.put("students", stu3);
        teachersMap.add(teacher2);

        map.put("teachers", teachersMap);


        Map<String, Object> resultMap = DefaultBuildAssignmentExpression.buildAssignmentExpression(paramTypeDescs, map);
        System.out.println(resultMap);


        //1.构建Model
        ModelDesc modelDesc = DefaultModelBuildUtil.buildModelDescByParamType(paramTypeDescs);

        //2.构建代码
        String generator = DefaultModelHandler.generator(modelDesc);
        System.out.println(generator);

        //3.编译
        Class<?> compile = ClassUtil.compile(modelDesc.getMainClassName(), generator);

        //4.赋值
        try {
            Object newInstance = compile.newInstance();
            BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(newInstance);
            beanWrapper.setAutoGrowNestedPaths(true);
            beanWrapper.setPropertyValues(resultMap);
            System.out.println(newInstance);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    /**
     * 处理单纯的基础数据类型的数组
     */
    public static void test3() {

        List<ParamTypeDesc> paramTypeDescs = new ArrayList<>();


        //list 对象类型
        ParamTypeDesc paramTypeDesc1 = new ParamTypeDesc();
        paramTypeDesc1.setParamName("user");
        paramTypeDesc1.setCommonTypeName("java.util.HashMap");

        List<ParamTypeDesc> childParams1 = new ArrayList<>();
        ParamTypeDesc childParams1_1 = new ParamTypeDesc();
        childParams1_1.setParamName("userName");
        childParams1_1.setCommonTypeName("java.lang.String");
        childParams1_1.setExistsT(false);

        ParamTypeDesc childParams1_2 = new ParamTypeDesc();
        childParams1_2.setParamName("habbos");
        childParams1_2.setCommonTypeName("java.lang.String");
        childParams1_2.setArray(true);
        childParams1_2.setExistsT(false);

        childParams1.add(childParams1_1);
        childParams1.add(childParams1_2);

        paramTypeDesc1.setChildParams(childParams1);
        paramTypeDesc1.setExistsT(false);


        paramTypeDescs.add(paramTypeDesc1);


        //构建Map
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> user = new HashMap<>();
        user.put("userName", "aa");
        String[] habbos = {"xx", "td", "fda"};
        user.put("habbos", habbos);
        map.put("user", user);

        Map<String, Object> resultMap = DefaultBuildAssignmentExpression.buildAssignmentExpression(paramTypeDescs, map);
        System.out.println(resultMap);

        //1.构建Model
        ModelDesc modelDesc = DefaultModelBuildUtil.buildModelDescByParamType(paramTypeDescs);

        //2.构建代码
        String generator = DefaultModelHandler.generator(modelDesc);
        System.out.println(generator);

        //3.编译
        Class<?> compile = ClassUtil.compile(modelDesc.getMainClassName(), generator);

        //4.赋值
        try {
            Object newInstance = compile.newInstance();
            BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(newInstance);
            beanWrapper.setAutoGrowNestedPaths(true);
            beanWrapper.setPropertyValues(resultMap);
            System.out.println(newInstance);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理对象类型的数组
     */
    public static void test4() {

        List<ParamTypeDesc> paramTypeDescs = new ArrayList<>();


        //list 对象类型
        ParamTypeDesc paramTypeDesc1 = new ParamTypeDesc();
        paramTypeDesc1.setParamName("user");
        paramTypeDesc1.setCommonTypeName("java.util.HashMap");

        List<ParamTypeDesc> childParams1 = new ArrayList<>();
        ParamTypeDesc childParams1_1 = new ParamTypeDesc();
        childParams1_1.setParamName("userName");
        childParams1_1.setCommonTypeName("java.lang.String");
        childParams1_1.setExistsT(false);

        ParamTypeDesc childParams1_2 = new ParamTypeDesc();
        childParams1_2.setParamName("students");
        childParams1_2.setCommonTypeName("java.util.HashMap");
        childParams1_2.setExistsT(false);
        childParams1_2.setArray(true);

        List<ParamTypeDesc> childParamsList1_1 = new ArrayList<>();
        ParamTypeDesc childParamsList1_1_1 = new ParamTypeDesc();
        childParamsList1_1_1.setParamName("stuName");
        childParamsList1_1_1.setCommonTypeName("java.lang.String");
        childParamsList1_1_1.setExistsT(false);

        ParamTypeDesc childParamsList1_1_2 = new ParamTypeDesc();
        childParamsList1_1_2.setParamName("stuAge");
        childParamsList1_1_2.setCommonTypeName("java.lang.String");
        childParamsList1_1_2.setExistsT(false);
        childParamsList1_1.add(childParamsList1_1_1);
        childParamsList1_1.add(childParamsList1_1_2);

        childParams1_2.setChildParams(childParamsList1_1);

        childParams1.add(childParams1_1);
        childParams1.add(childParams1_2);

        paramTypeDesc1.setChildParams(childParams1);
        paramTypeDesc1.setExistsT(false);


        paramTypeDescs.add(paramTypeDesc1);


        //构建Map
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> user = new HashMap<>();
        user.put("userName", "aa");

        Map[] studentMaps = new Map[2];
        Map studentMap1 = new HashMap();
        studentMap1.put("stuName", "fdaf");
        studentMap1.put("stuAge", "15");

        Map studentMap2 = new HashMap();
        studentMap2.put("stuName", "fdfdaaf");
        studentMap2.put("stuAge", "1512");

        studentMaps[0] = studentMap1;
        studentMaps[1] = studentMap2;

        user.put("students", studentMaps);
        map.put("user", user);

        Map<String, Object> resultMap = DefaultBuildAssignmentExpression.buildAssignmentExpression(paramTypeDescs, map);
        System.out.println(resultMap);
    }


    /**
     * list集合中包含了 对象类型的数组情况
     */
    public static void test5() {
        List<ParamTypeDesc> paramTypeDescs = new ArrayList<>();
        ParamTypeDesc paramTypeDesc1 = new ParamTypeDesc();
        paramTypeDesc1.setParamName("teachers");
        paramTypeDesc1.setCommonTypeName("java.util.List");

        List<ParamTypeDesc> childParams1 = new ArrayList<>();
        ParamTypeDesc childParams1_1 = new ParamTypeDesc();
        childParams1_1.setParamName("teacherName");
        childParams1_1.setCommonTypeName("java.lang.String");
        childParams1_1.setExistsT(false);

        ParamTypeDesc childParams1_2 = new ParamTypeDesc();
        childParams1_2.setParamName("students");
        childParams1_2.setCommonTypeName("java.util.HashMap");
        childParams1_2.setExistsT(false);

        List<ParamTypeDesc> childParamsList1_1 = new ArrayList<>();
        ParamTypeDesc childParamsList1_1_1 = new ParamTypeDesc();
        childParamsList1_1_1.setParamName("stuName");
        childParamsList1_1_1.setCommonTypeName("java.lang.String");
        childParamsList1_1_1.setExistsT(false);

        ParamTypeDesc childParamsList1_1_2 = new ParamTypeDesc();
        childParamsList1_1_2.setParamName("stuAge");
        childParamsList1_1_2.setCommonTypeName("java.lang.String");
        childParamsList1_1_2.setExistsT(false);
        childParamsList1_1.add(childParamsList1_1_1);
        childParamsList1_1.add(childParamsList1_1_2);

        childParams1_2.setChildParams(childParamsList1_1);
        childParams1_2.setArray(true);

        childParams1.add(childParams1_1);
        childParams1.add(childParams1_2);

        paramTypeDesc1.setChildParams(childParams1);
        paramTypeDesc1.setExistsT(true);


        paramTypeDescs.add(paramTypeDesc1);


        //构建Map
        Map<String, Object> map = new HashMap<>();

        List<Map> teachersMap = new ArrayList<>();
        Map<String, Object> teacher1 = new HashMap<>();
        teacher1.put("teacherName", "teacher1");

        Map[] studentsMap1 = new Map[2];
        Map<String, Object> stu1 = new HashMap<>();
        stu1.put("stuName", "aa");
        stu1.put("stuAge", "18");
        studentsMap1[0] = stu1;

        Map<String, Object> stu2 = new HashMap<>();
        stu2.put("stuName", "aa");
        stu2.put("stuAge", "18");
        studentsMap1[1] = stu2;

        teacher1.put("students", studentsMap1);
        teachersMap.add(teacher1);

        Map<String, Object> teacher2 = new HashMap<>();
        teacher2.put("teacherName", "teacher2");

        Map[] studentsMap2 = new Map[2];
        Map<String, Object> stu3 = new HashMap<>();
        stu3.put("stuName", "aa");
        stu3.put("stuAge", "18");
        studentsMap2[0] = (stu3);

        Map<String, Object> stu4 = new HashMap<>();
        stu4.put("stuName", "aa");
        stu4.put("stuAge", "18");
        studentsMap2[1] = stu4;

        teacher2.put("students", studentsMap2);
        teachersMap.add(teacher2);

        map.put("teachers", teachersMap);

        Map<String, Object> resultMap = DefaultBuildAssignmentExpression.buildAssignmentExpression(paramTypeDescs, map);
        System.out.println(resultMap);

    }

}
