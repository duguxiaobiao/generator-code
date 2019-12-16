package com.lonely.test;

import com.lonely.bean.AssignmentBean;
import com.lonely.bean.ParamBean;
import com.lonely.util.DefaultAssignmentSubstitutionUtil;

import java.util.ArrayList;
import java.util.HashMap;
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


        //对象中的对象集合类型赋值
        AssignmentBean assignmentBean = new AssignmentBean();
        assignmentBean.setLefetParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("outputObjectObj", OutputObject.OutputObjectObj.class, false));
            this.add(new ParamBean("outputObjectObjs", List.class, false));
            this.add(new ParamBean("outputObjectName", String.class, false));
        }});
        assignmentBean.setRightParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("teachers", List.class, false));
            this.add(new ParamBean("teacherName", String.class, false));
        }});

        //基础类型
        AssignmentBean assignmentBean2 = new AssignmentBean();
        assignmentBean2.setLefetParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("outputName", String.class, false));
        }});
        assignmentBean2.setRightParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("clazz", School.Clazz.class, false));
            this.add(new ParamBean("clazzName", String.class, false));
        }});

        //集合中的普通类型
        AssignmentBean assignmentBean3 = new AssignmentBean();
        assignmentBean3.setLefetParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("outputObjectObjs", List.class, false));
            this.add(new ParamBean("outputObjectName", String.class, false));
        }});
        assignmentBean3.setRightParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("teachers", List.class, false));
            this.add(new ParamBean("teacherName", String.class, false));
        }});

        //4.基础类型的数组
        AssignmentBean assignmentBean4 = new AssignmentBean();
        assignmentBean4.setLefetParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("strArr", String.class, true));
        }});
        assignmentBean4.setRightParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("strArr", String.class, true));
        }});

        //5. 集合中嵌套数组
        AssignmentBean assignmentBean5 = new AssignmentBean();
        assignmentBean5.setLefetParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("strArr", String.class, true));
        }});
        assignmentBean5.setRightParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("teachers", List.class, false));
            this.add(new ParamBean("habbos", String.class, true));
        }});

        //6.集合中嵌套对象数组
        AssignmentBean assignmentBean6 = new AssignmentBean();
        assignmentBean6.setLefetParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("strArr", String.class, true));
        }});
        assignmentBean6.setRightParamBeans(new ArrayList<ParamBean>() {{
            this.add(new ParamBean("teachers", List.class, false));
            //this.add(new ParamBean("studentArr", School.Student.class, true));
            this.add(new ParamBean("studentArr", HashMap.class, true));
            this.add(new ParamBean("stuName", String.class, false));
        }});

        //assignmentBeans.add(assignmentBean);
        //assignmentBeans.add(assignmentBean2);
        //assignmentBeans.add(assignmentBean3);
        //assignmentBeans.add(assignmentBean4);
        //assignmentBeans.add(assignmentBean5);
        assignmentBeans.add(assignmentBean6);


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
        ParamBean teacherParam = new ParamBean("teachers", List.class, false);
        ParamBean studentParam = new ParamBean("students", List.class, false);
        ParamBean studentNameParam = new ParamBean("age", Integer.class, false);
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
        teacher1.setHabbos(new String[]{"fdasf", "ewqrr"});
        School.Student[] studentArr1 = new School.Student[2];
        School.Student studentArr1_1 = new School.Student("xx",18);
        School.Student studentArr1_2 = new School.Student("xxfdaf",18);
        studentArr1[0] = studentArr1_1;
        studentArr1[1] = studentArr1_2;
        teacher1.setStudentArr(studentArr1);

        School.Teacher teacher2 = new School.Teacher();
        teacher2.setTeacherName("tea2");
        teacher2.setStudents(new ArrayList<School.Student>() {{
            this.add(new School.Student("aafdas", 15));
            this.add(new School.Student("bbfda", 16));
        }});
        teacher2.setHabbos(new String[]{"xx", "fdafd", "fdafda"});
        School.Student[] studentArr2 = new School.Student[2];
        School.Student studentArr2_1 = new School.Student("xx",18);
        School.Student studentArr2_2 = new School.Student("xxfdaf",18);
        studentArr2[0] = studentArr2_1;
        studentArr2[1] = studentArr2_2;
        teacher2.setStudentArr(studentArr2);

        teachers.add(teacher1);
        teachers.add(teacher2);
        testBean.setTeachers(teachers);

        testBean.setStrArr(new String[]{"xx", "xxdff", "fdsaf"});

        return testBean;
    }

}
