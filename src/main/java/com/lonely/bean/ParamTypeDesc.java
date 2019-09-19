package com.lonely.bean;

import lombok.Data;

import java.util.List;

/**
 * @author ztkj-hzb
 * @Date 2019/9/19 16:39
 * @Description 参数类型定义
 */
@Data
public class ParamTypeDesc {

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数类型名称
     */
    private String commonTypeName;

    /**
     * 子参数类型
     */
    private List<ParamTypeDesc> childParams;

    /**
     * 是否存在泛型
     */
    private boolean existsT;
}
