package com.andrei.referenceproject.task;

import com.andrei.referenceproject.event.EventType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractTask<T> {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    abstract EventType getEventType();

    protected abstract T perform(Object data);

    public void execute(Object data) {
        execute(data, TaskListener.createDefaultListener());
    }

    public void execute(Object data, TaskListener<T> listener) {
        executorService.execute(() -> {
            try {
                T performed = perform(data);
                listener.notifySubscribers(getEventType(), performed);
                listener.onSuccess(performed);
            } catch (Exception e) {
                listener.onFailure(e);
            }
        });
    }
}
