package com.lonely.generator;

import com.lonely.bean.ModelDesc;
import com.lonely.bean.ModelFieldDesc;
import com.lonely.test.School;
import com.lonely.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author ztkj-hzb
 * @Date 2019/9/20 10:55
 * @Description
 */
public class DefaultModelHandler {


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
        modelBuilder.append(generatorImport(modelDesc));

        //组装class声明信息
        modelBuilder.append(generatorClassStatementStart(modelDesc));

        //组装class主题信息
        modelBuilder.append(generatorClassBody(modelDesc));

        //组装内部类部分
        modelBuilder.append(generatorInnerClass(modelDesc.getInnerClasses()));

        //构建class声明结束部分
        modelBuilder.append(generatorClassStatementEnd());

        return modelBuilder.toString();
    }


    /**
     * 构建内联类集合的代码code
     *
     * @param innerModels
     * @return
     */
    public static String generatorInnerClass(List<ModelDesc> innerModels) {
        StringBuilder innerModelCodeStr = new StringBuilder();
        if (CollectionUtils.isEmpty(innerModels)) {
            return StringUtils.EMPTY;
        }
        for (ModelDesc innerModel : innerModels) {
            innerModelCodeStr.append(generatorInnerClass(innerModel));
        }
        return innerModelCodeStr.toString();
    }

    /**
     * 构建内联类的代码code
     *
     * @param innerModel
     * @return
     */
    public static String generatorInnerClass(ModelDesc innerModel) {

        //组装代码
        StringBuilder modelBuilder = new StringBuilder();

        //1.构建类声明
        modelBuilder.append(generatorClassStatementStart(innerModel));

        //2.组装class主题信息
        modelBuilder.append(generatorClassBody(innerModel));

        //3.构建class声明结束部分
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
     * 构建import部分
     *
     * @param modelDesc
     * @return
     */
    public static String generatorImport(ModelDesc modelDesc) {
        StringBuilder msg = new StringBuilder();

        //1. 获取导入类集合
        Set<String> importClassStrs = getImportClasses(modelDesc);

        //2.拼接代码
        if (CollectionUtils.isEmpty(importClassStrs)) {
            return StringUtils.EMPTY;
        }
        for (String importPackage : importClassStrs) {
            msg.append(MessageFormat.format("import {0};", importPackage)).append("\r\n");
        }
        return msg.toString();
    }


    /**
     * 获取指定实体类对应的导入类信息集合
     *
     * @param modelDesc
     * @return
     */
    public static Set<String> getImportClasses(ModelDesc modelDesc) {

        Set<String> importClasses = new HashSet<>();

        //1.处理主类的属性部分
        Set<String> mainImportClasses = getFieldsImportClasses(modelDesc.getModelFieldDescs());
        importClasses.addAll(mainImportClasses);

        //2.处理内联类中的属性部分
        if (CollectionUtils.isNotEmpty(modelDesc.getInnerClasses())) {
            for (ModelDesc innerClass : modelDesc.getInnerClasses()) {
                importClasses.addAll(getImportClasses(innerClass));
            }
        }
        return importClasses;
    }

    /**
     * 提取属性中的带映入的类名
     *
     * @param modelFieldDescs
     * @return
     */
    private static Set<String> getFieldsImportClasses(List<ModelFieldDesc> modelFieldDescs) {
        if (CollectionUtils.isEmpty(modelFieldDescs)) {
            return new HashSet<>();
        }

        //带过滤的包名，因为这些包不需要应用
        Set<String> ignorePrefixImports = new HashSet<>();
        ignorePrefixImports.add("java.lang");

        //提取存在全类名的属性
        List<String> importPackages = modelFieldDescs.stream().filter(modelFieldDesc ->
                StringUtils.isNotBlank(modelFieldDesc.getFullTypeName()) && modelFieldDesc.getFullTypeName().contains("."))
                .map(ModelFieldDesc::getFullTypeName)
                //.map(fullTypeName -> fullTypeName.substring(0, fullTypeName.lastIndexOf(".")))
                .filter(fullTypeName -> {
                    for (String ignorePrefixImport : ignorePrefixImports) {
                        if (fullTypeName.contains(ignorePrefixImport)) {
                            return false;
                        }
                    }
                    return true;
                })
                .distinct()
                .collect(Collectors.toList());

        return new HashSet<>(importPackages);
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
        return "}" + "\r\n";
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

                //判断是否存在泛型
                if (StringUtils.isEmpty(modelFieldDesc.getGenericityType())) {
                    //不存在泛型

                    //判断是否数组,因为java中数组无法指定类型，即没有全类名，所以这里约定 数组的Type为 Array就是数组
                    if (modelFieldDesc.isArray()) {
                        //构建成 private int[] ints;
                        fieldBuilder.append("\t").append(MessageFormat.format("{0} {1}[] {2};", modelFieldDesc.getModifier().modifier, modelFieldDesc.getType(), modelFieldDesc.getFieldName()))
                                .append("\r\n");
                    } else {
                        fieldBuilder.append("\t").append(MessageFormat.format("{0} {1} {2};", modelFieldDesc.getModifier().modifier, modelFieldDesc.getType(), modelFieldDesc.getFieldName()))
                                .append("\r\n");
                    }

                } else {
                    //存在泛型

                    //判断是否数组,因为java中数组无法指定类型，即没有全类名，所以这里约定 数组的Type为 Array就是数组
                    if (modelFieldDesc.isArray()) {
                        //构建成 private List<School.Student>[] lists;
                        fieldBuilder.append("\t").append(MessageFormat.format("{0} {1}<{2}>[] {3};", modelFieldDesc.getModifier().modifier, modelFieldDesc.getType(),
                                modelFieldDesc.getGenericitySimpleTypeName(), modelFieldDesc.getFieldName()))
                                .append("\r\n");

                    } else {
                        fieldBuilder.append("\t").append(MessageFormat.format("{0} {1}<{2}> {3};", modelFieldDesc.getModifier().modifier, modelFieldDesc.getType(),
                                modelFieldDesc.getGenericitySimpleTypeName(), modelFieldDesc.getFieldName()))
                                .append("\r\n");
                    }


                }

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

        //属性类型
        String type = modelFieldDesc.getType();
        if (StringUtils.isNotEmpty(modelFieldDesc.getGenericityType())) {
            //有泛型
            //是否是数组类型
            if (modelFieldDesc.isArray()) {
                type = MessageFormat.format("{0}<{1}>[]", type, modelFieldDesc.getGenericitySimpleTypeName());
            } else {
                type = MessageFormat.format("{0}<{1}>", type, modelFieldDesc.getGenericitySimpleTypeName());
            }
        } else {
            //没有泛型，判断是否是数组类型
            if (modelFieldDesc.isArray()) {
                type = MessageFormat.format("{0}[]", type);
            } else {
                type = MessageFormat.format("{0}", type);
            }
        }

        getterMethodBuilder.append("\t").append(MessageFormat.format("public {0} get{1}()", type, StringUtil.upperCase(modelFieldDesc.getFieldName())))
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

        String type = modelFieldDesc.getType();
        //判断是否有泛型
        if (StringUtils.isNotBlank(modelFieldDesc.getGenericitySimpleTypeName())) {
            //判断是否是数组类型
            if (modelFieldDesc.isArray()) {
                type = MessageFormat.format("{0}<{1}>[]", type, modelFieldDesc.getGenericitySimpleTypeName());
            } else {
                type = MessageFormat.format("{0}<{1}>", type, modelFieldDesc.getGenericitySimpleTypeName());
            }
        } else {
            //判断是否是数组类型
            if (modelFieldDesc.isArray()) {
                type = MessageFormat.format("{0}[]", type);
            } else {
                type = MessageFormat.format("{0}", type);
            }
        }

        setterMethodBuilder.append("\t").append(MessageFormat.format("public void set{0}({1} {2})", StringUtil.upperCase(modelFieldDesc.getFieldName()),
                type, modelFieldDesc.getFieldName()))
                .append("{").append("\r\n");
        setterMethodBuilder.append("\t\t").append(MessageFormat.format("this.{0} = {1};", modelFieldDesc.getFieldName(), modelFieldDesc.getFieldName())).append("\r\n");
        setterMethodBuilder.append("\t").append("}").append("\r\n");
        return setterMethodBuilder.toString();
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
