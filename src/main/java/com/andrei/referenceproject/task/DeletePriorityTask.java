package com.andrei.referenceproject.task;

import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletePriorityTask extends AbstractTask<Long> {
    private final PriorityService priorityService;

    @Override
    protected Long perform(Object data) {
        Long id = (Long) data;
        priorityService.deletePriority(id);
        return id;
    }

    @Override
    EventType getEventType() {
        return EventType.DELETE_PRIORITY;
    }
}
