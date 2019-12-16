package com.lonely.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ztkj-hzb
 * @Date 2019/9/24 10:48
 * @Description
 */
@Data
public class School {

    private String schoolName;

    private Clazz clazz;

    private List<Teacher> teachers;

    private String[] strArr;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Clazz {

        private String clazzName;

        private List<Student> students;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student {

        private String stuName;

        private Integer age;

        public Student(String stuName) {
            this.stuName = stuName;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Teacher {

        private String teacherName;

        private List<Student> students;

        private String[] habbos;

        private Student[] studentArr;

        private List<School> school;

        public Teacher(String teacherName, List<Student> students) {
            this.teacherName = teacherName;
            this.students = students;
        }


    }

}
