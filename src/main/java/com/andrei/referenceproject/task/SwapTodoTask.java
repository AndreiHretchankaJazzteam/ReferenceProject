package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SwapTodoTask extends AbstractTask<List<Todo>> {
    private final TodoService todoService;

    @Override
    protected EventType getEventType() {
        return EventType.SWAP_TODO;
    }

    @Override
    protected List<Todo> perform(Object data) {
        List<Todo> todoList = (List<Todo>) data;
        todoService.swapTodo(todoList.get(0), todoList.get(1));
        return todoList;
    }
}
