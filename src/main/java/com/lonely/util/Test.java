//package com.lonely.util;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.lonely.bean.ParamBean;
//import com.lonely.test.School;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author ztkj-hzb
// * @Date 2019/11/15 17:04
// * @Description
// */
//public class Test {
//
//
//    public static void main(String[] args) {
//
//        /*School school = new School();
//
//        school.setSchoolName("fdasf");
//
//        School.Clazz clazz = new School.Clazz();
//        clazz.setClazzName("fdasfsgdasff");
//        school.setClazz(clazz);
//
//
//        Map fromMap = JSON.parseObject(JSON.toJSONString(school));
//
//
//        List<ParamBean> paramBeans = new ArrayList<ParamBean>(){{
//            this.add(new ParamBean("clazz", HashMap.class,false));
//            this.add(new ParamBean("clazzName", String.class,false));
//        }};
//
//        Object expressionResultFromMap = DefaultAssignmentSubstitutionUtil.getExpressionResultFromMap(fromMap, paramBeans);
//        System.out.println(expressionResultFromMap);*/
//
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "fdadfda");
//
//        List<School.Student> students = new ArrayList<>();
//        School.Student student = new School.Student();
//        student.setStuName("fdafd");
//        student.setAge(1);
//        students.add(student);
//
//        School.Student student2 = new School.Student();
//        student2.setStuName("fdafdugufdsad");
//        student2.setAge(1);
//        students.add(student2);
//
//        map.put("users", students);
//
//
//        List<School> schools = new ArrayList<>();
//
//        School school = new School();
//        school.setSchoolName("seqeweqw");
//
//        School.Clazz clazz = new School.Clazz();
//        clazz.setClazzName("fadsljfdaljfldafads");
//        clazz.setStudents(students);
//        school.setClazz(clazz);
//
//        schools.add(school);
//        schools.add(school);
//
//        map.put("schools", schools);
//
//
//        //模拟返回jsonobject场景
//        String jsonString = JSON.toJSONString(schools);
//        System.out.println(jsonString);
//
//        JSON.parseArray(jsonString);
//
//
//
//
//        /*List<ParamBean> paramBeans = new ArrayList<ParamBean>(){{
//            this.add(new ParamBean("name",String.class,false));
//        }};*/
//
//        /*List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
//            this.add(new ParamBean("users", List.class, false));
//            this.add(new ParamBean("stuName", String.class, false));
//        }};*/
//
//        List<ParamBean> paramBeans = new ArrayList<ParamBean>() {{
//            this.add(new ParamBean("schools", List.class, false));
//            this.add(new ParamBean("clazz", HashMap.class, false));
//            this.add(new ParamBean("students", List.class, false));
//            this.add(new ParamBean("stuName", String.class, false));
//        }};
//
//        Object expressionResultFromMap = DefaultAssignmentSubstitutionUtil.getExpressionResultFromMap(currMap, paramBeans);
//        System.out.println(expressionResultFromMap);
//
//    }
//
//}
