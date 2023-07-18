package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventSubscriber;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.gui.model.PriorityComboBoxModel;
import com.andrei.referenceproject.gui.model.TodoTableModel;
import com.andrei.referenceproject.task.*;
import com.github.lgooddatepicker.tableeditors.DateTableEditor;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.andrei.referenceproject.exception.ExceptionMessages.TODO_EXISTED_NAME_VALUES_MESSAGE;
import static com.andrei.referenceproject.gui.model.TodoTableModel.COLUMN_INDEX_PRIORITY;

public class MainFrame extends JFrame {
    private static final String FRAME_TITLE = "Reference project";
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private final Map<EventType, EventSubscriber> eventSubscribers = new HashMap<>();
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
    private PriorityComboBoxModel prioritiesModel;

    public MainFrame() {
        initPanel();
        loadPriorities();
        loadTodoTableData();
        addListeners();
        addSubscribers();
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

    private void loadPriorities() {
        GetAllPriorityTask getAllPriorityTask = TaskFactory.getGetAllPriorityTask();
        getAllPriorityTask.execute(new ArrayList<>(), new TaskListener<>() {
            @Override
            public void onSuccess(List<Priority> priorities) {
                prioritiesModel = new PriorityComboBoxModel(priorities);
                priorityComboBox.setModel(prioritiesModel);
            }
        });
    }

    private void loadTodoTableData() {
        GetAllTodoTask getAllTodoTask = TaskFactory.getGetAllTodoTask();
        getAllTodoTask.execute(new ArrayList<>(), new TaskListener<>() {
            @Override
            public void onSuccess(List<Todo> todos) {
                initTable(todos);
            }
        });
    }

    private void initTable(List<Todo> todos) {
        tableModel = new TodoTableModel(todos, prioritiesModel);
        todoTable.setModel(tableModel);
        decorateTable();
        tableModel.setUpdateValueCallback(todo -> {
            UpdateTodoTask updateTodoTask = TaskFactory.getUpdateTodoTask();
            updateTodoTask.execute(todo, new TaskListener<>() {
                @Override
                public void onFailure(Exception e) {
                    FindTodoTask findTodoTask = TaskFactory.getFindTodoTask();
                    findTodoTask.execute(todo, createFindTodoTaskListener());
                    JOptionPane.showMessageDialog(MainFrame.this, TODO_EXISTED_NAME_VALUES_MESSAGE);
                }
            });
        });
    }

    private TaskListener<Todo> createFindTodoTaskListener() {
        return new TaskListener<>() {
            @Override
            public void onSuccess(Todo todo) {
                tableModel.updateRow(todo);
            }
        };
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
        addButton.addActionListener(e -> new TodoFrame());
    }

    private void addEditButtonListener() {
        editButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            if (selectedRow != -1) {
                Todo todo = tableModel.getSelectedTodo(selectedRow);
                new TodoFrame(todo);
            }
        });
    }

    private void addDeleteButtonListener() {
        deleteButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            if (selectedRow != -1) {
                Todo todoToDelete = tableModel.getSelectedTodo(selectedRow);
                DeleteTodoTask deleteTodoTask = TaskFactory.getDeleteTodoTask();
                deleteTodoTask.execute(todoToDelete.getId());
            }
        });
    }

    private void addMoveUpButtonListener() {
        moveUpButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            int rowTo = selectedRow - 1;
            if (selectedRow > 0) {
                List<Todo> todoList = new ArrayList<>();
                todoList.add(tableModel.getSelectedTodo(selectedRow));
                todoList.add(tableModel.getSelectedTodo(rowTo));
                SwapTodoTask swapTodoTask = TaskFactory.getSwapTodoTask();
                swapTodoTask.execute(todoList);
                todoTable.changeSelection(rowTo, 0, false, false);
            }
        });
    }

    private void addMoveDownButtonListener() {
        moveDownButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            int rowTo = selectedRow + 1;
            if (rowTo < tableModel.getRowCount() && selectedRow != -1) {
                List<Todo> todoList = new ArrayList<>();
                todoList.add(tableModel.getSelectedTodo(selectedRow));
                todoList.add(tableModel.getSelectedTodo(rowTo));
                SwapTodoTask swapTodoTask = TaskFactory.getSwapTodoTask();
                swapTodoTask.execute(todoList);
                todoTable.changeSelection(rowTo, 0, false, false);
            }
        });
    }

    private void addPriorityButtonListener() {
        priorityButton.addActionListener(e -> new PriorityFrame());
    }

    private void addSubscribers() {
        eventSubscribers.put(EventType.CREATE_TODO, data -> tableModel.addRow((Todo) data));
        eventSubscribers.put(EventType.UPDATE_TODO, data -> tableModel.updateRow((Todo) data));
        eventSubscribers.put(EventType.DELETE_TODO, data -> tableModel.deleteRow((Long) data));
        eventSubscribers.put(EventType.SWAP_TODO, data -> tableModel.swapRow((List<Todo>) data));
        eventSubscribers.put(EventType.CREATE_PRIORITY, data -> prioritiesModel.addPriority((Priority) data));
        eventSubscribers.put(EventType.UPDATE_PRIORITY, data -> {
            prioritiesModel.updatePriority((Priority) data);
            tableModel.fireTableDataChanged();
        });
        eventSubscribers.put(EventType.DELETE_PRIORITY, data -> prioritiesModel.deletePriority((Long) data));
        EventPublisher.subscribe(eventSubscribers);
    }
}