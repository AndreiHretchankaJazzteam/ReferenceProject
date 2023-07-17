package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventSubscriber;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.InvalidEnteredDataException;
import com.andrei.referenceproject.gui.model.PriorityComboBoxModel;
import com.andrei.referenceproject.service.PriorityService;
import com.andrei.referenceproject.service.TodoService;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.andrei.referenceproject.exception.ExceptionMessages.INVALID_TODO_FIELDS_MESSAGE;
import static com.andrei.referenceproject.exception.ExceptionMessages.TODO_EXISTED_NAME_VALUES_MESSAGE;

public class TodoFrame extends JFrame {
    private static final String FRAME_TITLE_CREATE = "Create Todo";
    private static final String FRAME_TITLE_EDIT = "Edit Todo";
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 500;
    private final PriorityService priorityService;
    private final EventPublisher eventPublisher;
    private final Map<EventType, EventSubscriber> eventSubscribers = new HashMap<>();
    private final TodoService todoService;
    private DatePicker datePicker;
    private JPanel rootPanel;
    private JButton acceptButton;
    private JTextArea descriptionTextArea;
    private JTextField nameTextField;
    private PriorityComboBoxModel prioritiesModel;
    private JComboBox<Priority> priorityComboBox;
    private JButton dateButton;
    private Todo todoToUpdate;

    public TodoFrame(TodoService todoService, PriorityService priorityService, EventPublisher eventPublisher) {
        this(todoService, priorityService, null, eventPublisher);
    }

    public TodoFrame(TodoService todoService, PriorityService priorityService, Todo todo, EventPublisher eventPublisher) {
        this.todoService = todoService;
        this.priorityService = priorityService;
        this.todoToUpdate = todo;
        this.eventPublisher = eventPublisher;
        initFrame();
    }

    private void initFrame() {
        initPanel();
        initPriorities();
        initFields();
        addListeners();
        addSubscribers();
    }

    private void initPanel() {
        setTitle(isForEdit() ? FRAME_TITLE_EDIT : FRAME_TITLE_CREATE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setVisible(true);
    }

    private void initPriorities() {
        prioritiesModel = new PriorityComboBoxModel(priorityService.findAllPriorities());
        priorityComboBox.setModel(prioritiesModel);
    }

    private void addListeners() {
        addAcceptButtonListener();
        addWindowListener();
    }

    private void addAcceptButtonListener() {
        acceptButton.addActionListener(e -> {
            try {
                Todo todo = createTodoFromFields();
                if (todoToUpdate == null) {
                    eventPublisher.notifySubscribers(EventType.CREATE_TODO, todoService.saveTodo(todo));
                } else {
                    todoService.updateTodo(todoToUpdate.getId(), todo);
                    eventPublisher.notifySubscribers(EventType.UPDATE_TODO, todoService.updateTodo(todoToUpdate.getId(), todo));
                }
                dispose();
            } catch (InvalidEnteredDataException ex) {
                JOptionPane.showMessageDialog(TodoFrame.this, INVALID_TODO_FIELDS_MESSAGE);
            } catch (ComponentExistedValuesException ex) {
                JOptionPane.showMessageDialog(TodoFrame.this, TODO_EXISTED_NAME_VALUES_MESSAGE);
            }
        });
    }

    private void addWindowListener() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                eventPublisher.unsubscribe(eventSubscribers);
            }
        });
    }

    private Todo createTodoFromFields() {
        validateTodoFields();
        Todo todo = new Todo();
        todo.setName(nameTextField.getText());
        todo.setDescription(descriptionTextArea.getText());
        todo.setDate(datePicker.getDate());
        todo.setPriority(prioritiesModel.getSelectedPriority());
        return todo;
    }

    private void validateTodoFields() {
        boolean isValid = true;
        nameTextField.setBackground(Color.WHITE);
        if (nameTextField.getText().isEmpty()) {
            isValid = false;
            nameTextField.setBackground(Color.PINK);
        }

        if (!isValid) {
            throw new InvalidEnteredDataException(INVALID_TODO_FIELDS_MESSAGE);
        }
    }

    private void initFields() {
        datePicker = new DatePicker();
        datePicker.setDate(LocalDate.now());
        dateButton.add(datePicker);
        if (todoToUpdate != null) {
            nameTextField.setText(todoToUpdate.getName());
            descriptionTextArea.setText(todoToUpdate.getDescription());
            datePicker.setDate(todoToUpdate.getDate());
            priorityComboBox.setSelectedItem(todoToUpdate.getPriority());
        }
    }

    private boolean isForEdit() {
        return todoToUpdate != null;
    }

    private void addSubscribers() {
        eventSubscribers.put(EventType.CREATE_PRIORITY, data -> prioritiesModel.addPriority((Priority) data));
        eventSubscribers.put(EventType.UPDATE_PRIORITY, data -> prioritiesModel.updatePriority((Priority) data));
        eventSubscribers.put(EventType.DELETE_PRIORITY, data -> prioritiesModel.deletePriority((Long) data));
        eventPublisher.subscribe(eventSubscribers);
    }

    @Override
    public void dispose() {
        eventPublisher.unsubscribe(eventSubscribers);
        super.dispose();
    }
}
