package com.lonely.bean;

import com.lonely.enums.Modifiers;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

    /**
     * 泛型类型
     */
    private String genericityType;

    /**
     * 是否是数组类型
     */
    private boolean isArray;


    /**
     * 获取泛型的类型的简称
     *
     * @return
     */
    public String getGenericitySimpleTypeName() {
        if (StringUtils.isBlank(genericityType)) {
            return null;
        }
        try {
            return Class.forName(this.genericityType).getSimpleName();
        } catch (ClassNotFoundException e) {
            return this.genericityType;
        }
    }
}
