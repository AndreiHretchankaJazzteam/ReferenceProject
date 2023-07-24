package com.andrei.referenceproject;

import com.andrei.referenceproject.gui.frame.MainFrame;
import com.andrei.referenceproject.task.AbstractTask;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile("withoutActiveMQ")
public class ContextRefreshedApplicationListenerWithoutActiveMQ implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AbstractTask.setProfile("withoutActiveMQ");
        new MainFrame();
    }
}
