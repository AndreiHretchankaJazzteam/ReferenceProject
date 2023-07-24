package com.andrei.referenceproject.task;

import com.andrei.referenceproject.service.PriorityService;
import com.andrei.referenceproject.service.TodoService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskFactory {
    @Autowired
    private TodoService todoService;
    @Autowired
    private PriorityService priorityService;

    @Getter
    private static CreatePriorityTask createPriorityTask;
    @Getter
    private static DeletePriorityTask deletePriorityTask;
    @Getter
    private static UpdatePriorityTask updatePriorityTask;
    @Getter
    @Autowired
    private static GetAllPriorityTask getAllPriorityTask;
    @Getter
    private static GetAllTodoTask getAllTodoTask;
    @Getter
    private static UpdateTodoTask updateTodoTask;
    @Getter
    private static FindTodoTask findTodoTask;
    @Getter
    private static DeleteTodoTask deleteTodoTask;
    @Getter
    private static SwapTodoTask swapTodoTask;
    @Getter
    private static CreateTodoTask createTodoTask;

    @PostConstruct
    public void initTaskFactory() {
        createPriorityTask = new CreatePriorityTask(priorityService);
        deletePriorityTask = new DeletePriorityTask(priorityService);
        updatePriorityTask = new UpdatePriorityTask(priorityService);
        getAllPriorityTask = new GetAllPriorityTask(priorityService);
        getAllTodoTask = new GetAllTodoTask(todoService);
        updateTodoTask = new UpdateTodoTask(todoService);
        findTodoTask = new FindTodoTask(todoService);
        deleteTodoTask = new DeleteTodoTask(todoService);
        swapTodoTask = new SwapTodoTask(todoService);
        createTodoTask = new CreateTodoTask(todoService);
    }
}
