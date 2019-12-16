package com.lonely.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lonely.bean.AssignmentBean;
import com.lonely.bean.ParamBean;
import com.lonely.constants.DataTypeConstant;
import com.lonely.test.School;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztkj-hzb
 * @Date 2019/9/24 10:14
 * @Description 专门处理第三方接口返回的处理
 */
public class DefaultThirdPartyDataProcessingUtil {


    private DefaultThirdPartyDataProcessingUtil() {
    }


    /**
     * 从map中获取指定表达式结果 例如 map:{"userName":"aa"}  中取出 userName的值 返回,可支持嵌套
     *
     * @param fromMap
     * @param paramBeans
     * @return
     */
    public static Object getExpressionResult(Map fromMap, List<ParamBean> paramBeans) {

        if (fromMap == null || fromMap.isEmpty()) {
            return null;
        }

        if (CollectionUtils.isEmpty(paramBeans)) {
            throw new RuntimeException("没有表达式，玩jb");
        }

        return doGetExpressionResultForMapHandler(fromMap, paramBeans);

    }


    /**
     * 从map中获取数据处理过程
     *
     * @param fromMap
     * @param paramBeans
     * @return
     */
    @SuppressWarnings("all")
    private static Object doGetExpressionResultForMapHandler(Map fromMap, List<ParamBean> paramBeans) {


        if (CollectionUtils.isEmpty(paramBeans)) {
            return null;
        }

        //针对于list场景，从第一个配置开始
        ParamBean paramBean = paramBeans.get(0);
        if (!fromMap.containsKey(paramBean.getParamName())) {
            return null;
        }

        //List results = new ArrayList();
        Object result = fromMap.get(paramBean.getParamName());
        //判断当前数据类型是否为集合
        if (result == null) {
            return null;
        }

        //根据提供的类型判断
        //判断类型
        Class type = paramBean.getClazz();
        if (type.isPrimitive() || ClassUtil.isWrapClass(type) || DataTypeConstant.TYPE_STRING.equalsIgnoreCase(type.getSimpleName())) {
            //基础数据类型
            return result;
        } else if (DataTypeConstant.TYPE_OBJECT.equalsIgnoreCase(type.getSimpleName())) {
            //对象类型处理,第三方的对象类型充当JSONObject对象处理
            List<ParamBean> currParamBeans = new ArrayList<>(paramBeans.subList(1, paramBeans.size()));

            Object expressionResults;
            if (result != null && result instanceof JSONObject) {
                //当前值为jsonobject类型，则当map处理
                expressionResults = doGetExpressionResultForMapHandler((Map) result, currParamBeans);
            } else {
                //不是jsonobject，则使用通过工具类处理
                expressionResults = DefaultAssignmentSubstitutionUtil.getExpressionResults(result, currParamBeans);
            }
            return expressionResults;

        } else if (DataTypeConstant.TYPE_LIST.equalsIgnoreCase(type.getSimpleName())) {
            //list类型处理
            List<ParamBean> currParamBeans = new ArrayList<>(paramBeans.subList(1, paramBeans.size()));
            return doGetExpressionResultForListHandler((List) result, currParamBeans);
        } else if (DataTypeConstant.TYPE_MAP.equalsIgnoreCase(type.getSimpleName())) {
            //map类型处理
            List<ParamBean> currParamBeans = new ArrayList<>(paramBeans.subList(1, paramBeans.size()));
            Object currResult = doGetExpressionResultForMapHandler((Map) result, currParamBeans);

            return currResult;
        } else {
            throw new RuntimeException(MessageFormat.format("不支持其他类型", result.getClass().getName()));
        }

    }

