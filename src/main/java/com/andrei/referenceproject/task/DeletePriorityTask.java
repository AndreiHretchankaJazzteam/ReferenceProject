package com.andrei.referenceproject.task;

import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.andrei.referenceproject.activemq.ActiveMQConstants.PRIORITY_TOPIC;

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
    protected EventType getEventType() {
        return EventType.DELETE_PRIORITY;
    }

    @Override
    protected String getTopicName() {
        return PRIORITY_TOPIC;
    }
}
