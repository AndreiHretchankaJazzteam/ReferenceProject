package com.andrei.referenceproject;


import com.andrei.referenceproject.activemq.MessageProducer;
import com.andrei.referenceproject.gui.frame.MainFrame;
import com.andrei.referenceproject.task.TaskFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
        ApplicationContext context = new SpringApplicationBuilder(Application.class)
                .headless(false)
                .run(args);
        context.getBean(TaskFactory.class).initTaskFactory();
        context.getBean(MessageProducer.class).initMessageProducer(context.getBean(JmsTemplate.class));
        new MainFrame();
        new MainFrame();
    }
}