    /**
     * 将返回的结果添加到 当前的结果集中,如果当前结果结果为空，则设置一个空集合进去
     *
     * @param results
     * @param currResult
     * @param currParamBeans
     */
    private static void addResultConvert(List<Object> results, Object currResult, List<ParamBean> currParamBeans) {
        /*long listCount = currParamBeans.stream().filter(currParam -> ClassUtil.isListTypeClass(currParam.getClazz()) || currParam.isArray())
                .count();*/
        if (existsListByParamBeans(currParamBeans)) {
            results.addAll(currResult == null ? new ArrayList() {{
                this.add(null);
            }} : (List) currResult);
        } else {
            results.add(currResult);
        }
    }


    /**
     * 针对list结果从list中获取数据处理过程
     *
     * @param results
     * @param paramBeans
     * @return
     */
    private static List doGetExpressionResultForListHandler(List results, List<ParamBean> paramBeans) {

        List currResults = new ArrayList();

        if (CollectionUtils.isEmpty(results)) {
            return currResults;
        }

        if (CollectionUtils.isEmpty(paramBeans)) {
            //这种属于 List<String> -->这种不是List<Map>这种格式的
            currResults.addAll(results);
            return currResults;
        }


        //当前第一个属性的参数配置
        ParamBean paramBean = paramBeans.get(0);
        Class type = paramBean.getClazz();


        for (Object result : results) {
            //Class type = result.getClass();
            if (type.isPrimitive() || ClassUtil.isWrapClass(type) || DataTypeConstant.TYPE_STRING.equalsIgnoreCase(type.getSimpleName())) {
                //基础数据类型

                //如果当前值也是基础数据类型，则直接返回
                Class resultType = result.getClass();
                if (resultType.isPrimitive() || ClassUtil.isWrapClass(resultType) || DataTypeConstant.TYPE_STRING.equalsIgnoreCase(resultType.getSimpleName())) {
                    currResults.add(result);
                } else if (result instanceof Map) {
                    //当前结果是map类型，但是类型不是map类型的情况
                    currResults.add(((Map) result).get(paramBean.getParamName()));
                } else {
                    //
                    currResults.add(DefaultAssignmentSubstitutionUtil.getExpressionResults(result, paramBeans));
                }

            } else if (DataTypeConstant.TYPE_MAP.equalsIgnoreCase(type.getSimpleName())) {
                //map处理
                addResultConvert(currResults, doGetExpressionResultForMapHandler((Map) result, paramBeans), paramBeans);
                //currResults.add(doGetExpressionResultForMapHandler((Map) result, paramBeans));
            } else if (DataTypeConstant.TYPE_LIST.equalsIgnoreCase(type.getSimpleName())) {
                //list类型处理

                //获取当前的层级的结果
                if (result instanceof Map) {
                    Object currResultList = ((Map) result).get(paramBean.getParamName());
                    List<ParamBean> currParams = new ArrayList<>(paramBeans.subList(1, paramBeans.size()));
                    currResults.addAll(doGetExpressionResultForListHandler((List) currResultList, currParams));
                } else if (result instanceof List) {
                    currResults.addAll(doGetExpressionResultForListHandler((List) result, paramBeans));
                } else {
                    throw new RuntimeException("针对ParamBean为List场景，其结果必须是Map或者是List");
                }

            } else {
                //object类型处理
                Object expressionResults = DefaultAssignmentSubstitutionUtil.getExpressionResults(result, paramBeans);
                addResultConvert(currResults, expressionResults, paramBeans);
            }
        }


        return currResults;
    }


