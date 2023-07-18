package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SwapTodoTask extends AbstractTask<List<Todo>> {
    private final TodoService todoService;

    @Override
    EventType getEventType() {
        return EventType.SWAP_TODO;
    }

    @Override
    protected List<Todo> perform(Object data) {
        List<Todo> todoList = (List<Todo>) data;
        todoService.swapTodo(todoList.get(0), todoList.get(1));
        todoList.sort(new Comparator<Todo>() {
            @Override
            public int compare(Todo o1, Todo o2) {
                return o2.getOrderNumber().intValue() - o1.getOrderNumber().intValue();
            }
        });
        Collections.swap(todoList, 0, 1);
        return todoList;
    }
}
