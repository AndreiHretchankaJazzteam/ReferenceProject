package com.andrei.referenceproject;


import com.andrei.referenceproject.gui.frame.MainFrame;
import com.andrei.referenceproject.service.PriorityService;
import com.andrei.referenceproject.service.TodoService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(Application.class)
                .headless(false)
                .run(args);
        new MainFrame(context.getBean(TodoService.class), context.getBean(PriorityService.class));
    }
}