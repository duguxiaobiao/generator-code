package com.lonely.generator;

import com.lonely.util.ClassUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.instrument.ClassDefinition;

/**
 * @author ztkj-hzb
 * @Date 2019/9/19 15:24
 * @Description
 */
public class Test {


    @SuppressWarnings("all")
    public static void main(String[] args) throws Exception {

        Map<String,Object> map = new HashMap<>();
        map.put("name","dugu");
        map.put("age",25);
        map.put("aa",null);

        Map<String,Object> classInfoMap = new HashMap<>();
        classInfoMap.put("classId",1);
        classInfoMap.put("className","终极一班");
        map.put("classInfo",classInfoMap);

        System.out.println(map.get("classInfo").getClass().getTypeName());


        //构建的java类
        Map<String,String> convertMap = DefaultModelHandler.mapToModelStr(map);

        //编译
        /*Class<?> compile = ClassUtil.compile(convertMap.get("className"), convertMap.get("generatorCode"));

        //赋值
        Object newInstance = compile.newInstance();
        BeanUtils.copyProperties(newInstance,map);
        System.out.println(ClassUtil.getToString(newInstance));*/
    }


}
