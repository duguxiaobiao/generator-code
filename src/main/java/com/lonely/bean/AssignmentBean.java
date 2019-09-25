package com.lonely.bean;

import lombok.Data;

import java.util.List;

/**
 * @author ztkj-hzb
 * @Date 2019/9/24 10:22
 * @Description 用于表示赋值的表达式bean,该类最终表示 前端的一条展示 例如 输入对象.用户名 = 响应结果.用户.用户名 这个格式
 */
@Data
public class AssignmentBean {

    /**
     * 左侧赋值信息
     */
    private List<ParamBean> lefetParamBeans;

    /**
     * 右侧赋值信息
     */
    private List<ParamBean> rightParamBeans;
}
