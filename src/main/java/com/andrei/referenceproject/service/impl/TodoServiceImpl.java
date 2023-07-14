package com.andrei.referenceproject.service.impl;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.ComponentNotFoundException;
import com.andrei.referenceproject.repository.TodoRepository;
import com.andrei.referenceproject.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.andrei.referenceproject.exception.ExceptionMessages.TODO_EXISTED_NAME_VALUES_MESSAGE;
import static com.andrei.referenceproject.exception.ExceptionMessages.TODO_NOT_FOUND_MESSAGE;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Todo saveTodo(Todo todo) {
        try {
            Long maxOrderNumber = todoRepository.getMaxOrderNumber();
            if (maxOrderNumber == null) {
                maxOrderNumber = 0L;
            }
            todo.setOrderNumber(maxOrderNumber + 1);
            return todoRepository.save(todo);
        } catch (DataIntegrityViolationException e) {
            throw new ComponentExistedValuesException(TODO_EXISTED_NAME_VALUES_MESSAGE);
        }
    }

    @Override
    public List<Todo> findAllTodos() {
        return todoRepository.findAllByOrderByOrderNumberAsc();
    }

    @Override
    public Todo updateTodo(Long id, Todo todo) {
        try {
            todo.setOrderNumber(findTodoById(id).getOrderNumber());
            todo.setId(id);
            return todoRepository.save(todo);
        } catch (DataIntegrityViolationException e) {
            throw new ComponentExistedValuesException(TODO_EXISTED_NAME_VALUES_MESSAGE);
        }
    }

    @Override
    public void swapTodo(Todo todo1, Todo todo2) {
        Long firstOrderNumber = todo1.getOrderNumber();
        Long secondOrderNumber = todo2.getOrderNumber();
        todo1.setOrderNumber(secondOrderNumber);
        todo2.setOrderNumber(firstOrderNumber);
        todoRepository.save(todo1);
        todoRepository.save(todo2);
    }

    @Override
    public void deleteTodo(Long id) {
        findTodoById(id);
        todoRepository.deleteById(id);
    }

    @Override
    public Todo findTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException(String.format(TODO_NOT_FOUND_MESSAGE, id)));
    }
}
