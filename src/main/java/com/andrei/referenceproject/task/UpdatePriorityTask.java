package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePriorityTask extends AbstractTask<Priority> {
    private final PriorityService priorityService;

    @Override
    protected EventType getEventType() {
        return EventType.UPDATE_PRIORITY;
    }

    @Override
    protected Priority perform(Object data) {
        Priority priority = (Priority) data;
        priority = priorityService.updatePriority(priority.getId(), priority);
        return priority;
    }
}
