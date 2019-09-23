package com.lonely.generator;

import com.lonely.bean.ModelDesc;
import com.lonely.bean.ParamTypeDesc;
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
 * @Date 2019/9/19 15:24
 * @Description
 */
public class Test {


    public static void main(String[] args) throws Exception {
        //testInner();

        testBuildAssignment();
    }


    public static void testBuildAssignment() throws IllegalAccessException, InstantiationException {

        //参数配置集合
        List<ParamTypeDesc> paramTypeDescs = buildParamTypeDescBeans();

        //构建结果集map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name", "dugu");
        resultMap.put("age", 25);

        Map clazzMap = new HashMap();
        clazzMap.put("className", "终极一班");

        Map studentMap = new HashMap();
        studentMap.put("studentName", "stu_lonely");
        studentMap.put("studentAge", 26);

        clazzMap.put("student", studentMap);
        resultMap.put("clazz", clazzMap);

        Map schoolMap = new HashMap();
        schoolMap.put("schoolName", "三国学院");
        resultMap.put("school", schoolMap);

        List list1 = new ArrayList();
        list1.add(14);
        list1.add("lonely");
        resultMap.put("familys", list1);

        List<String> list2 = new ArrayList<>();
        list2.add("dafd");
        list2.add("werqrw");
        resultMap.put("familysGene", list2);

        List<Map<String, Object>> list3 = new ArrayList<>();
        Map<String, Object> list3_1_map = new HashMap<>();
        list3_1_map.put("fimilyName", "fdasfda");
        list3_1_map.put("fimilyBobby", "bfdsgtwt");

        Map<String, Object> list3_2_map = new HashMap<>();
        list3_2_map.put("fimilyName", "fdasf324da");
        list3_2_map.put("fimilyBobby", "bfdsrwetgtwt");
        list3.add(list3_1_map);
        list3.add(list3_2_map);
        resultMap.put("familysStuGenes", list3);

        Map<String, Object> returnMap = DefaultBuildAssignmentExpression.buildAssignmentExpression(paramTypeDescs, resultMap);
        System.out.println(returnMap);


        //1.构建Model
        ModelDesc modelDesc = DefaultModelBuildUtil.buildModelDescByParamType(paramTypeDescs);

        //2.构建代码
        String generator = DefaultModelHandler.generator(modelDesc);
        System.out.println(generator);

        //3.编译
        Class<?> compile = ClassUtil.compile(modelDesc.getMainClassName(), generator);
        System.out.println(compile.getTypeName());

        //4.组装参数
        //上面已组装

        //5.赋值
        Object newInstance = compile.newInstance();
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(newInstance);
        beanWrapper.setAutoGrowNestedPaths(true);

        beanWrapper.setPropertyValues(returnMap);

        //6.查看赋值情况
        System.out.println(newInstance);

    }


    public static void testInner() throws Exception {

        List<ParamTypeDesc> paramTypeDescs = buildParamTypeDescBeans();

        //1.构建Model
        ModelDesc modelDesc = DefaultModelBuildUtil.buildModelDescByParamType(paramTypeDescs);

        //2.构建代码
        String generator = DefaultModelHandler.generator(modelDesc);
        System.out.println(generator);

        /*if(StringUtils.isNotBlank(generator)){
            return;
        }*/

        //3.编译
        Class<?> compile = ClassUtil.compile(modelDesc.getMainClassName(), generator);
        System.out.println(compile.getTypeName());

        //4.构建数据结构
        Map map = new HashMap();
        map.put("name", "dugu");
        map.put("age", 25);

        Map clazzMap = new HashMap();
        clazzMap.put("className", "终极一班");

        Map studentMap = new HashMap();
        studentMap.put("studentName", "stu_lonely");
        studentMap.put("studentAge", 26);

        clazzMap.put("student", studentMap);
        map.put("clazz", clazzMap);

        Map schoolMap = new HashMap();
        schoolMap.put("schoolName", "三国学院");
        map.put("school", schoolMap);


        //5.赋值
        Object newInstance = compile.newInstance();
        //BeanUtils.copyProperties(newInstance, map);

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(newInstance);
        beanWrapper.setAutoGrowNestedPaths(true);

        beanWrapper.setPropertyValue("name", "dugu");
        beanWrapper.setPropertyValue("age", 17);

        beanWrapper.setPropertyValue("school.schoolName", "三国学院");

        beanWrapper.setPropertyValue("clazz.className", "终极一班");
        beanWrapper.setPropertyValue("clazz.student.studentName", "stu_lonely");
        beanWrapper.setPropertyValue("clazz.student.studentAge", "26");


        //beanWrapper.setPropertyValues("name","dugu");

        beanWrapper.setPropertyValue("familys[0]", "26");
        beanWrapper.setPropertyValue("familys[1]", 25);


        beanWrapper.setPropertyValue("familysGene[0]", "hello");
        beanWrapper.setPropertyValue("familysGene[1]", "world");

        beanWrapper.setPropertyValue("familysStuGenes[0].fimilyName", "lfda");
        beanWrapper.setPropertyValue("familysStuGenes[0].fimilyBobby", "cxbvxcb");
        beanWrapper.setPropertyValue("familysStuGenes[1].fimilyName", "lfda132");
        beanWrapper.setPropertyValue("familysStuGenes[1].fimilyBobby", "cxbvxcb1234");

        //beanWrapper.setPropertyValue("familysStuSetGenes[0].fimilyName", "lfda");
        //beanWrapper.setPropertyValue("familysStuSetGenes[0].fimilyBobby", "cxbvxcb");
        //beanWrapper.setPropertyValue("familysStuSetGenes[1].fimilyName", "lfda132");
        //beanWrapper.setPropertyValue("familysStuSetGenes[1].fimilyBobby", "cxbvxcb1234");


        System.out.println(beanWrapper.getPropertyValue("school.schoolName"));


        //6. 输出
        System.out.println(ClassUtil.getToString(newInstance));

    }


