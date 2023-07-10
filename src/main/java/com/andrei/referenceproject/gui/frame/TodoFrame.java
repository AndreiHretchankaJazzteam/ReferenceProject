package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.gui.model.PriorityComboBoxModel;
import com.andrei.referenceproject.gui.model.PriorityComboBoxModelItem;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class TodoFrame extends JFrame {
    private static final String FRAME_TITLE = "Task creator";
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 500;
    private List<Priority> priorities = new ArrayList<>();

    private JPanel rootPanel;
    private JButton acceptButton;
    private JTextArea descriptionTextArea;

    private JTextField nameTextField;
    private PriorityComboBoxModel prioritiesModel;
    private JComboBox<PriorityComboBoxModelItem> priorityComboBox;
    private JButton dateButton;


    private Todo todoToUpdate;


    public TodoFrame() {
        initFrame();
    }

    public TodoFrame(Todo todo) {
        this.todoToUpdate = todo;
        initFrame();
    }

    private void initFrame() {
        setTitle(FRAME_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setVisible(true);

        generatePriorityList();
        prioritiesModel = new PriorityComboBoxModel(priorities);
        priorityComboBox.setModel(prioritiesModel);
        initFields();
    }

    private void initFields() {
        DatePicker datePicker = new DatePicker();
        datePicker.setDate(LocalDate.now());
        dateButton.add(datePicker);
        if (todoToUpdate != null) {
            nameTextField.setText(todoToUpdate.getName());
            descriptionTextArea.setText(todoToUpdate.getDescription());
            datePicker.setDate(todoToUpdate.getDate());
            priorityComboBox.setSelectedItem(prioritiesModel.getPriorityComboBoxModelItem(todoToUpdate.getPriority()));
        }

    }

    private void generatePriorityList() {
        for (int i = 0; i < 5; i++) {
            Priority priority = new Priority();
            priority.setName("1" + i);
            Integer weight = Integer.valueOf(1) + i;
            priority.setWeight(weight);
            priorities.add(priority);
        }
    }
}
