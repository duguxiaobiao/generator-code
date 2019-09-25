package com.lonely.util;

import com.lonely.bean.AssignmentBean;
import com.lonely.bean.ParamBean;
import com.lonely.test.School;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ztkj-hzb
 * @Date 2019/9/24 10:14
 * @Description 默认的赋值替换的工具类，根据配置，将右侧的数据 赋值 给 左侧数据
 */
public class DefaultAssignmentSubstitutionUtil {


    private DefaultAssignmentSubstitutionUtil() {
    }


    /**
     * 赋值替换
     *
     * @param leftInstance
     * @param rightInstance
     * @param assignmentBeans
     */
    public static void assignmentSubstitution(Object leftInstance, Object rightInstance, List<AssignmentBean> assignmentBeans) {

        if (CollectionUtils.isEmpty(assignmentBeans)) {
            return;
        }

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(leftInstance);
        //todo 这句话很重要，取值可以不用设置，但是赋值一定需要，因为他的作用是在对象属性为null时，会帮你new一个，避免了空指针异常
        beanWrapper.setAutoGrowNestedPaths(true);

        for (AssignmentBean assignmentBean : assignmentBeans) {

            //判断当前表达式是否包含了集合
            long count = assignmentBean.getLefetParamBeans().stream().filter(paramBean -> ClassUtil.isListTypeClass(paramBean.getClazz()))
                    .count();
            if (count == 0) {
                //正常属性或对象属性处理
                //1.获取对应的右侧的属性值
                Object result = getExpressionResults(rightInstance, assignmentBean.getRightParamBeans());
                //2.根据左侧属性关系构建表达式
                //String express = buildCascadeExpression(assignmentBean.getLefetParamBeans());
                String express = getExpression(assignmentBean.getLefetParamBeans());
                //3.设置属性
                beanWrapper.setPropertyValue(express, result);
            } else if (count == 1) {
                //存在集合
                //1.获取对应的右侧的属性值
                Object result = getExpressionResults(rightInstance, assignmentBean.getRightParamBeans());
                //判断右侧属性中是否存在集合
                long rCount = assignmentBean.getRightParamBeans().stream().filter(paramBean -> ClassUtil.isListTypeClass(paramBean.getClazz()))
                        .count();
                List list = new ArrayList();
                if (rCount == 0) {
                    //右侧属性不包括集合，需要构建一个集合
                    list.add(result);
                } else {
                    //右侧是一个集合
                    list.addAll((List) result);
                }
                //获取初级表达式
                String express = getExpression(assignmentBean.getLefetParamBeans());
                //替换表达式赋值
                String listReplaceStr = MessageFormat.format("[L{0}]", 0);
                for (int i = 0; i < list.size(); i++) {
                    String newExpress = express.replace(StringUtil.removeLeftAndRightMiddleBrackets(listReplaceStr), i + "");
                    beanWrapper.setPropertyValue(newExpress, list.get(i));
                }
            } else {
                throw new RuntimeException("不支持嵌套list,请检查");
            }


        }

    }

    /**
     * 获取指定表达式的结果，例如有一个临时变量  String a = 响应结果.用户.用户名,  需要返回 响应结果.用户.用户名 的结果操作
     *
     * @param objInstance
     * @param paramBeans
     * @return
     */
    public static Object getExpressionResults(Object objInstance, List<ParamBean> paramBeans) {

        //循环构建表达式
        if (CollectionUtils.isEmpty(paramBeans)) {
            return null;
        }


        //1.根据参数构建初级表达式，比如构建的表达式是： user[L0].student[L1].name
        String expression = getExpression(paramBeans);

        //2.根据值替换表达式
        if (StringUtils.isEmpty(expression)) {
            return null;
        }
        Set<String> expressSet = new HashSet<>();
        expressSet.add(expression);
        Set<String> replaceExpression = replaceExpression(expressSet, 0, objInstance);

        //System.out.println(replaceExpression);

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(objInstance);

        //判断是单条数据还是多条数据
        if (CollectionUtils.isEmpty(replaceExpression)) {
            throw new RuntimeException("玩个鸡儿，撒都米有了");
        }

        if (replaceExpression.size() == 1) {
            //只有一条，不要将结果累计，直接返回
            String resultExpressStr = replaceExpression.stream().findFirst().orElseThrow(()->new RuntimeException("没有表达式，无法处理"));
            Object propertyValue = beanWrapper.getPropertyValue(resultExpressStr);
            //System.out.println(propertyValue);
            return propertyValue;
        } else {
            //多条，需要将结果累计
            List<Object> result = new ArrayList<>();
            for (String resultExpressStr : replaceExpression) {
                Object propertyValue = beanWrapper.getPropertyValue(resultExpressStr);
                //System.out.println(propertyValue);
                result.add(propertyValue);
            }

            //这里需要注意，如果最终需要返回的是一个数组，则需要将list转数组
            return result;
        }

    }


