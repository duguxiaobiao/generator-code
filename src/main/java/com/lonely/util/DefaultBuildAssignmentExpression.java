package com.lonely.util;

import com.lonely.bean.ParamTypeDesc;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ztkj-hzb
 * @Date 2019/9/23 11:15
 * @Description 最基础的构建赋值表达式解析处理过程
 */
public class DefaultBuildAssignmentExpression {

    private DefaultBuildAssignmentExpression() {
    }


    /**
     * 根据参数集合是实际的结果 构建符合BeanWrapper赋值表达式的map
     *
     * @param paramTypeDescs
     * @param resultMap
     * @return
     */
    public static Map<String, Object> buildAssignmentExpression(List<ParamTypeDesc> paramTypeDescs, Map<String, Object> resultMap) {

        Map<String, Object> returnMap = new HashMap<>();
        if (resultMap == null || resultMap.isEmpty()) {
            return returnMap;
        }

        //将参数列表根据名称分组
        if (CollectionUtils.isEmpty(paramTypeDescs)) {
            return returnMap;
        }
        Map<String, ParamTypeDesc> paramTypeDescMap = paramTypeDescs.stream().collect(Collectors.toMap(ParamTypeDesc::getParamName, Function.identity(), (x, y) -> x));


        //遍历结果集的所有key
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {

            //属性名
            String paramName = entry.getKey();

            //通过key，获取对应的类型
            ParamTypeDesc paramTypeDesc = paramTypeDescMap.get(paramName);
            //判断类型
            Class type = paramTypeDesc.getType();
            if (type.isPrimitive() || ClassUtil.isWrapClass(type) || "String".equalsIgnoreCase(type.getSimpleName())) {
                //基础数据类型,直接构建属性
                returnMap.put(paramName, entry.getValue());
            } else if ("HashMap".equalsIgnoreCase(type.getSimpleName())) {
                //hashmap，转换成内部类,然后添加内部类
                //根据key获取对应的值
                Map<String, Object> map = (Map<String, Object>) resultMap.get(paramName);
                returnMap.putAll(objectAssignmentHanlder(null, paramTypeDesc, map));
            } else if ("List".equalsIgnoreCase(type.getSimpleName())) {
                //这里不支持Set，因为Set不是按照索引处理
                //判断是否存在子节点集合，如果不存在，则说明是List或List<String>等基础类型,否则List<Student>这样的类型
                //判断是否存在泛型
                List<Object> list = (List<Object>) resultMap.get(paramName);
                returnMap.putAll(listAssignmentHanlder(null, paramTypeDesc, list));
            }

        }

        return returnMap;
    }


    /**
     * 递归处理 object类型(即Map)类型的 转换成对应的拼接格式  例如： user.clazz.clazzName, user.clazz.clazzId赋值关系
     *
     * @param keyPrefix
     * @param paramTypeDesc
     * @param map
     * @return
     */
    private static Map<String, Object> objectAssignmentHanlder(String keyPrefix, ParamTypeDesc paramTypeDesc, Map<String, Object> map) {

        //判断是否存在子节点
        if (CollectionUtils.isEmpty(paramTypeDesc.getChildParams())) {
            return new HashMap<>();
        }

        //根据当前的参数名，构建前缀部分例如 user.xx.xx
        if (StringUtils.isNotBlank(keyPrefix)) {
            keyPrefix = MessageFormat.format("{0}.{1}", keyPrefix, paramTypeDesc.getParamName());
        } else {
            keyPrefix = paramTypeDesc.getParamName();
        }

        Map<String, Object> buildMap = new HashMap<>();

        for (ParamTypeDesc childParam : paramTypeDesc.getChildParams()) {
            //判断类型
            Class type = childParam.getType();
            if (type.isPrimitive() || ClassUtil.isWrapClass(type) || "String".equalsIgnoreCase(type.getSimpleName())) {
                //基础数据类型,直接构建属性
                String key = MessageFormat.format("{0}.{1}", keyPrefix, childParam.getParamName());
                buildMap.put(key, map.get(childParam.getParamName()));
            } else if ("HashMap".equalsIgnoreCase(type.getSimpleName())) {
                //hashmap，转换成内部类,然后添加内部类
                buildMap.putAll(objectAssignmentHanlder(keyPrefix, childParam, (Map<String, Object>) map.get(childParam.getParamName())));
            } else if ("List".equalsIgnoreCase(type.getSimpleName()) || "Set".equalsIgnoreCase(type.getSimpleName())) {
                //判断是否存在子节点集合，如果不存在，则说明是List或List<String>等基础类型,否则List<Student>这样的类型
                buildMap.putAll(listAssignmentHanlder(null, childParam, (List<Object>) map.get(childParam.getParamName())));
            }
        }
        return buildMap;
    }


