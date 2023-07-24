package com.andrei.referenceproject.activemq;

import com.andrei.referenceproject.event.EventType;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static com.andrei.referenceproject.activemq.ActiveMQConstants.EVENT_TYPE_PROPERTY;

@Component
@RequiredArgsConstructor
@Profile("!withoutActiveMQ")
public class MessageProducer {
    private static JmsTemplate jmsTemplate;

    public static void sendMessage(String topic, Serializable object, EventType eventType) {
        jmsTemplate.send(topic, createMessage(object, eventType));
    }

    private static MessageCreator createMessage(Serializable object, EventType eventType) {
        return new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(object);
                message.setStringProperty(EVENT_TYPE_PROPERTY, eventType.name());
                return message;
            }
        };
    }

    public void initMessageProducer(JmsTemplate jmsTemplateBean) {
        jmsTemplate = jmsTemplateBean;
    }
}