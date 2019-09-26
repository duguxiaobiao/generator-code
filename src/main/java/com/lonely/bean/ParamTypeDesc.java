package com.lonely.bean;

import com.lonely.util.DefaultModelBuildUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

    /**
     * 泛型的类型
     */
    private String genericityType;

    /**
     * 是否是数组
     */
    private boolean isArray;

    /**
     * 获取全类名对应的class对象
     *
     * @return
     */
    public Class getType() {
        if (StringUtils.isBlank(commonTypeName)) {
            return null;
        }
        try {
            return Class.forName(this.commonTypeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 子参数构建的名称
     *
     * @return
     */
    public String getChildTypeName() {
        return DefaultModelBuildUtil.generatorClassName();
    }
}
