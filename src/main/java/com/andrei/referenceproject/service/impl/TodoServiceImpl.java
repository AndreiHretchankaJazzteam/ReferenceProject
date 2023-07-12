package com.andrei.referenceproject.service.impl;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.ComponentNotFoundException;
import com.andrei.referenceproject.service.TodoService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.andrei.referenceproject.exception.ExceptionMessages.*;

public class TodoServiceImpl implements TodoService {
    private static Long i = 1L;
    private final List<Todo> todos;

    public TodoServiceImpl(List<Todo> todos) {
        this.todos = todos;
    }

    @Override
    public Todo saveTodo(Todo todo) {
        todos.stream()
                .filter(t -> t.getName().equals(todo.getName()))
                .findFirst()
                .ifPresent(t -> {
                    throw new ComponentExistedValuesException(TODO_EXISTED_NAME_VALUES_MESSAGE);
                });
        todo.setId(i);
        i++;
        todos.add(todo);
        return todo;
    }

    @Override
    public List<Todo> findAllTodos() {
        return todos;
    }

    @Override
    public Todo updateTodo(Long id, Todo todo) {
        todos.stream()
                .filter(t -> t.getName().equals(todo.getName()) && !Objects.equals(t.getId(), id))
                .findFirst()
                .ifPresent(t -> {
                    throw new ComponentExistedValuesException(TODO_EXISTED_NAME_VALUES_MESSAGE);
                });
        todo.setId(id);
        todos.stream().
                filter(t -> Objects.equals(t.getId(), todo.getId()))
                .findFirst()
                .ifPresent(t -> {
                    int index = todos.indexOf(t);
                    todos.set(index, todo);
                });
        return todo;
    }

    @Override
    public void swapTodo(int index1, int index2) {
        Collections.swap(this.todos, index1, index2);
    }

    @Override
    public void deleteTodo(Long id) {
        todos.stream().
                filter(t -> Objects.equals(t.getId(), id))
                .findFirst()
                .ifPresent(todos::remove);
    }

    @Override
    public Todo findTodoById(Long id) {
        return todos.stream()
                .filter(t -> Objects.equals(t.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ComponentNotFoundException(String.format(TODO_NOT_FOUND_MESSAGE, id)));
    }
}
