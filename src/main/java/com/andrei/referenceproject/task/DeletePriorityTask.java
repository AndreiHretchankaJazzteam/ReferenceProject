package com.andrei.referenceproject.task;

import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletePriorityTask extends AbstractTask<Long> {
    private final PriorityService priorityService;
    private final EventPublisher eventPublisher;
    @Override
    protected Long perform(Object data) {
        Long id = (Long) data;
        priorityService.deletePriority(id);
        eventPublisher.notifySubscribers(EventType.DELETE_PRIORITY, id);
        return id;
    }
}