    /**
     * 递归处理 object类型(即Map)类型的 转换成对应的拼接格式  例如： user.clazz.clazzName, user.clazz.clazzId赋值关系
     *
     * @param keyPrefix
     * @param paramTypeDesc
     * @param list
     * @return
     */
    private static Map<String, Object> listAssignmentHanlder(String keyPrefix, ParamTypeDesc paramTypeDesc, List<Object> list) {

        Map<String, Object> buildMap = new HashMap<>();

        //判断是否存在子节点
        /*if (CollectionUtils.isEmpty(paramTypeDesc.getChildParams())) {
            return buildMap;
        }*/

        if (CollectionUtils.isEmpty(list)) {
            return buildMap;
        }


        //根据当前的参数名，构建前缀部分例如 users[0].name
        if (StringUtils.isNotBlank(keyPrefix)) {
            keyPrefix = MessageFormat.format("{0}.{1}", keyPrefix, paramTypeDesc.getParamName());
        } else {
            keyPrefix = paramTypeDesc.getParamName();
        }


        //先循环具体数据的条数
        for (int i = 0; i < list.size(); i++) {
            Object currData = list.get(i);
            //判断是否具有泛型
            if (paramTypeDesc.isExistsT()) {
                //存在泛型
                //判断泛型是否是基础数据类型
                if (CollectionUtils.isEmpty(paramTypeDesc.getChildParams())) {
                    //基础数据类型
                    String key = MessageFormat.format("{0}[{1}]", keyPrefix, i);
                    buildMap.put(key, currData);
                } else {
                    //对象类型
                    Map<String, Object> currMap = (Map<String, Object>) currData;
                    for (ParamTypeDesc childParam : paramTypeDesc.getChildParams()) {
                        //判断类型
                        Class type = childParam.getType();
                        if (type.isPrimitive() || ClassUtil.isWrapClass(type) || "String".equalsIgnoreCase(type.getSimpleName())) {
                            //基础数据类型,直接构建属性
                            String key = MessageFormat.format("{0}[{1}].{2}", keyPrefix, i, childParam.getParamName());
                            buildMap.put(key, currMap.get(childParam.getParamName()));
                        } else if ("HashMap".equalsIgnoreCase(type.getSimpleName())) {
                            //hashmap，转换成内部类,然后添加内部类
                            //buildMap.putAll(objectAssignmentHanlder(keyPrefix, childParam, (Map<String, Object>) map.get(childParam.getParamName())));
                        } else if ("List".equalsIgnoreCase(type.getSimpleName()) || "Set".equalsIgnoreCase(type.getSimpleName())) {
                            //判断是否存在子节点集合，如果不存在，则说明是List或List<String>等基础类型,否则List<Student>这样的类型
                        }
                    }
                }

            } else {
                //不存在泛型，直接赋值
                String key = MessageFormat.format("{0}[{1}]", keyPrefix, i);
                buildMap.put(key, currData);
            }

        }


        return buildMap;
    }


}
