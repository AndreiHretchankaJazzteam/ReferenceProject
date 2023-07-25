package com.andrei.referenceproject.task;

import com.andrei.referenceproject.event.EventType;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.andrei.referenceproject.activemq.ActiveMQConstants.TOPIC;

public abstract class AbstractTask<T> {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    protected abstract EventType getEventType();

    protected abstract T perform(Object data);

    public void execute(Object data) {
        execute(data, TaskListener.createDefaultListener());
    }

    public void execute(Object data, TaskListener<T> listener) {
        executorService.execute(() -> {
            try {
                T performed = perform(data);
                EventType eventType = getEventType();
                EventQueue.invokeLater(() -> {
                    if (eventType != null) {
                        listener.notifySubscribers(TOPIC, performed, eventType);
                    }
                    listener.onSuccess(performed);
                });
            } catch (Exception e) {
                EventQueue.invokeLater(() -> listener.onFailure(e));
            }
        });
    }
}
