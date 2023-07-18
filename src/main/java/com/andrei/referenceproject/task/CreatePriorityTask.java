package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatePriorityTask extends AbstractTask<Priority>{
    private final PriorityService priorityService;
    private final EventPublisher eventPublisher;

    @Override
    protected Priority perform(Object data) {
        Priority priority = (Priority) data;
        priority = priorityService.savePriority(priority);
        eventPublisher.notifySubscribers(EventType.CREATE_PRIORITY, priority);
        return priority;
    }
}
