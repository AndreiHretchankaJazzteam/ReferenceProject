package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.gui.model.PriorityComboBoxModel;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;


public class TodoFrame extends JFrame {
    private static final String FRAME_TITLE_CREATE = "Create Todo";
    private static final String FRAME_TITLE_EDIT = "Edit Todo";
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 500;
    private final List<Priority> priorities;
    private JPanel rootPanel;
    private JButton acceptButton;
    private JTextArea descriptionTextArea;
    private JTextField nameTextField;
    private PriorityComboBoxModel prioritiesModel;
    private JComboBox<Priority> priorityComboBox;
    private JButton dateButton;
    private Todo todoToUpdate;

    public TodoFrame(List<Priority> priorities) {
        this.priorities = priorities;
        initFrame();
    }

    public TodoFrame(Todo todo, List<Priority> priorities) {
        this.priorities = priorities;
        this.todoToUpdate = todo;
        initFrame();
    }

    private void initFrame() {
        initPanel();
        prioritiesModel = new PriorityComboBoxModel(priorities);
        priorityComboBox.setModel(prioritiesModel);
        initFields();
    }

    private void initPanel() {
        if (todoToUpdate == null) {
            setTitle(FRAME_TITLE_CREATE);
        } else {
            setTitle(FRAME_TITLE_EDIT);
        }
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setVisible(true);
    }

    private void initFields() {
        DatePicker datePicker = new DatePicker();
        datePicker.setDate(LocalDate.now());
        dateButton.add(datePicker);
        if (todoToUpdate != null) {
            nameTextField.setText(todoToUpdate.getName());
            descriptionTextArea.setText(todoToUpdate.getDescription());
            datePicker.setDate(todoToUpdate.getDate());
            priorityComboBox.setSelectedItem(todoToUpdate.getPriority());
        }
    }
}