    private static String buildCascadeExpression(List<ParamBean> paramBeans) {
        if (CollectionUtils.isEmpty(paramBeans)) {
            return StringUtils.EMPTY;
        }
        return paramBeans.stream().map(ParamBean::getParamName).collect(Collectors.joining("."));
    }


    /*private static void replaceExpression(String expression, Object objInstance, Set<String> expressSet, int index) {
        //int index = 0;
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(objInstance);

        while (StringUtil.regularMatchingListOrArrayFormat(expression)) {
            //判断是集合还是数组
            String listReplaceStr = MessageFormat.format("[L{0}]", index);
            String arrayReplaceStr = MessageFormat.format("[A{0}]", index);

            //判断当前最近处理的是 list还是数组
            if (expression.indexOf(listReplaceStr) > expression.indexOf(arrayReplaceStr)) {
                //先处理list
                String subExpress = StringUtil.subStringListFormat(expression, listReplaceStr);
                List list = (List) beanWrapper.getPropertyValue(subExpress);
                if (CollectionUtils.isEmpty(list)) {
                    return;
                }
                Set<String> expreSet = new HashSet<>();
                for (int i = 0; i < list.size(); i++) {
                    //int currIndex = index+1;
                    String newExpress = expression.replace(StringUtil.removeLeftAndRightMiddleBrackets(listReplaceStr), i + "");
                    //递归处理
                    //replaceExpression(newExpress, objInstance, expressSet, ++index);
                    expreSet.add(newExpress);
                }

                for (String express : expreSet) {
                    replaceExpression(express, objInstance, expressSet, ++index);
                }

                //return;
            } else {
                //处理数组

            }
        }
        expressSet.add(expression);

    }*/


    /**
     * 将表达式构建成具体的赋值表达式，例如  student[L0].studentName -> student[0].studentName 和 student[1].studentName
     *
     * @param expressSet
     * @param currIndex
     * @param objInstance
     * @return
     */
    private static Set<String> replaceExpression(Set<String> expressSet, int currIndex, Object objInstance) {

        if (CollectionUtils.isEmpty(expressSet)) {
            return new HashSet<>();
        }

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(objInstance);

        Set<String> resultSet = new HashSet<>();

        for (String express : expressSet) {
            if (StringUtil.regularMatchingListOrArrayFormat(express)) {
                //判断是集合还是数组
                String listReplaceStr = MessageFormat.format("[L{0}]", currIndex);
                String arrayReplaceStr = MessageFormat.format("[A{0}]", currIndex);

                //判断当前最近处理的是 list还是数组
                if (express.indexOf(listReplaceStr) > express.indexOf(arrayReplaceStr)) {
                    //先处理list
                    String subExpress = StringUtil.subStringListFormat(express, listReplaceStr);
                    List list = (List) beanWrapper.getPropertyValue(subExpress);
                    if (CollectionUtils.isEmpty(list)) {
                        return new HashSet<>();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        String newExpress = express.replace(StringUtil.removeLeftAndRightMiddleBrackets(listReplaceStr), i + "");
                        resultSet.add(newExpress);
                    }
                } else {
                    //处理数组

                }
            }

        }

        if (CollectionUtils.isEmpty(resultSet)) {
            return expressSet;
        }

        return replaceExpression(resultSet, ++currIndex, objInstance);
    }


