package com.lonely.test;

import com.lonely.bean.ModelDesc;
import com.lonely.bean.ParamTypeDesc;
import com.lonely.generator.DefaultModelHandler;
import com.lonely.util.ClassUtil;
import com.lonely.util.DefaultModelBuildUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztkj-hzb
 * @Date 2019/11/12 16:29
 * @Description
 */
public class TestSimpleBean {


    public static void main(String[] args) {

        List<ParamTypeDesc> paramTypeDescs = new ArrayList<>();

        ParamTypeDesc paramTypeDesc = new ParamTypeDesc();
        paramTypeDesc.setParamName("userName");
        paramTypeDesc.setCommonTypeName("java.lang.String");
        paramTypeDesc.setExistsT(false);
        paramTypeDescs.add(paramTypeDesc);

        ParamTypeDesc paramTypeDesc2 = new ParamTypeDesc();
        paramTypeDesc2.setParamName("age");
        paramTypeDesc2.setCommonTypeName("java.lang.Integer");
        paramTypeDesc2.setExistsT(false);
        paramTypeDescs.add(paramTypeDesc2);

        ModelDesc modelDesc = DefaultModelBuildUtil.buildModelDescByParamType(paramTypeDescs);

        String generator = DefaultModelHandler.generator(modelDesc);

        System.out.println(generator);

        //
        //Class<?> compile = ClassUtil.compile(modelDesc.getMainClassName(), generator);

    }


}
