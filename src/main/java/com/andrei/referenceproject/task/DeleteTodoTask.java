package com.andrei.referenceproject.task;

import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTodoTask extends AbstractTask<Long> {
    private final TodoService todoService;

    @Override
    protected EventType getEventType() {
        return EventType.DELETE_TODO;
    }

    @Override
    protected Long perform(Object data) {
        Long id = (Long) data;
        todoService.deleteTodo(id);
        return id;
    }
}
