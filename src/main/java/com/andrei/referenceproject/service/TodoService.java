package com.andrei.referenceproject.service;

import com.andrei.referenceproject.entity.Todo;

import java.util.List;

public interface TodoService {
    Todo saveTodo(Todo todo);

    List<Todo> findAllTodos();

    Todo updateTodo(Long id, Todo todo);

    void deleteTodo(Long id);

    void swapTodo(Todo todo1, Todo todo2);

    Todo findTodoById(Long id);
}