    /**
     * 给指定对象根据赋值配置 赋予需要的数据值
     *
     * @param object         待赋值的对象
     * @param dataSource     数据来源，即需要将该数据放入到待赋值的对象中
     * @param assignmentBean 赋值表达式配置
     */
    public static void setValue(Object object, Object dataSource, AssignmentBean assignmentBean) {

        if (assignmentBean == null) {
            return;
        }

        List<ParamBean> rightParamBeans = assignmentBean.getRightParamBeans();
        if (CollectionUtils.isEmpty(rightParamBeans)) {
            return;
        }

        //1.根据结构，获取右侧返回的值
        Object expressionResult = getExpressionResult((Map) dataSource, rightParamBeans);

        //2.根据左侧属性关系构建表达式
        String express = DefaultAssignmentSubstitutionUtil.getExpression(assignmentBean.getLefetParamBeans());

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        beanWrapper.setAutoGrowNestedPaths(true);

        //是否存在集合
        if (existsListByParamBeans(rightParamBeans)) {
            if (!(expressionResult instanceof List)) {
                //数据与类型不匹配
                throw new RuntimeException("数据与类型结构不匹配");
            }
            List list = new ArrayList((List) expressionResult);

            //判断先处理集合还是数组
            String listReplaceStr = MessageFormat.format("[L{0}]", 0);
            String arrayReplaceStr = MessageFormat.format("[A{0}]", 0);

            //判断是处理的是 list还是数组，这里不支持嵌套,所以只会存在一种
            String finalHandlerReplaceStr = express.contains(listReplaceStr) ? listReplaceStr : arrayReplaceStr;

            //替换表达式赋值
            for (int i = 0; i < list.size(); i++) {
                String newExpress = express.replace(StringUtil.removeLeftAndRightMiddleBrackets(finalHandlerReplaceStr), i + "");
                beanWrapper.setPropertyValue(newExpress, list.get(i));
            }

        } else {
            //不存在集合,直接设值
            beanWrapper.setPropertyValue(express, expressionResult);
        }

    }

    /**
     * 判断是否存在list类型
     *
     * @param paramBeans
     * @return
     */
    private static boolean existsListByParamBeans(List<ParamBean> paramBeans) {
        long listCount = paramBeans.stream().filter(currParam -> ClassUtil.isListTypeClass(currParam.getClazz()) || currParam.isArray())
                .count();
        return listCount > 0;
    }


