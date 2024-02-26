package com.pers.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GenerateNumberCardUtil {
    public static Integer generateNumber() {
        return (int) (Math.random() * 10000 + Math.random() * 1000);
    }
}
