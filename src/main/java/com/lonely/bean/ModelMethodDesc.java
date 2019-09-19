package com.lonely.bean;

import com.lonely.enums.Modifiers;
import lombok.Data;

import java.util.List;

/**
 * @author ztkj-hzb
 * @Date 2019/9/19 10:17
 * @Description 实体方法
 */
@Data
public class ModelMethodDesc {

    /**
     * 修饰符
     */
    private Modifiers modifier;

    /**
     * 返回值类型
     */
    private String returnType;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数集合
     */
    private List<MethodParameterDesc> parameterDescs;

}
