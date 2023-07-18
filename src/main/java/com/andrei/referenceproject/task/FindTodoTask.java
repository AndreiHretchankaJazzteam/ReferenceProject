package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindTodoTask extends AbstractTask<Todo> {
    private final TodoService todoService;

    @Override
    protected Todo perform(Object data) {
        Todo todo = (Todo) data;
        return todoService.findTodoById(todo.getId());
    }
}
