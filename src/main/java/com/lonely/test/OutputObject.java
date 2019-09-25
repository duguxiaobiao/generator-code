package com.lonely.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ztkj-hzb
 * @Date 2019/9/24 15:28
 * @Description 输出对象实体
 */
@Data
public class OutputObject {

    /**
     * 测试单纯整形
     */
    private String outputName;

    /**
     * 测试对象类型
     */
    private OutputObjectObj outputObjectObj;

    /**
     * 测试对象类型
     */
    private List<OutputObjectObj> outputObjectObjs;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OutputObjectObj {

        private String outputObjectName;

        private List<OutputObjectObj> outputObjectObjs;

    }

}
