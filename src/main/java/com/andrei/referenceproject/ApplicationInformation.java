package com.andrei.referenceproject;

public class ApplicationInformation {
    private static String applicationId;

    public static String getApplicationId() {
        return applicationId;
    }

    public static void setApplicationId(String id) {
        if (applicationId == null) {
            applicationId = id;
        }
    }
}
