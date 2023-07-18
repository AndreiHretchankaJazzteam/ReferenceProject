package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SwapTodoTask extends AbstractTask<List<Todo>> {
    private final TodoService todoService;
    private final EventPublisher eventPublisher;

    @Override
    protected List<Todo> perform(Object data) {
        List<Todo> todoList = (List<Todo>) data;
        todoService.swapTodo(todoList.get(0), todoList.get(1));
        List<Integer> indices = new ArrayList<>();
        indices.add(todoList.get(0).getOrderNumber().intValue() - 1);
        indices.add(todoList.get(1).getOrderNumber().intValue() - 1);
        Collections.sort(indices);
        eventPublisher.notifySubscribers(EventType.SWAP_TODO, indices);
        return todoList;
    }
}
