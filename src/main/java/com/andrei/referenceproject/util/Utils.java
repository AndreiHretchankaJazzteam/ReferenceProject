package com.andrei.referenceproject.util;

public class Utils {
    public static boolean equalsForNullableField(Object field1, Object field2) {
        if (field1 == null && field2 == null) {
            return true;
        }
        return field1 != null && field1.equals(field2);
    }
}
