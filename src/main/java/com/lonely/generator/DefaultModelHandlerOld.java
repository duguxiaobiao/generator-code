package com.lonely.generator;

import com.lonely.bean.ModelDesc;
import com.lonely.bean.ModelFieldDesc;
import com.lonely.bean.ParamTypeDesc;
import com.lonely.enums.Modifiers;
import com.lonely.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ztkj-hzb
 * @Date 2019/9/19 13:51
 * @Description 默认的构建普通的javaBean处理, 这里只涉及普通的 属性和对应的get set方法
 */
@Deprecated
public class DefaultModelHandlerOld {


    /**
     * 将定义的实体信息构建成实体bean字符串
     *
     * @param modelDesc
     * @return
     */
    public static String generator(ModelDesc modelDesc) {

        //组装代码
        StringBuilder modelBuilder = new StringBuilder();

        //组装package
        modelBuilder.append(generatorPackage(modelDesc.getPackageName()));

        //组装import
        modelBuilder.append(generatorImport(modelDesc.getModelFieldDescs()));

        //组装class声明信息
        modelBuilder.append(generatorClassStatementStart(modelDesc));

        //组装class主题信息
        modelBuilder.append(generatorClassBody(modelDesc));

        //构建class声明结束部分
        modelBuilder.append(generatorClassStatementEnd());

        return modelBuilder.toString();
    }

    /**
     * 构建package部分
     *
     * @param packageName
     * @return
     */
    public static String generatorPackage(String packageName) {
        StringBuilder msg = new StringBuilder();
        if (StringUtils.isNotEmpty(packageName)) {
            msg.append(MessageFormat.format("package {0};", packageName)).append("\r\n");
        }
        return msg.toString();
    }


    /**
     * 构建import部分,理论上 import部分不应该是由输入，所有从属性的类型中提取
     *
     * @param importPackages
     * @return
     */
    /*public static String generatorImport(Set<String> importPackages) {
        StringBuilder msg = new StringBuilder();
        if (CollectionUtils.isNotEmpty(importPackages)) {
            StringBuilder packageBuilder = new StringBuilder();
            for (String importPackage : importPackages) {
                packageBuilder.append(importPackage).append(";").append("\r\n");
            }
            msg.append(packageBuilder).append("\r\n");
        }
        return msg.toString();
    }*/


