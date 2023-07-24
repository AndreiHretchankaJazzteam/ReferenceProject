package com.andrei.referenceproject;

import com.andrei.referenceproject.activemq.MessageProducer;
import com.andrei.referenceproject.gui.frame.MainFrame;
import com.andrei.referenceproject.task.AbstractTask;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!withoutActiveMQ")
public class ContextRefreshedApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        context.getBean(MessageProducer.class).initMessageProducer(context.getBean(JmsTemplate.class));
        AbstractTask.setProfile("default");
        new MainFrame();
    }
}
