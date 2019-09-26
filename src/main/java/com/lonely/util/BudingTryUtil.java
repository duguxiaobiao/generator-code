package com.lonely.util;
import java.util.logging.Logger;

public class BudingTryUtil {
    public static String BudingTry(String tryBody){
        StringBuffer vue = new StringBuffer(" try {");
        vue.append(tryBody);
        vue.append("} catch (Exception e) {");
        vue.append("e.printStackTrace();");
        vue.append("}");
        return vue.toString();
    }
}
