package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateTodoTask extends AbstractTask<Todo> {
    private final TodoService todoService;
    private final EventPublisher eventPublisher;

    @Override
    protected Todo perform(Object data) {
        Todo todo = (Todo) data;
        todo = todoService.updateTodo(todo.getId(), todo);
        eventPublisher.notifySubscribers(EventType.UPDATE_TODO, todo);
        return todo;
    }
}
