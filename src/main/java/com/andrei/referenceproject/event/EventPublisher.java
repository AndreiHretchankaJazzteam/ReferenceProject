package com.andrei.referenceproject.event;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventPublisher {
    private static final Map<EventType, List<EventSubscriber>> subscribers = new HashMap<>();

    public static void subscribe(EventType eventType, EventSubscriber eventSubscriber) {
        List<EventSubscriber> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers != null) {
            eventSubscribers.add(eventSubscriber);
        } else {
            eventSubscribers = new ArrayList<>();
            eventSubscribers.add(eventSubscriber);
            subscribers.put(eventType, eventSubscribers);
        }
    }

    public static void unsubscribe(EventType eventType, EventSubscriber eventSubscriber) {
        subscribers.get(eventType).remove(eventSubscriber);
    }

    public static void subscribe(Map<EventType, EventSubscriber> eventSubscribers) {
        eventSubscribers.forEach(EventPublisher::subscribe);
    }

    public static void unsubscribe(Map<EventType, EventSubscriber> eventSubscribers) {
        eventSubscribers.forEach(EventPublisher::unsubscribe);
    }

    public static void notifySubscribers(EventType eventType, Object data) {
        List<EventSubscriber> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers != null) {
            eventSubscribers.forEach(eventSubscriber -> eventSubscriber.update(data));
        }
    }
}
