package com.lonely.util;

import org.apache.commons.lang.StringUtils;

import javax.tools.*;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * @author ztkj-hzb
 * @Date 2019/9/19 11:36
 * @Description
 */
public class ClassUtil {

    private static final String LINE = System.getProperty("line.separator");

    /**
     * 装载字符串成为java可执行文件
     *
     * @param className className
     * @param javaCodes javaCodes
     * @return Class
     */
    public static Class<?> compile(String className, String javaCodes) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        StrSrcJavaObject srcObject = new StrSrcJavaObject(className, javaCodes);
        Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(srcObject);
        String flag = "-d";
        String outDir = "";
        try {
            File classPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            outDir = classPath.getAbsolutePath() + File.separator;
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        Iterable<String> options = Arrays.asList(flag, outDir);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, fileObjects);
        boolean result = task.call();
        if (result) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String getToStringFormat(Object o) {
        Class<? extends Object> clazz = o.getClass();
        StringBuilder builder =
                new StringBuilder()
                        .append(clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1))
                        .append(LINE)
                        .append("   [");
        Field[] fields = clazz.getDeclaredFields();
        for (int i = fields.length - 1; i >= 0; i--) {
            Field field = fields[i];
            field.setAccessible(true);
            if (i == 0) {
                Object fieldValue = getFieldValue(o, field);
                builder.append(LINE)
                        .append("    ")
                        .append(field.getName())
                        .append(" = ");
                if (fieldValue == null) {
                    builder.append("null");
                } else {
                    builder.append(fieldValue.toString());
                }
                builder.append(LINE)
                        .append("   ]");
            } else {
                builder.append(LINE)
                        .append("    ")
                        .append(field.getName())
                        .append(" = ");
                Object fieldValue = getFieldValue(o, field);
                if (fieldValue == null) {
                    builder.append("null");
                } else {
                    builder.append(fieldValue.toString());
                }
                builder.append(",");
            }
        }
        return builder.toString();
    }

    /**
     * 将对象toString显示
     *
     * @param o
     * @return
     */
    public static String getToString(Object o) {
        Class<? extends Object> clazz = o.getClass();
        StringBuilder builder =
                new StringBuilder()
                        .append(clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1))
                        .append(" [");
        Field[] fields = clazz.getDeclaredFields();
        for (int i = fields.length - 1; i >= 0; i--) {
            Field field = fields[i];
            field.setAccessible(true);
            if (i == 0) {
                Object fieldValue = getFieldValue(o, field);
                builder.append(field.getName())
                        .append(" = ");
                if (fieldValue == null) {
                    builder.append("null");
                } else {
                    builder.append(fieldValue.toString());
                }
                builder.append("]");
            } else {
                builder.append(field.getName())
                        .append(" = ");
                Object fieldValue = getFieldValue(o, field);
                if (fieldValue == null) {
                    builder.append("null");
                } else {
                    builder.append(fieldValue.toString());
                }
                builder.append(",");
            }
        }
        return builder.toString();
    }

    private static Object getFieldValue(Object o, Field field) {
        try {
            return field.get(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static class StrSrcJavaObject extends SimpleJavaFileObject {
        private String content;

        public StrSrcJavaObject(String name, String content) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }
    }

    /**
     * 判断是否是基础数据类型的包装类型
     *
     * @param clz
     * @return
     */
    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 判断指定类是否是List的子类或者父类
     *
     * @param clz
     * @return
     */
    public static boolean isListTypeClass(Class clz) {
        try {
            return clz.isAssignableFrom(List.class) || clz.newInstance() instanceof List;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 判断指定类是否是数组
     *
     * @param clz
     * @return
     */
    public static boolean isArrayTypeClass(Class clz) {
        return clz.isArray();
    }


    /**
     * 判断指定类名是否是数组
     *
     * @param className
     * @return
     */
    public static boolean isArrayTypeClass(String className) {
        if (StringUtils.isEmpty(className)) {
            return false;
        }

        try {
            Class<?> forName = Class.forName(className);
            return isArrayTypeClass(forName);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
