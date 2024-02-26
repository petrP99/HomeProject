package com.pers.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class ExpireDateUtil {
    private final int amountOfYear = 5;
    public static LocalDate calculateExpireDate() {
        return LocalDate.now().plusYears(amountOfYear);
    }
}
