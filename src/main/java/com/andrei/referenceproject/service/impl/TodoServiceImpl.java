package com.andrei.referenceproject.service.impl;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.exception.ActionWithNonActualElementException;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.ComponentNotFoundException;
import com.andrei.referenceproject.repository.TodoRepository;
import com.andrei.referenceproject.service.PriorityService;
import com.andrei.referenceproject.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.andrei.referenceproject.exception.ExceptionMessages.*;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final PriorityService priorityService;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository, PriorityService priorityService) {
        this.todoRepository = todoRepository;
        this.priorityService = priorityService;
    }

    @Override
    public Todo saveTodo(Todo todo) {
        try {
            Long maxOrderNumber = todoRepository.getMaxOrderNumber();
            if (maxOrderNumber == null) {
                maxOrderNumber = 0L;
            }
            todo.setOrderNumber(maxOrderNumber + 1);
            if (todo.getPriority() != null) {
                priorityService.findPriorityById(todo.getPriority().getId());
            }
            return todoRepository.save(todo);
        } catch (ComponentNotFoundException e) {
            throw new ComponentNotFoundException(PRIORITY_NOT_FOUND_MESSAGE);
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
        Todo todoInDb = findTodoById(id);
        try {
            todo.setOrderNumber(todoInDb.getOrderNumber());
            todo.setId(id);
            if (todo.getPriority() != null) {
                priorityService.findPriorityById(todo.getPriority().getId());
            }
            return todoRepository.save(todo);
        } catch (ComponentNotFoundException e) {
            throw new ComponentNotFoundException(PRIORITY_NOT_FOUND_MESSAGE);
        } catch (DataIntegrityViolationException e) {
            throw new ComponentExistedValuesException(TODO_EXISTED_NAME_VALUES_MESSAGE);
        }
    }

    @Override
    public void swapTodo(Todo todo1, Todo todo2) {
        Todo todo1InDb = findTodoById(todo1.getId());
        Todo todo2InDb = findTodoById(todo2.getId());
        if (!Objects.equals(todo1InDb.getOrderNumber(), todo1.getOrderNumber()) || !Objects.equals(todo2InDb.getOrderNumber(), todo2.getOrderNumber())) {
            throw new ActionWithNonActualElementException(ACTION_WITH_NON_ACTUAL_ELEMENT);
        }
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