    public static void main(String[] args) {

        //1.普通属性测试
        /*Map map = new HashMap();
        map.put("name", "fda");

        List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
            this.add(new ParamBean("name", String.class, false));
        }};
        System.out.println(getExpressionResult(map, paramBeans));*/

        //2.普通对象中设置普通属性
        /*Map map = new HashMap();
        School.Student student = new School.Student();
        student.setStuName("fdafs");
        map.put("stu", student);

        List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
            this.add(new ParamBean("stu", Object.class, false));
            this.add(new ParamBean("stuName", String.class, false));
        }};
        System.out.println(getExpressionResult(map, paramBeans));*/

        //3.从JSONObject对象中获取普通属性
        Map map = new HashMap();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "fda");
        map.put("stuInfo", jsonObject);
        List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
            this.add(new ParamBean("stuInfo", Map.class, false));
            this.add(new ParamBean("name", String.class, false));
        }};
        System.out.println(getExpressionResult(map, paramBeans));


        //4.处理list普通类型数据
        /*Map map = new HashMap();
        List<String> names = new ArrayList<String>() {{
            this.add("fdaf");
            this.add("fdaf34");
            this.add("fdaf213");
        }};
        map.put("names", names);
        List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
            this.add(new ParamBean("names", List.class, false));
        }};
        System.out.println(getExpressionResult(map, paramBeans));*/

        //5.处理list对象类型数据
        /*Map map = new HashMap();
        List<School.Student> students = new ArrayList<>();
        School.Student student1 = new School.Student();
        student1.setStuName("stu1");
        School.Student student2 = new School.Student();
        student2.setStuName("stu2");
        students.add(student1);
        students.add(student2);
        map.put("stus",students);
        List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
            this.add(new ParamBean("stus", List.class, false));
            this.add(new ParamBean("stuName", String.class, false));
        }};
        System.out.println(DefaultThirdPartyDataProcessingUtil.getExpressionResult(map, paramBeans));*/

        //6. 处理List<Map>格式
        /*Map map = new HashMap();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", "fdafd");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name", "fsadfadafd");
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
        map.put("names", jsonArray);
        List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
            this.add(new ParamBean("names", List.class, false));
            this.add(new ParamBean("name", String.class, false));
        }};
        System.out.println(getExpressionResult(map, paramBeans));*/


        //7.处理list中嵌套list格式
        /*Map map = new HashMap();
        List<School> schools = new ArrayList<>();
        School school1 = new School();

        List<School.Student> students = new ArrayList<>();
        School.Student student1 = new School.Student();
        student1.setStuName("stu1");
        School.Student student2 = new School.Student();
        student2.setStuName("stu2");
        students.add(student1);
        students.add(student2);

        School.Clazz clazz = new School.Clazz();
        clazz.setStudents(students);
        school1.setClazz(clazz);

        schools.add(school1);
        schools.add(school1);
        schools.add(school1);

        List jsonArray = JSON.parseArray(JSON.toJSONString(schools));
        map.put("schools", jsonArray);

        List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
            this.add(new ParamBean("schools", List.class, false));
            this.add(new ParamBean("clazz", Map.class, false));
            this.add(new ParamBean("students", List.class, false));
            this.add(new ParamBean("stuName", String.class, false));
        }};
        System.out.println(getExpressionResult(map, paramBeans));*/

        //8.三层list嵌套
        /*Map map = new HashMap();
        List<School> schools = new ArrayList<>();
        School school1 = new School();

        List<School.Student> students = new ArrayList<>();
        School.Student student1 = new School.Student();
        student1.setStuName("stu1");
        School.Student student2 = new School.Student();
        student2.setStuName("stu2");
        students.add(student1);
        students.add(student2);

        List<School.Teacher> teachers = new ArrayList<>();
        School.Teacher teacher = new School.Teacher();
        teacher.setStudents(students);
        teachers.add(teacher);
        teachers.add(teacher);
        teachers.add(teacher);

        school1.setTeachers(teachers);

        schools.add(school1);
        schools.add(school1);
        schools.add(school1);

        List jsonArray = JSON.parseArray(JSON.toJSONString(schools));
        map.put("schools", jsonArray);

        List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
            this.add(new ParamBean("schools", List.class, false));
            this.add(new ParamBean("teachers", List.class, false));
            this.add(new ParamBean("students", List.class, false));
            this.add(new ParamBean("stuName", String.class, false));
        }};
        System.out.println(getExpressionResult(map, paramBeans));*/


        //测试赋值
        /*Map map = new HashMap();
        List<School> schools = new ArrayList<>();
        School school1 = new School();

        List<School.Student> students = new ArrayList<>();
        School.Student student1 = new School.Student();
        student1.setStuName("stu1");
        School.Student student2 = new School.Student();
        student2.setStuName("stu2");
        students.add(student1);
        students.add(student2);

        List<School.Teacher> teachers = new ArrayList<>();
        School.Teacher teacher = new School.Teacher();
        teacher.setStudents(students);
        teachers.add(teacher);
        teachers.add(teacher);
        teachers.add(teacher);

        school1.setTeachers(teachers);

        schools.add(school1);
        schools.add(school1);
        schools.add(school1);

        List jsonArray = JSON.parseArray(JSON.toJSONString(schools));
        map.put("schools", jsonArray);

        List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
            this.add(new ParamBean("schools", List.class, false));
            this.add(new ParamBean("teachers", List.class, false));
            this.add(new ParamBean("students", List.class, false));
            this.add(new ParamBean("stuName", String.class, false));
        }};
        //System.out.println(getExpressionResult(map, paramBeans));


        School.Clazz leftClazz = new School.Clazz();
        List<ParamBean> leftParams = new ArrayList<ParamBean>() {{
            this.add(new ParamBean("students", List.class, false));
            this.add(new ParamBean("stuName", String.class, false));
        }};

        AssignmentBean assignmentBean = new AssignmentBean();
        assignmentBean.setLefetParamBeans(leftParams);
        assignmentBean.setRightParamBeans(paramBeans);

        setValue(leftClazz,map,assignmentBean);

        System.out.println(leftClazz);*/

    }


}
