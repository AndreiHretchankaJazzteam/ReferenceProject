package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventSubscriber;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.InvalidEnteredDataException;
import com.andrei.referenceproject.gui.model.PriorityComboBoxModel;
import com.andrei.referenceproject.gui.model.TodoTableModel;
import com.andrei.referenceproject.service.PriorityService;
import com.andrei.referenceproject.service.TodoService;
import com.github.lgooddatepicker.tableeditors.DateTableEditor;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.andrei.referenceproject.exception.ExceptionMessages.INVALID_TODO_FIELDS_MESSAGE;
import static com.andrei.referenceproject.exception.ExceptionMessages.TODO_EXISTED_NAME_VALUES_MESSAGE;
import static com.andrei.referenceproject.gui.model.TodoTableModel.COLUMN_INDEX_PRIORITY;

public class MainFrame extends JFrame {
    private static final String FRAME_TITLE = "Reference project";
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private final TodoService todoService;
    private final PriorityService priorityService;
    private final EventPublisher eventPublisher;
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

    public MainFrame(TodoService todoService, PriorityService priorityService, EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.todoService = todoService;
        this.priorityService = priorityService;
        initPanel();
        initTableData();
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

    private void initTableData() {
        prioritiesModel = new PriorityComboBoxModel(priorityService.findAllPriorities());
        priorityComboBox.setModel(prioritiesModel);
        tableModel = new TodoTableModel(todoService.findAllTodos(), prioritiesModel);
        todoTable.setModel(tableModel);
        decorateTable();
        tableModel.setUpdateValueCallback(todo -> {
            try {
                todoService.updateTodo(todo.getId(), todo);
            } catch (InvalidEnteredDataException ex) {
                JOptionPane.showMessageDialog(MainFrame.this, INVALID_TODO_FIELDS_MESSAGE);
            } catch (ComponentExistedValuesException ex) {
                String oldName = todoService.findTodoById(todo.getId()).getName();
                todo.setName(oldName);
                tableModel.updateRow(todo);
                JOptionPane.showMessageDialog(MainFrame.this, TODO_EXISTED_NAME_VALUES_MESSAGE);
            }
        });
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
        addButton.addActionListener(e -> new TodoFrame(todoService, priorityService, eventPublisher));
    }

    private void addEditButtonListener() {
        editButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            if (selectedRow != -1) {
                Todo todo = tableModel.getSelectedTodo(selectedRow);
                new TodoFrame(todoService, priorityService, todo, eventPublisher);
            }
        });
    }

    private void addDeleteButtonListener() {
        deleteButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            if (selectedRow != -1) {
                Todo todoToDelete = tableModel.getSelectedTodo(selectedRow);
                todoService.deleteTodo(todoToDelete.getId());
                eventPublisher.notifySubscribers(EventType.DELETE_TODO, todoToDelete.getId());
            }
        });
    }

    private void addMoveUpButtonListener() {
        moveUpButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            int rowTo = selectedRow - 1;
            if (selectedRow > 0) {
                Todo todoToSwap1 = tableModel.getSelectedTodo(selectedRow);
                Todo todoToSwap2 = tableModel.getSelectedTodo(rowTo);
                todoService.swapTodo(todoToSwap1, todoToSwap2);
                List<Integer> indices = new ArrayList<>();
                indices.add(selectedRow);
                indices.add(rowTo);
                eventPublisher.notifySubscribers(EventType.SWAP_TODO, indices);
                todoTable.changeSelection(rowTo, 0, false, false);
            }
        });
    }

    private void addMoveDownButtonListener() {
        moveDownButton.addActionListener(e -> {
            int selectedRow = todoTable.getSelectedRow();
            int rowTo = selectedRow + 1;
            if (rowTo < tableModel.getRowCount() && selectedRow != -1) {
                Todo todoToSwap1 = tableModel.getSelectedTodo(selectedRow);
                Todo todoToSwap2 = tableModel.getSelectedTodo(rowTo);
                todoService.swapTodo(todoToSwap1, todoToSwap2);
                List<Integer> indices = new ArrayList<>();
                indices.add(selectedRow);
                indices.add(rowTo);
                eventPublisher.notifySubscribers(EventType.SWAP_TODO, indices);
                todoTable.changeSelection(rowTo, 0, false, false);
            }
        });
    }

    private void addPriorityButtonListener() {
        priorityButton.addActionListener(e -> new PriorityFrame(priorityService, eventPublisher));
    }

    private void addSubscribers() {
        eventSubscribers.put(EventType.CREATE_TODO, data -> tableModel.addRow((Todo) data));
        eventSubscribers.put(EventType.UPDATE_TODO, data -> tableModel.updateRow((Todo) data));
        eventSubscribers.put(EventType.DELETE_TODO, data -> tableModel.deleteRow((Long) data));
        eventSubscribers.put(EventType.SWAP_TODO, data -> tableModel.swapRow((List<Integer>) data));
        eventSubscribers.put(EventType.CREATE_PRIORITY, data -> prioritiesModel.addPriority((Priority) data));
        eventSubscribers.put(EventType.UPDATE_PRIORITY, data -> {
            prioritiesModel.updatePriority((Priority) data);
            tableModel.fireTableDataChanged();
        });
        eventSubscribers.put(EventType.DELETE_PRIORITY, data -> prioritiesModel.deletePriority((Long) data));
        eventPublisher.subscribe(eventSubscribers);
    }
}