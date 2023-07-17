package com.andrei.referenceproject.event;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventPublisher {
    private final Map<EventType, List<EventSubscriber>> subscribers = new HashMap<>();

    public void subscribe(EventType eventType, EventSubscriber eventSubscriber) {
        List<EventSubscriber> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers != null) {
            eventSubscribers.add(eventSubscriber);
        } else {
            eventSubscribers = new ArrayList<>();
            eventSubscribers.add(eventSubscriber);
            subscribers.put(eventType, eventSubscribers);
        }
    }

    public void unsubscribe(EventType eventType, EventSubscriber eventSubscriber) {
        subscribers.get(eventType).remove(eventSubscriber);
    }

    public void subscribe(Map<EventType, EventSubscriber> eventSubscribers) {
        eventSubscribers.forEach(this::subscribe);
    }

    public void unsubscribe(Map<EventType, EventSubscriber> eventSubscribers) {
        eventSubscribers.forEach(this::unsubscribe);
    }

    public void notifySubscribers(EventType eventType, Object data) {
        subscribers.get(eventType).forEach(eventSubscriber -> eventSubscriber.update(data));
    }
}
