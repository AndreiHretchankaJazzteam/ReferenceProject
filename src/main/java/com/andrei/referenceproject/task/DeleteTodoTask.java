package com.andrei.referenceproject.task;

import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTodoTask extends AbstractTask<Long> {
    private final TodoService todoService;
    private final EventPublisher eventPublisher;

    @Override
    protected Long perform(Object data) {
        Long id = (Long) data;
        todoService.deleteTodo(id);
        eventPublisher.notifySubscribers(EventType.DELETE_TODO, id);
        return id;
    }
}
