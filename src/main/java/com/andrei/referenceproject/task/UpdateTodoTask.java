package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateTodoTask extends AbstractTask<Todo> {
    private final TodoService todoService;

    @Override
    EventType getEventType() {
        return EventType.UPDATE_TODO;
    }

    @Override
    protected Todo perform(Object data) {
        Todo todo = (Todo) data;
        todo = todoService.updateTodo(todo.getId(), todo);
        return todo;
    }
}
