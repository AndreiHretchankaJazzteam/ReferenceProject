package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.gui.model.PriorityComboBoxModel;
import com.andrei.referenceproject.gui.model.PriorityComboBoxModelItem;
import com.andrei.referenceproject.gui.model.TodoTableModel;
import com.github.lgooddatepicker.tableeditors.DateTableEditor;

import javax.swing.*;
import javax.swing.table.TableColumnModel;

import java.time.LocalDate;
import java.util.*;

import static com.andrei.referenceproject.gui.model.TodoTableModel.COLUMN_INDEX_PRIORITY;

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

    private final List<Todo> todos = new ArrayList<>();
    private final List<Priority> priorities = new ArrayList<>();
    private final JComboBox<PriorityComboBoxModelItem> priorityComboBox = new JComboBox<>();

    public MainFrame() {
        setTitle(FRAME_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setVisible(true);

        generatePriorityAndTodoList();
        initTableData();
        addListeners();
    }

    private void initTableData() {
        PriorityComboBoxModel prioritiesModel = new PriorityComboBoxModel(priorities);
        priorityComboBox.setModel(prioritiesModel);
        TodoTableModel tableModel = new TodoTableModel(todos, prioritiesModel);
        todoTable.setModel(tableModel);
        decorateTable();
    }

    private void decorateTable() {
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.setDefaultRenderer(LocalDate.class, new DateTableEditor());
        todoTable.setDefaultEditor(LocalDate.class, new DateTableEditor());
        TableColumnModel columnModel = todoTable.getColumnModel();
        columnModel.getColumn(COLUMN_INDEX_PRIORITY).setCellEditor(new DefaultCellEditor(priorityComboBox));
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


    private void generatePriorityAndTodoList() {
        for (int i = 0; i < 5; i++) {
            Todo todo = new Todo();
            Priority priority = new Priority();
            priority.setName("1" + i);
            Integer weight = Integer.valueOf(1) + i;
            priority.setWeight(weight);
            todo.setName("1" + i);
            todo.setDescription("1" + i);
            todo.setPriority(priority);
            todo.setDate(LocalDate.now());
            todos.add(todo);
            priorities.add(priority);
        }
    }
}
