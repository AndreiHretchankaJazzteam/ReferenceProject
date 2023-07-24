package com.andrei.referenceproject.task;

import com.andrei.referenceproject.event.EventType;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.andrei.referenceproject.activemq.ActiveMQConstants.PRIORITY_TOPIC;
import static com.andrei.referenceproject.activemq.ActiveMQConstants.TODO_TOPIC;

public abstract class AbstractTask<T> {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private static final String PROFILE_NAME_DEFAULT = "default";
    private static final String PROFILE_NAME_WITHOUT_ACTIVE_MQ = "withoutActiveMQ";
    private static String profile;

    protected abstract EventType getEventType();

    protected abstract T perform(Object data);

    public void execute(Object data) {
        execute(data, TaskListener.createDefaultListener());
    }

    public void execute(Object data, TaskListener<T> listener) {
        executorService.execute(() -> {
            try {
                T performed = perform(data);
                if (getEventType() != null) {
                    if (Objects.equals(profile, PROFILE_NAME_WITHOUT_ACTIVE_MQ)) {
                        listener.notifySubscribers(performed, getEventType());
                    }
                    if (Objects.equals(profile, PROFILE_NAME_DEFAULT)) {
                        listener.notifySubscribers(getTopicName(), performed, getEventType());
                    }
                }
                listener.onSuccess(performed);
            } catch (Exception e) {
                listener.onFailure(e);
            }
        });
    }

    private String getTopicName() {
        switch (getEventType()) {
            case CREATE_PRIORITY, UPDATE_PRIORITY, DELETE_PRIORITY -> {
                return PRIORITY_TOPIC;
            }
            case CREATE_TODO, UPDATE_TODO, DELETE_TODO, SWAP_TODO -> {
                return TODO_TOPIC;
            }
            default -> {
                assert false : "add handling for type";
            }
        }
        return null;
    }

    public static void setProfile(String profileName) {
        profile = profileName;
    }
}
