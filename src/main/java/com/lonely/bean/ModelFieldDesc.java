package com.lonely.bean;

import com.lonely.enums.Modifiers;
import lombok.Data;

/**
 * @author ztkj-hzb
 * @Date 2019/9/19 10:09
 * @Description 实体属性
 */
@Data
public class ModelFieldDesc {

    /**
     * 修饰符
     */
    private Modifiers modifier;

    /**
     * 属性的全类名,例如 Integer --> java.lang.Integer
     */
    private String fullTypeName;

    /**
     * 类型的简称，例如 Integer或int
     */
    private String type;

    /**
     * 属性名
     */
    private String fieldName;


}
