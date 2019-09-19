package com.lonely.enums;

/**
 * @author ztkj-hzb
 * @Date 2019/9/19 10:11
 * @Description
 */
public enum Modifiers {

    NONE(" "),
    PUBLIC("public "),
    PUBLIC_ABSTRACT("public abstract "),
    PUBLIC_STATIC("public static "),
    PROTECTED("protected "),
    PRIVATE("private "),
    PRIVATE_STATIC("private static ");


    public String modifier;

    Modifiers(String modifier) {
        this.modifier = modifier;
    }
}
