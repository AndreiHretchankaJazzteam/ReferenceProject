package com.andrei.referenceproject;

import com.andrei.referenceproject.activemq.MessageProducer;
import com.andrei.referenceproject.gui.frame.MainFrame;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ContextRefreshedApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationInformation.setApplicationId(UUID.randomUUID().toString());
        MessageProducer.setMessageProducer(event.getApplicationContext().getBean(JmsTemplate.class));
        new MainFrame();
    }
}
