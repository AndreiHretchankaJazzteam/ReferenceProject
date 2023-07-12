package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.service.PriorityService;
import com.andrei.referenceproject.service.TodoService;
import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.gui.model.PriorityComboBoxModel;
import com.andrei.referenceproject.gui.model.TodoTableModel;
import com.andrei.referenceproject.service.impl.PriorityServiceImpl;
import com.andrei.referenceproject.service.impl.TodoServiceImpl;
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
    private final List<Todo> todos = new ArrayList<>();
    private final List<Priority> priorities = new ArrayList<>();
    private final TodoService todoService;
    private final PriorityService priorityService;
    private final JComboBox<Priority> priorityComboBox = new JComboBox<>();
    private JPanel rootPanel;
    private JTable todoTable;
    private JButton addButton;
    private JButton priorityButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private TodoTableModel tableModel;


    public MainFrame() {
        initPanel();
        generatePriorityAndTodoList();
        todoService = new TodoServiceImpl(todos);
        priorityService = new PriorityServiceImpl(priorities);
        initTableData();
        addListeners();
    }

    private void initPanel() {
        setTitle(FRAME_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setVisible(true);
    }

    private void initTableData() {
        PriorityComboBoxModel prioritiesModel = new PriorityComboBoxModel(priorityService.findAllPriorities());
        priorityComboBox.setModel(prioritiesModel);
        tableModel = new TodoTableModel(todoService.findAllTodos());
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
        addEditButtonListener();
        addDeleteButtonListener();
        addMoveUpButtonListener();
        addMoveDownButtonListener();
    }

    private void addAddButtonListener() {
        addButton.addActionListener(e -> new TodoFrame(priorities, todos, tableModel));
    }

    private void addEditButtonListener() {
        editButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            if (selectedRow != -1) {
                Todo todo = tableModel.getSelectedTodo(selectedRow);
                new TodoFrame(todo, selectedRow, priorities, todos, tableModel);
            }
        });
    }

    private void addDeleteButtonListener() {
        deleteButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            if (selectedRow != -1) {
                Todo todoToDelete = tableModel.getSelectedTodo(selectedRow);
                tableModel.deleteRow(selectedRow);
                todoService.deleteTodo(todoToDelete.getId());
            }
        });
    }

    private void addMoveUpButtonListener() {
        moveUpButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            int rowTo = selectedRow - 1;
            if (selectedRow > 0) {
                todoService.swapTodo(selectedRow, rowTo);
                tableModel.swapRow(selectedRow, rowTo);
                todoTable.changeSelection(rowTo, 0, false, false);
            }
        });
    }

    private void addMoveDownButtonListener() {
        moveDownButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            int rowTo = selectedRow + 1;
            if (rowTo < tableModel.getRowCount() && selectedRow != -1) {
                todoService.swapTodo(selectedRow, rowTo);
                tableModel.swapRow(selectedRow, rowTo);
                todoTable.changeSelection(rowTo, 0, false, false);
            }
        });
    }

    private void addPriorityButtonListener() {
        priorityButton.addActionListener(e -> new PriorityFrame(priorities, (PriorityComboBoxModel) priorityComboBox.getModel()));
    }

    private void generatePriorityAndTodoList() {
        for (int i = 0; i < 5; i++) {
            Todo todo = new Todo();
            Priority priority = new Priority();
            long id = i - 5;
            priority.setId(id);
            priority.setName("1" + i);
            Integer weight = Integer.valueOf(1) + i;
            priority.setWeight(weight);
            todo.setId(id);
            todo.setName("1" + i);
            todo.setDescription("1" + i);
            todo.setPriority(priority);
            todo.setDate(LocalDate.now());
            todos.add(todo);
            priorities.add(priority);
        }
    }
}