    /**
     * 从属性的类型上提取import部分code
     *
     * @param modelFieldDescs
     * @return
     */
    public static String generatorImport(List<ModelFieldDesc> modelFieldDescs) {

        StringBuilder msg = new StringBuilder();
        if (CollectionUtils.isEmpty(modelFieldDescs)) {
            return StringUtils.EMPTY;
        }

        //带过滤的包名，因为这些包不需要应用
        Set<String> ignorePrefixImports = new HashSet<>();
        ignorePrefixImports.add("java.lang.");

        //提取存在全类名的属性
        List<String> importPackages = modelFieldDescs.stream().filter(modelFieldDesc ->
                StringUtils.isNotBlank(modelFieldDesc.getFullTypeName()) && modelFieldDesc.getFullTypeName().contains("."))
                .map(ModelFieldDesc::getFullTypeName)
                .map(fullTypeName -> fullTypeName.substring(0, fullTypeName.lastIndexOf(".")))
                .filter(fullTypeName -> {
                    for (String ignorePrefixImport : ignorePrefixImports) {
                        if (fullTypeName.contains(ignorePrefixImport)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(importPackages)) {
            return StringUtils.EMPTY;
        }

        for (String importPackage : importPackages) {
            msg.append(MessageFormat.format("import {0};", importPackage)).append("\r\n");
        }

        return msg.toString();
    }


    /**
     * 构建类声明start
     *
     * @param modelDesc
     * @return
     */
    public static String generatorClassStatementStart(ModelDesc modelDesc) {
        StringBuilder clazzDescBuilder = new StringBuilder();
        clazzDescBuilder.append(MessageFormat.format("{0} class {1} ", modelDesc.getModifiers().modifier, modelDesc.getMainClassName()))
                .append("{").append("\r\n");
        return clazzDescBuilder.toString();
    }

    /**
     * 构建类主题部分
     *
     * @param modelDesc
     * @return
     */
    public static String generatorClassBody(ModelDesc modelDesc) {
        StringBuilder msg = new StringBuilder();

        //构建属性
        msg.append(generatorField(modelDesc.getModelFieldDescs()));

        //构建get set方法
        msg.append(generatorGetSetMethod(modelDesc.getModelFieldDescs()));

        return msg.toString();
    }

    /**
     * 构建类声明结束部分括号
     *
     * @return
     */
    public static String generatorClassStatementEnd() {
        return "}";
    }


    /**
     * 构建属性部分
     *
     * @param modelFieldDescs
     * @return
     */
    public static String generatorField(List<ModelFieldDesc> modelFieldDescs) {
        StringBuilder msg = new StringBuilder();
        if (CollectionUtils.isNotEmpty(modelFieldDescs)) {
            StringBuilder fieldBuilder = new StringBuilder();
            for (ModelFieldDesc modelFieldDesc : modelFieldDescs) {
                fieldBuilder.append("\t").append(MessageFormat.format("{0} {1} {2};", modelFieldDesc.getModifier().modifier, modelFieldDesc.getType(), modelFieldDesc.getFieldName()))
                        .append("\r\n");
            }
            msg.append(fieldBuilder).append("\r\n");
        }
        return msg.toString();
    }


    /**
     * 构建通用的getter setter方法
     *
     * @param modelFieldDescs
     * @return
     */
    public static String generatorGetSetMethod(List<ModelFieldDesc> modelFieldDescs) {
        if (CollectionUtils.isEmpty(modelFieldDescs)) {
            return StringUtils.EMPTY;
        }

        StringBuilder methodBuilder = new StringBuilder();
        for (ModelFieldDesc modelFieldDesc : modelFieldDescs) {
            methodBuilder.append(generatorGetterMethod(modelFieldDesc)).append(generatorSetterMethod(modelFieldDesc));
        }
        return methodBuilder.toString();
    }


    /**
     * 构建get方法，模型：
     * public String getName(){
     * return this.name
     * }
     *
     * @param modelFieldDesc
     * @return
     */
    private static String generatorGetterMethod(ModelFieldDesc modelFieldDesc) {
        StringBuilder getterMethodBuilder = new StringBuilder();
        getterMethodBuilder.append("\t").append(MessageFormat.format("public {0} get{1}()", modelFieldDesc.getType(), StringUtil.upperCase(modelFieldDesc.getFieldName())))
                .append("{").append("\r\n");

        getterMethodBuilder.append("\t\t").append(MessageFormat.format("return this.{0};", modelFieldDesc.getFieldName())).append("\r\n");

        getterMethodBuilder.append("\t}").append("\r\n");
        return getterMethodBuilder.toString();
    }


    /**
     * 构建set方法，模型：
     * public void setName(String name){
     * this.name = name;
     * }
     *
     * @param modelFieldDesc
     * @return
     */
    private static String generatorSetterMethod(ModelFieldDesc modelFieldDesc) {
        StringBuilder setterMethodBuilder = new StringBuilder();
        setterMethodBuilder.append("\t").append(MessageFormat.format("public void set{0}({1} {2})", StringUtil.upperCase(modelFieldDesc.getFieldName()),
                modelFieldDesc.getType(), modelFieldDesc.getFieldName()))
                .append("{").append("\r\n");
        setterMethodBuilder.append("\t\t").append(MessageFormat.format("this.{0} = {1};", modelFieldDesc.getFieldName(), modelFieldDesc.getFieldName())).append("\r\n");
        setterMethodBuilder.append("\t").append("}").append("\r\n");
        return setterMethodBuilder.toString();
    }

    /**
     * 构建内联类代码
     *
     * @return
     */
    public static String generatorInnerClass(ModelDesc innerModel) {

        //这里设定内敛为访问级别为 private static

        //1.构建类声明start
        innerModel.setModifiers(Modifiers.PRIVATE_STATIC);
        innerModel.setMainClassName(generatorClassName());


        return null;
    }


    /**
     * 将resultMap,这里是处理数据访问的返回结果，构建成普通的实体Bean
     *
     * @param resultMap
     * @return
     */
    public static Map<String, String> mapToModelStr(Map<String, Object> resultMap) {

        if (resultMap == null || resultMap.isEmpty()) {
            return null;
        }

        ModelDesc modelDesc = new ModelDesc();

        //设置类名
        modelDesc.setModifiers(Modifiers.PUBLIC);
        modelDesc.setMainClassName(generatorClassName());

        //添加属性
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            modelDesc.addModelFieldDesc(buildDefaultModelFieldDesc(entry.getKey(), entry.getValue().getClass()));
        }

        String generator = generator(modelDesc);
        System.out.println(generator);

        Map<String, String> convertMap = new HashMap<>();
        convertMap.put("className", modelDesc.getMainClassName());
        convertMap.put("generatorCode", generator);
        return convertMap;

    }


    public static Map<String, String> mapToModelStr2(Map<String, ParamTypeDesc> paramTypeDescMap) {

        if (paramTypeDescMap == null || paramTypeDescMap.isEmpty()) {
            return null;
        }

        ModelDesc modelDesc = new ModelDesc();

        //设置类名
        modelDesc.setModifiers(Modifiers.PUBLIC);
        modelDesc.setMainClassName(generatorClassName());

        //添加属性
        for (Map.Entry<String, ParamTypeDesc> entry : paramTypeDescMap.entrySet()) {
            modelDesc.addModelFieldDesc(buildDefaultModelFieldDesc(entry.getKey(), entry.getValue().getClass()));
        }

        String generator = generator(modelDesc);
        System.out.println(generator);

        Map<String, String> convertMap = new HashMap<>();
        convertMap.put("className", modelDesc.getMainClassName());
        convertMap.put("generatorCode", generator);
        return convertMap;

    }

    /**
     * 根据参数类型信息构建ModelDesc对象信息
     *
     * @param paramTypeDescMap
     * @return
     */
    public static ModelDesc buildModelDescByParamType(Map<String, ParamTypeDesc> paramTypeDescMap) {
        if (paramTypeDescMap == null || paramTypeDescMap.isEmpty()) {
            return null;
        }

        ModelDesc modelDesc = new ModelDesc();

        //
        for (ParamTypeDesc paramTypeDesc : paramTypeDescMap.values()) {

            Class type = paramTypeDesc.getType();
            if (type.isPrimitive() || "String".equalsIgnoreCase(type.getSimpleName())) {
                //基础数据类型,直接构建属性
                modelDesc.addModelFieldDesc(buildDefaultModelFieldDesc(paramTypeDesc));
            } else if ("HashMap".equalsIgnoreCase(type.getSimpleName())) {
                //hashmap，转换成内部类,然后添加内部类
                ModelDesc innerModel = buildInnerModelDesc(paramTypeDesc.getChildParams(), modelDesc);
                modelDesc.addInnerModelDesc(innerModel);
            }


        }


        return null;
    }

    /**
     * 根据参数集合构建内联类对象
     *
     * @param paramTypeDescs
     * @return
     */
    public static ModelDesc buildInnerModelDesc(List<ParamTypeDesc> paramTypeDescs, ModelDesc modelDesc) {

        if (CollectionUtils.isEmpty(paramTypeDescs)) {
            return null;
        }

        ModelDesc innerModelDesc = new ModelDesc();
        innerModelDesc.setModifiers(Modifiers.PRIVATE_STATIC);

        for (ParamTypeDesc paramTypeDesc : paramTypeDescs) {
            //判断是否是普通类型
            Class type = paramTypeDesc.getType();
            if (type.isPrimitive() || "String".equalsIgnoreCase(type.getSimpleName())) {
                //基础数据类型,直接构建属性
                innerModelDesc.addModelFieldDesc(buildDefaultModelFieldDesc(paramTypeDesc));

                //添加内部类
                modelDesc.addInnerModelDesc(innerModelDesc);
            } else if ("HashMap".equalsIgnoreCase(type.getSimpleName())) {
                //hashmap，转换成内部类,然后添加内部类
                ModelDesc innerModel = buildInnerModelDesc(paramTypeDesc.getChildParams(), modelDesc);

                //添加类名
                innerModelDesc.addModelFieldDesc(buildDefaultModelFieldDesc(paramTypeDesc.getChildTypeName(), paramTypeDesc.getParamName()));

                //添加内部类
                modelDesc.addInnerModelDesc(innerModel);
            }
        }

        return modelDesc;
    }


    /**
     * 构建属性信息
     *
     * @param fieldName
     * @param fieldClazz
     * @return
     */
    private static ModelFieldDesc buildDefaultModelFieldDesc(String fieldName, Class fieldClazz) {
        ModelFieldDesc modelFieldDesc = new ModelFieldDesc();
        modelFieldDesc.setModifier(Modifiers.PRIVATE);
        modelFieldDesc.setFullTypeName(fieldClazz.getTypeName());
        modelFieldDesc.setType(fieldClazz.getSimpleName());
        modelFieldDesc.setFieldName(fieldName);
        return modelFieldDesc;
    }

    /**
     * 构建属性信息
     *
     * @param paramTypeDesc
     * @return
     */
    private static ModelFieldDesc buildDefaultModelFieldDesc(ParamTypeDesc paramTypeDesc) {
        ModelFieldDesc modelFieldDesc = new ModelFieldDesc();
        modelFieldDesc.setModifier(Modifiers.PRIVATE);
        modelFieldDesc.setFullTypeName(paramTypeDesc.getCommonTypeName());
        modelFieldDesc.setType(paramTypeDesc.getType().getSimpleName());
        modelFieldDesc.setFieldName(paramTypeDesc.getParamName());
        return modelFieldDesc;
    }

    /**
     * 构建属性信息
     *
     * @param paramTypeDesc
     * @return
     */
    private static ModelFieldDesc buildDefaultModelFieldDesc(String typeName, String paramName) {
        ModelFieldDesc modelFieldDesc = new ModelFieldDesc();
        modelFieldDesc.setModifier(Modifiers.PRIVATE);
        modelFieldDesc.setType(typeName);
        modelFieldDesc.setFieldName(paramName);
        return modelFieldDesc;
    }

    /**
     * 构建随机类名
     *
     * @return
     */
    public static String generatorClassName() {
        String randomUuid = (UUID.randomUUID().toString().split("-"))[4];
        return MessageFormat.format("Generator${0}", randomUuid);
    }


}
