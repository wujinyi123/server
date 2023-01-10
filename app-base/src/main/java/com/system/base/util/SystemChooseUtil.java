package com.system.base.util;

public class SystemChooseUtil {
    public static <T> T choose(T win,T linux) {
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            return win;
        } else {
            return linux;
        }
    }
}
