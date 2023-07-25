package com.andrei.referenceproject.activemq;

import com.andrei.referenceproject.ApplicationInformation;
import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventType;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Session;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import java.io.Serializable;

import static com.andrei.referenceproject.activemq.ActiveMQConstants.APPLICATION_ID_PROPERTY;
import static com.andrei.referenceproject.activemq.ActiveMQConstants.EVENT_TYPE_PROPERTY;


public class MessageProducer {
    private static JmsTemplate jmsTemplate;

    public static void sendMessage(String topic, Serializable object, EventType eventType) {
        EventPublisher.notifySubscribers(eventType, object);
        if (jmsTemplate != null) {
            jmsTemplate.send(topic, createMessage(object, eventType));
        }
    }

    private static MessageCreator createMessage(Serializable object, EventType eventType) {
        return new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(object);
                message.setStringProperty(EVENT_TYPE_PROPERTY, eventType.name());
                message.setStringProperty(APPLICATION_ID_PROPERTY, ApplicationInformation.getApplicationId());
                return message;
            }
        };
    }

    public static void setMessageProducer(JmsTemplate jmsTemplateBean) {
        if (jmsTemplate == null) {
            jmsTemplate = jmsTemplateBean;
        }
    }
}