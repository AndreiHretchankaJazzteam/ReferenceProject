package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventSubscriber;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.exception.InvalidEnteredDataException;
import com.andrei.referenceproject.gui.model.PriorityComboBoxModel;
import com.andrei.referenceproject.task.*;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.andrei.referenceproject.exception.ExceptionMessages.INVALID_TODO_FIELDS_MESSAGE;
import static com.andrei.referenceproject.exception.ExceptionMessages.TODO_EXISTED_NAME_VALUES_MESSAGE;

public class TodoFrame extends JFrame {
    private static final String FRAME_TITLE_CREATE = "Create Todo";
    private static final String FRAME_TITLE_EDIT = "Edit Todo";
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 500;
    private final Map<EventType, EventSubscriber> eventSubscribers = new HashMap<>();
    private DatePicker datePicker;
    private JPanel rootPanel;
    private JButton acceptButton;
    private JTextArea descriptionTextArea;
    private JTextField nameTextField;
    private PriorityComboBoxModel prioritiesModel;
    private JComboBox<Priority> priorityComboBox;
    private JButton dateButton;
    private Todo todoToUpdate;

    public TodoFrame() {
        this(null);
    }

    public TodoFrame(Todo todo) {
        this.todoToUpdate = todo;
        initFrame();
    }

    private void initFrame() {
        initPanel();
        initPriorities();
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
        GetAllPriorityTask getAllPriorityTask = TaskFactory.getGetAllPriorityTask();
        getAllPriorityTask.execute(new ArrayList<>(), new TaskListener<>() {
            @Override
            public void onSuccess(List<Priority> priorities) {
                prioritiesModel = new PriorityComboBoxModel(priorities);
                priorityComboBox.setModel(prioritiesModel);
                initFields();
            }
        });
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
                    CreateTodoTask createTodoTask = TaskFactory.getCreateTodoTask();
                    createTodoTask.execute(todo, new TaskListener<>() {
                        @Override
                        public void onFailure(Exception e) {
                            JOptionPane.showMessageDialog(TodoFrame.this, TODO_EXISTED_NAME_VALUES_MESSAGE);
                        }
                    });
                } else {
                    todo.setId(todoToUpdate.getId());
                    UpdateTodoTask updateTodoTask = TaskFactory.getUpdateTodoTask();
                    updateTodoTask.execute(todo, new TaskListener<>() {
                        @Override
                        public void onFailure(Exception e) {
                            JOptionPane.showMessageDialog(TodoFrame.this, TODO_EXISTED_NAME_VALUES_MESSAGE);
                        }
                    });
                }
                dispose();
            } catch (InvalidEnteredDataException ex) {
                JOptionPane.showMessageDialog(TodoFrame.this, INVALID_TODO_FIELDS_MESSAGE);
            }
        });
    }

    private void addWindowListener() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                EventPublisher.unsubscribe(eventSubscribers);
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
            prioritiesModel.setSelectedItem(todoToUpdate.getPriority());
        }
    }

    private boolean isForEdit() {
        return todoToUpdate != null;
    }

    private void addSubscribers() {
        eventSubscribers.put(EventType.CREATE_PRIORITY, data -> prioritiesModel.addPriority((Priority) data));
        eventSubscribers.put(EventType.UPDATE_PRIORITY, data -> prioritiesModel.updatePriority((Priority) data));
        eventSubscribers.put(EventType.DELETE_PRIORITY, data -> prioritiesModel.deletePriority((Long) data));
        EventPublisher.subscribe(eventSubscribers);
    }

    @Override
    public void dispose() {
        EventPublisher.unsubscribe(eventSubscribers);
        super.dispose();
    }
}
