package com.andrei.referenceproject.activemq;

import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventType;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static com.andrei.referenceproject.activemq.ActiveMQConstants.*;

@Component
@Profile("!withoutActiveMQ")
@RequiredArgsConstructor
public class ActiveMQConsumer implements MessageListener {

    @Override
    @JmsListener(destination = TODO_TOPIC)
    @JmsListener(destination = PRIORITY_TOPIC)
    public void onMessage(Message message) {
        try {
            String eventTypeString = message.getStringProperty(EVENT_TYPE_PROPERTY);
            EventType eventType = EventType.valueOf(eventTypeString);
            ObjectMessage objectMessage = (ObjectMessage) message;
            Serializable object = objectMessage.getObject();
            EventPublisher.notifySubscribers(eventType, object);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}