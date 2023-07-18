package com.andrei.referenceproject.task;

public interface TaskListener<T> {
    void onSuccess(T t);
    void onFailure(Exception e);
}