    /**
     * 获取表达式结果
     *
     * @param paramBeans
     * @return
     */
    public static String getExpression(List<ParamBean> paramBeans) {

        //循环构建表达式
        if (CollectionUtils.isEmpty(paramBeans)) {
            return StringUtils.EMPTY;
        }

        String prefix = StringUtils.EMPTY;
        int index = 0;
        for (int i = 0; i < paramBeans.size(); i++) {
            ParamBean paramBean = paramBeans.get(i);
            //判断是否是集合类型
            if (ClassUtil.isListTypeClass(paramBean.getClazz())) {
                //集合类型，先获取目前为止的集合的值
                prefix = StringUtils.isEmpty(prefix) ? MessageFormat.format("{0}[L{1}]", paramBean.getParamName(), index++) :
                        MessageFormat.format("{0}.{1}[L{2}]", prefix, paramBean.getParamName(), index++);
            } else if (ClassUtil.isArrayTypeClass(paramBean.getClazz())) {
                //数组类型
                prefix = StringUtils.isEmpty(prefix) ? paramBean.getParamName() : MessageFormat.format("{0}.{1}[A{2}]", prefix, paramBean.getParamName(), index++);
            } else {
                //非集合类型
                prefix = StringUtils.isEmpty(prefix) ? paramBean.getParamName() : MessageFormat.format("{0}.{1}", prefix, paramBean.getParamName());
            }
        }

        return prefix;
    }


    public static void main(String[] args) {
        School testBean = new School();
        testBean.setSchoolName("lonely");
        testBean.setClazz(new School.Clazz("终极一班", new ArrayList<School.Student>() {{
            this.add(new School.Student("aa", 15));
            this.add(new School.Student("bb", 16));
        }}));

        List<School.Teacher> teachers = new ArrayList<>();
        School.Teacher teacher1 = new School.Teacher();
        teacher1.setTeacherName("tea1");
        teacher1.setStudents(new ArrayList<School.Student>() {{
            this.add(new School.Student("aa", 15));
            this.add(new School.Student("bb", 16));
        }});

        School.Teacher teacher2 = new School.Teacher();
        teacher2.setTeacherName("tea2");
        teacher2.setStudents(new ArrayList<School.Student>() {{
            this.add(new School.Student("aafdas", 15));
            this.add(new School.Student("bbfda", 16));
        }});

        teachers.add(teacher1);
        teachers.add(teacher2);
        testBean.setTeachers(teachers);


        List<ParamBean> paramBeans = new ArrayList<>();

        //1.基础类型
        //ParamBean nameParam = new ParamBean("name",String.class);
        //paramBeans.add(nameParam);

        //2.级联对象类型
        //ParamBean clazzParam = new ParamBean("clazz", School.Clazz.class);
        //ParamBean clazzNameParam = new ParamBean("clazzName",String.class);
        //paramBeans.add(clazzParam);
        //paramBeans.add(clazzNameParam);

        //3.集合类型
        //ParamBean clazzParam = new ParamBean("clazz", School.Clazz.class);
        //ParamBean studentParam = new ParamBean("students", List.class);
        //ParamBean studentNameParam = new ParamBean("stuName", String.class);
        //paramBeans.add(clazzParam);
        //paramBeans.add(studentParam);
        //paramBeans.add(studentNameParam);

        //4.集合嵌套
        //ParamBean clazzParam = new ParamBean("clazz", School.Clazz.class);
        ParamBean teacherParam = new ParamBean("teachers", List.class);
        ParamBean studentParam = new ParamBean("students", List.class);
        ParamBean studentNameParam = new ParamBean("age", Integer.class);
        paramBeans.add(teacherParam);
        paramBeans.add(studentParam);
        paramBeans.add(studentNameParam);


        //System.out.println(getExpression(paramBeans));


        Object expressionResults = getExpressionResults(testBean, paramBeans);
        System.out.println(expressionResults);


        //Object expressionResults = getExpressionResults(testBean, paramBeans);
        //System.out.println(expressionResults);


    }

}
