package com.andrei.referenceproject.task;

import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.gui.frame.MainFrame;

import javax.swing.*;

import static com.andrei.referenceproject.exception.ExceptionMessages.DEFAULT_ERROR_MESSAGE;

public interface TaskListener<T> {
    default void onSuccess(T t) {

    }

    default void onFailure(Exception e) {
        JOptionPane.showMessageDialog(MainFrame.getWindows()[0], DEFAULT_ERROR_MESSAGE);
    }

    default void notifySubscribers(T performed, EventType eventType) {
        EventPublisher.notifySubscribers(eventType, performed);
    }

    static TaskListener createDefaultListener() {
        return new TaskListener() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }
}
