package com.andrei.referenceproject;

import java.util.UUID;

public class ApplicationInformation {
    private static final String applicationId = UUID.randomUUID().toString();

    public static String getApplicationId() {
        return applicationId;
    }
}
