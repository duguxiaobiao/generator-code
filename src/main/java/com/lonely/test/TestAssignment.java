package com.lonely.test;

import com.lonely.bean.AssignmentBean;
import com.lonely.bean.ParamBean;
import com.lonely.util.DefaultAssignmentSubstitutionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztkj-hzb
 * @Date 2019/9/24 15:24
 * @Description 测试赋值
 */
public class TestAssignment {

    public static void main(String[] args) {

        testAssignmentSubstitution();

        //testGetExpressionResults();

    }


    /**
     * 测试assignmentSubstitution方法
     */
    public static void testAssignmentSubstitution() {


        OutputObject outputObject = new OutputObject();

        School school = getTestSchool();

        /*List<AssignmentBean> assignmentBeans = new ArrayList<>();
        AssignmentBean assignmentBean = new AssignmentBean();
        assignmentBean.setLefetParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("outputObjectObj", OutputObject.OutputObjectObj.class));
            this.add(new ParamBean("outputObjectName", String.class));
        }});
        assignmentBean.setRightParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("clazz", School.Clazz.class));
            this.add(new ParamBean("clazzName", String.class));
        }});

        assignmentBeans.add(assignmentBean);*/

        List<AssignmentBean> assignmentBeans = new ArrayList<>();
        AssignmentBean assignmentBean = new AssignmentBean();
        assignmentBean.setLefetParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("outputObjectObj", OutputObject.OutputObjectObj.class));
            this.add(new ParamBean("outputObjectObjs", List.class));
            this.add(new ParamBean("outputObjectName", String.class));
        }});
        assignmentBean.setRightParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("teachers", List.class));
            this.add(new ParamBean("teacherName", String.class));
        }});

        assignmentBeans.add(assignmentBean);


        //基础类型测试
        DefaultAssignmentSubstitutionUtil.assignmentSubstitution(outputObject, school, assignmentBeans);


        System.out.println(outputObject);

    }


    /**
     * 测试getExpressionResults方法
     */
    public static void testGetExpressionResults() {

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

        School testBean = getTestSchool();

        Object expressionResults = DefaultAssignmentSubstitutionUtil.getExpressionResults(testBean, paramBeans);
        System.out.println(expressionResults);

    }


    private static School getTestSchool() {
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

        return testBean;
    }

}
