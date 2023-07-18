package com.andrei.referenceproject.task;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllPriorityTask extends AbstractTask<List<Priority>> {
    private final PriorityService priorityService;

    @Override
    EventType getEventType() {
        return EventType.GET_ALL_PRIORITY;
    }

    @Override
    protected List<Priority> perform(Object data) {
        return priorityService.findAllPriorities();
    }
}
