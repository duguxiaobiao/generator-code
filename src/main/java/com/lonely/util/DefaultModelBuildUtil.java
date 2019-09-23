package com.lonely.util;

import com.lonely.bean.ModelDesc;
import com.lonely.bean.ModelFieldDesc;
import com.lonely.bean.ParamTypeDesc;
import com.lonely.enums.Modifiers;
import org.apache.commons.collections4.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

/**
 * @author ztkj-hzb
 * @Date 2019/9/20 14:10
 * @Description 默认的构建ModelDesc实体工具类
 */
public class DefaultModelBuildUtil {

    private DefaultModelBuildUtil() {
    }

    /**
     * 根据参数类型信息构建ModelDesc对象信息
     *
     * @param paramTypeDescs
     * @return
     */
    public static ModelDesc buildModelDescByParamType(List<ParamTypeDesc> paramTypeDescs) {
        if (CollectionUtils.isEmpty(paramTypeDescs)) {
            return null;
        }

        //创建ModelDesc实体类
        ModelDesc modelDesc = new ModelDesc();
        modelDesc.setModifiers(Modifiers.PUBLIC);
        modelDesc.setMainClassName(generatorClassName());

        //构建属性对象以及内部类
        for (ParamTypeDesc paramTypeDesc : paramTypeDescs) {
            Class type = paramTypeDesc.getType();
            if (type.isPrimitive() || ClassUtil.isWrapClass(type) || "String".equalsIgnoreCase(type.getSimpleName())) {
                //基础数据类型,直接构建属性
                modelDesc.addModelFieldDesc(buildDefaultModelFieldDesc(paramTypeDesc));
            } else if ("HashMap".equalsIgnoreCase(type.getSimpleName())) {
                //hashmap，转换成内部类,然后添加内部类
                buildInnerModelDesc(paramTypeDesc, modelDesc, modelDesc);
            } else if ("List".equalsIgnoreCase(type.getSimpleName()) || "Set".equalsIgnoreCase(type.getSimpleName())) {
                //判断是否存在子节点集合，如果不存在，则说明是List或List<String>等基础类型,否则List<Student>这样的类型
                if (CollectionUtils.isEmpty(paramTypeDesc.getChildParams())) {
                    modelDesc.addModelFieldDesc(buildDefaultModelFieldDesc(paramTypeDesc));
                } else {
                    //有子节点的，创建内部类
                    buildInnerModelDesc(paramTypeDesc, modelDesc, modelDesc);
                }
            }
        }
        return modelDesc;
    }


    /**
     * 根据参数类型构建实体属性信息对象，默认为private修饰符
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

        //泛型类型
        modelFieldDesc.setGenericityType(paramTypeDesc.getGenericityType());

        return modelFieldDesc;
    }

    /**
     * 根据参数集合构建内联类对象
     *
     * @param headerParamType
     * @return
     */
    private static void buildInnerModelDesc(ParamTypeDesc headerParamType, ModelDesc modelDesc, ModelDesc previous) {

        if (CollectionUtils.isEmpty(headerParamType.getChildParams())) {
            return;
        }

        //创建内部类对象
        ModelDesc innerModelDesc = new ModelDesc();
        innerModelDesc.setModifiers(Modifiers.PUBLIC_STATIC);

        //添加属性和内部类信息
        for (ParamTypeDesc paramTypeDesc : headerParamType.getChildParams()) {
            //判断是否是普通类型
            Class type = paramTypeDesc.getType();
            if (type.isPrimitive() || ClassUtil.isWrapClass(type) || "String".equalsIgnoreCase(type.getSimpleName())) {
                //基础数据类型,直接构建属性
                innerModelDesc.addModelFieldDesc(buildDefaultModelFieldDesc(paramTypeDesc));
            } else if ("HashMap".equalsIgnoreCase(type.getSimpleName())) {
                //hashmap，转换成内部类,然后添加内部类
                buildInnerModelDesc(paramTypeDesc, modelDesc, innerModelDesc);
            } else if (("List".equalsIgnoreCase(type.getSimpleName()) || "Set".equalsIgnoreCase(type.getSimpleName())) && CollectionUtils.isNotEmpty(paramTypeDesc.getChildParams())) {
                //非基础数据类型的泛型 的List
                buildInnerModelDesc(paramTypeDesc, modelDesc, innerModelDesc);
            }
        }

        //设置内联类的类名
        String innerClassName = headerParamType.getChildTypeName();
        innerModelDesc.setMainClassName(innerClassName);

        //添加外层属性，判断当前类型是List还是Object
        Class type = headerParamType.getType();
        if ("List".equalsIgnoreCase(type.getSimpleName()) || "Set".equalsIgnoreCase(type.getSimpleName())) {
            headerParamType.setGenericityType(innerClassName);
            previous.addModelFieldDesc(buildDefaultModelFieldDesc(headerParamType));
            //previous.addModelFieldDesc(buildDefaultModelFieldDesc(headerParamType.getCommonTypeName(), headerParamType.getType().getSimpleName(),innerClassName));
        } else {
            previous.addModelFieldDesc(buildDefaultModelFieldDesc(innerClassName, headerParamType.getParamName()));
        }

        //添加内部类,注意这里是放在在外层的实体bean中，因为所有的内部类同级别
        modelDesc.addInnerModelDesc(innerModelDesc);
    }


    /**
     * 构建属性信息,指定类型和参数名，默认是private修饰符
     *
     * @param typeName
     * @param paramName
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
