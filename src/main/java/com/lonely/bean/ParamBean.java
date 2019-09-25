package com.lonely.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ztkj-hzb
 * @Date 2019/9/24 10:20
 * @Description 参数实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamBean {

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数类型
     */
    private Class clazz;

}
