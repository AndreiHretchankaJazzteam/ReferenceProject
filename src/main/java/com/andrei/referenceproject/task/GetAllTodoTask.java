package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllTodoTask extends AbstractTask<List<Todo>> {
    private final TodoService todoService;

    @Override
    protected List<Todo> perform(Object data) {
        return todoService.findAllTodos();
    }
}
