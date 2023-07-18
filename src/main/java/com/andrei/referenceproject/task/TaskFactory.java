package com.andrei.referenceproject.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class TaskFactory {
    private final CreatePriorityTask createPriorityTask;
    private final DeletePriorityTask deletePriorityTask;
    private final UpdatePriorityTask updatePriorityTask;
    private final GetAllPriorityTask getAllPriorityTask;
    private final GetAllTodoTask getAllTodoTask;
    private final UpdateTodoTask updateTodoTask;
    private final FindTodoTask findTodoTask;
    private final DeleteTodoTask deleteTodoTask;
    private final SwapTodoTask swapTodoTask;
    private final CreateTodoTask createTodoTask;

}
