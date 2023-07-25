package com.andrei.referenceproject;

import com.andrei.referenceproject.gui.frame.MainFrame;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class LoadUI implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        new MainFrame();
    }
}
