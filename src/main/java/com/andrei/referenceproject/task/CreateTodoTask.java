package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateTodoTask extends AbstractTask<Todo> {
    private final TodoService todoService;

    @Override
    protected Todo perform(Object data) {
        Todo todo = (Todo) data;
        todo = todoService.saveTodo(todo);
        return todo;
    }

    @Override
    protected EventType getEventType() {
        return EventType.CREATE_TODO;
    }

}
