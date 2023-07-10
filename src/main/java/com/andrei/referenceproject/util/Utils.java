package com.andrei.referenceproject.util;

public class Utils {
    public static <T> boolean equalsForNullableField(T field1, T field2) {
        if (field1 == null && field2 == null) {
            return true;
        }
        if (field1 == null && field2 != null) {
            return false;
        }
        if (field1 != null && field2 == null) {
            return false;
        }
        return field1.equals(field2);
    }
}
