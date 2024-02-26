package com.pers.util;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class GenerateNumberCardUtil {
    Set<Integer> numbersOfCards = new HashSet<>();

    public static Integer generateNumber() {
        int number = (int) (Math.random() * 10907 + Math.random() * 1000);
        if (numbersOfCards.add(number)) {
            return number;
        } else {
            int newNumber = number + 1;
            numbersOfCards.add(newNumber);
            return newNumber;
        }
    }
}
