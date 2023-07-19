package com.andrei.referenceproject.task;

import com.andrei.referenceproject.event.EventType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractTask<T> {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    protected EventType getEventType() {
        return null;
    }

    protected String getTopicName() {
        return null;
    }

    protected abstract T perform(Object data);

    public void execute(Object data) {
        execute(data, TaskListener.createDefaultListener());
    }

    public void execute(Object data, TaskListener<T> listener) {
        executorService.execute(() -> {
            try {
                T performed = perform(data);
                listener.notifySubscribers(getTopicName(), performed, getEventType());
                listener.onSuccess(performed);
            } catch (Exception e) {
                listener.onFailure(e);
            }
        });
    }
}
