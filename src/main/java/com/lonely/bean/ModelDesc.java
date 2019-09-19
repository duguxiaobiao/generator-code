package com.lonely.bean;

import com.lonely.enums.Modifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ztkj-hzb
 * @Date 2019/9/19 10:07
 * @Description 实体类描述
 */
public class ModelDesc {

    /**
     * 当前包名
     */
    private String packageName;

    /**
     * 导入的包名集合
     */
    private Set<String> importPackages;

    /**
     * 修饰符
     */
    private Modifiers modifiers;

    /**
     * 主类名
     */
    private String mainClassName;

    /**
     * 属性集合
     */
    private List<ModelFieldDesc> modelFieldDescs;

    /**
     * 方法集合
     */
    private List<ModelMethodDesc> modelMethodDescs;

    /**
     * 内联类集合
     */
    private List<ModelDesc> innerClasses;


    /**
     * 添加属性
     *
     * @param modelFieldDesc
     */
    public void addModelFieldDesc(ModelFieldDesc modelFieldDesc) {
        if (modelFieldDescs == null) {
            modelFieldDescs = new ArrayList<>();
        }
        modelFieldDescs.add(modelFieldDesc);
    }


    /**
     * 添加内部类
     *
     * @param modelDesc
     */
    public void addModelFieldDesc(ModelDesc modelDesc) {
        if (innerClasses == null) {
            innerClasses = new ArrayList<>();
        }
        innerClasses.add(modelDesc);
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Modifiers getModifiers() {
        return modifiers;
    }

    public void setModifiers(Modifiers modifiers) {
        this.modifiers = modifiers;
    }

    public String getMainClassName() {
        return mainClassName;
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName = mainClassName;
    }

    public List<ModelFieldDesc> getModelFieldDescs() {
        return modelFieldDescs;
    }

    public List<ModelMethodDesc> getModelMethodDescs() {
        return modelMethodDescs;
    }

    public void setModelMethodDescs(List<ModelMethodDesc> modelMethodDescs) {
        this.modelMethodDescs = modelMethodDescs;
    }
}