    private static List<ParamTypeDesc> buildParamTypeDescBeans() {
        List<ParamTypeDesc> paramTypeDescs = new ArrayList<>();

        ParamTypeDesc paramTypeDesc1 = new ParamTypeDesc();
        paramTypeDesc1.setParamName("name");
        paramTypeDesc1.setCommonTypeName("java.lang.String");
        paramTypeDesc1.setExistsT(false);

        ParamTypeDesc paramTypeDesc2 = new ParamTypeDesc();
        paramTypeDesc2.setParamName("age");
        paramTypeDesc2.setCommonTypeName("java.lang.Integer");
        paramTypeDesc2.setExistsT(false);

        //内部map 在加map，
        ParamTypeDesc paramTypeDesc3 = new ParamTypeDesc();
        paramTypeDesc3.setParamName("clazz");
        paramTypeDesc3.setCommonTypeName("java.util.HashMap");
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

        paramTypeDesc3.setChildParams(childParams);
        paramTypeDesc3.setExistsT(false);


        //最外层map
        ParamTypeDesc paramTypeDesc4 = new ParamTypeDesc();
        paramTypeDesc4.setParamName("school");
        paramTypeDesc4.setCommonTypeName("java.util.HashMap");
        List<ParamTypeDesc> childParams3 = new ArrayList<>();
        ParamTypeDesc childParams3_1 = new ParamTypeDesc();
        childParams3_1.setParamName("schoolName");
        childParams3_1.setCommonTypeName("java.lang.String");
        childParams3_1.setExistsT(false);
        childParams3.add(childParams3_1);
        paramTypeDesc4.setChildParams(childParams3);
        paramTypeDesc4.setExistsT(false);

        //来一个list类型的
        ParamTypeDesc paramTypeDesc5 = new ParamTypeDesc();
        paramTypeDesc5.setParamName("familys");
        paramTypeDesc5.setCommonTypeName("java.util.List");
        paramTypeDesc5.setExistsT(false);
        //paramTypeDesc5.setGenericityType("java.lang.String");

        //list基础类型泛型
        ParamTypeDesc paramTypeDesc6 = new ParamTypeDesc();
        paramTypeDesc6.setParamName("familysGene");
        paramTypeDesc6.setCommonTypeName("java.util.List");
        paramTypeDesc6.setExistsT(true);
        paramTypeDesc6.setGenericityType("java.lang.String");

        //list 对象类型
        ParamTypeDesc paramTypeDesc7 = new ParamTypeDesc();
        paramTypeDesc7.setParamName("familysStuGenes");
        paramTypeDesc7.setCommonTypeName("java.util.List");

        List<ParamTypeDesc> childParams4 = new ArrayList<>();
        ParamTypeDesc childParams4_1 = new ParamTypeDesc();
        childParams4_1.setParamName("fimilyName");
        childParams4_1.setCommonTypeName("java.lang.String");
        childParams4_1.setExistsT(false);

        ParamTypeDesc childParams4_2 = new ParamTypeDesc();
        childParams4_2.setParamName("fimilyBobby");
        childParams4_2.setCommonTypeName("java.lang.String");
        childParams4_2.setExistsT(false);

        childParams4.add(childParams4_1);
        childParams4.add(childParams4_2);

        paramTypeDesc7.setChildParams(childParams4);
        paramTypeDesc7.setExistsT(true);

        //set 无泛型
        ParamTypeDesc paramTypeDesc8 = new ParamTypeDesc();
        paramTypeDesc8.setParamName("familySetNone");
        paramTypeDesc8.setCommonTypeName("java.util.Set");
        paramTypeDesc8.setExistsT(false);

        //set 普通类型
        ParamTypeDesc paramTypeDesc9 = new ParamTypeDesc();
        paramTypeDesc9.setParamName("familySetGene");
        paramTypeDesc9.setCommonTypeName("java.util.Set");
        paramTypeDesc9.setExistsT(true);
        paramTypeDesc9.setGenericityType("java.lang.Integer");

        //set 对象类型
        ParamTypeDesc paramTypeDesc10 = new ParamTypeDesc();
        paramTypeDesc10.setParamName("familysStuSetGenes");
        paramTypeDesc10.setCommonTypeName("java.util.Set");

        List<ParamTypeDesc> childParams5 = new ArrayList<>();
        ParamTypeDesc childParams5_1 = new ParamTypeDesc();
        childParams5_1.setParamName("fimilyName");
        childParams5_1.setCommonTypeName("java.lang.String");
        childParams5_1.setExistsT(false);

        ParamTypeDesc childParams5_2 = new ParamTypeDesc();
        childParams5_2.setParamName("fimilyBobby");
        childParams5_2.setCommonTypeName("java.lang.String");
        childParams5_2.setExistsT(false);

        childParams5.add(childParams5_1);
        childParams5.add(childParams5_2);

        paramTypeDesc10.setChildParams(childParams5);
        paramTypeDesc10.setExistsT(true);


        paramTypeDescs.add(paramTypeDesc1);
        paramTypeDescs.add(paramTypeDesc2);
        paramTypeDescs.add(paramTypeDesc3);
        paramTypeDescs.add(paramTypeDesc4);
        paramTypeDescs.add(paramTypeDesc5);
        paramTypeDescs.add(paramTypeDesc6);
        paramTypeDescs.add(paramTypeDesc7);
        paramTypeDescs.add(paramTypeDesc8);
        paramTypeDescs.add(paramTypeDesc9);
        paramTypeDescs.add(paramTypeDesc10);

        return paramTypeDescs;
    }

}
