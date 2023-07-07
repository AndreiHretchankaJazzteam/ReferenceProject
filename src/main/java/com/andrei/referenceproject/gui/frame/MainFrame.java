package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.gui.model.TodoTableModel;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private static final String FRAME_TITLE = "Reference project";
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private JPanel rootPanel;
    private JTable todoTable;
    private JButton addButton;
    private JButton priorityButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton moveUpButton;
    private JButton moveDownButton;

    public MainFrame() {
        setTitle(FRAME_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setVisible(true);
        List<Todo> todos = generateTodoList();

        loadTableData(todos);
        addListeners();
    }

    private void loadTableData(List<Todo> todos) {

        TodoTableModel tableModel = new TodoTableModel(todos);
        todoTable.setModel(tableModel);


    }

    private void addListeners() {
        addAddButtonListener();
        addPriorityButtonListener();
    }

    private void addAddButtonListener() {
        addButton.addActionListener(e -> new TodoFrame());
    }

    private void addPriorityButtonListener() {
        priorityButton.addActionListener(e -> new PriorityFrame());
    }


    private List<Todo> generateTodoList() {
        List<Todo> todos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Todo todo = new Todo();
            Priority priority = new Priority();
            priority.setName("1" + i);
            Integer weight = Integer.valueOf(1) + i;
            priority.setWeight(weight);
            todo.setName("1" + i);
            todo.setDescription("1" + i);
            todo.setPriority(priority.getName());
            todo.setDate(LocalDate.now());
            todos.add(todo);
        }
        return todos;
    }


}
