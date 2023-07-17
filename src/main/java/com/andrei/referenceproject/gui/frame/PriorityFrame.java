package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.event.EventPublisher;
import com.andrei.referenceproject.event.EventSubscriber;
import com.andrei.referenceproject.event.EventType;
import com.andrei.referenceproject.exception.ComponentDeletedException;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.InvalidEnteredDataException;
import com.andrei.referenceproject.gui.model.PriorityTableModel;
import com.andrei.referenceproject.service.PriorityService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import static com.andrei.referenceproject.exception.ExceptionMessages.*;

public class PriorityFrame extends JFrame {
    private static final String FRAME_TITLE = "Priority";
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 300;
    private final PriorityService priorityService;
    private final EventPublisher eventPublisher;
    private final Map<EventType, EventSubscriber> eventSubscribers = new HashMap<>();
    private PriorityTableModel priorityTableModel;
    private JPanel rootPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField priorityNameTextField;
    private JTextField priorityWeightTextField;
    private JTable priorityTable;

    public PriorityFrame(PriorityService priorityService, EventPublisher eventPublisher) {
        this.priorityService = priorityService;
        this.eventPublisher = eventPublisher;
        initPanel();
        initTable();
        addListeners();
        addSubscribers();
    }

    private void initPanel() {
        setTitle(FRAME_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setVisible(true);
    }

    private void initTable() {
        priorityTableModel = new PriorityTableModel(priorityService.findAllPriorities());
        priorityTable.setModel(priorityTableModel);
        priorityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void addListeners() {
        addAddButtonListener();
        addDeleteButtonListener();
        addEditButtonListener();
        addTableListener();
        addWindowListener();
    }

    private void addTableListener() {
        priorityTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = priorityTable.getSelectedRow();
                if (selectedRow != -1) {
                    Priority priority = priorityTableModel.getSelectedPriority(selectedRow);
                    priorityNameTextField.setText(priority.getName());
                    if (priority.getWeight() != null) {
                        priorityWeightTextField.setText(priority.getWeight().toString());
                    }
                } else {
                    clearTextFields();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void addAddButtonListener() {
        addButton.addActionListener(e -> {
            try {
                Priority priority = createPriorityFromFields();
                priority = priorityService.savePriority(priority);
                eventPublisher.notifySubscribers(EventType.CREATE_PRIORITY, priority);
                clearTextFields();
            } catch (InvalidEnteredDataException ex) {
                JOptionPane.showMessageDialog(PriorityFrame.this, INVALID_PRIORITY_FIELDS_MESSAGE);
            } catch (ComponentExistedValuesException ex) {
                JOptionPane.showMessageDialog(PriorityFrame.this, PRIORITY_EXISTED_VALUES_MESSAGE);
            }
        });
    }

    private void addDeleteButtonListener() {
        deleteButton.addActionListener(e -> {
            int selectedRow = priorityTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    Priority priority = priorityTableModel.getSelectedPriority(selectedRow);
                    priorityService.deletePriority(priority.getId());
                    eventPublisher.notifySubscribers(EventType.DELETE_PRIORITY, priority.getId());
                    clearTextFields();
                } catch (ComponentDeletedException ex) {
                    JOptionPane.showMessageDialog(PriorityFrame.this, DELETE_BEING_USED_PRIORITY_MESSAGE);
                }
            }
        });
    }

    private void addEditButtonListener() {
        editButton.addActionListener(e -> {
            int selectedRow = priorityTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    Priority priority = createPriorityFromFields();
                    Priority priorityToUpdate = priorityTableModel.getSelectedPriority(selectedRow);
                    priority = priorityService.updatePriority(priorityToUpdate.getId(), priority);
                    eventPublisher.notifySubscribers(EventType.UPDATE_PRIORITY, priority);
                    clearTextFields();
                } catch (InvalidEnteredDataException ex) {
                    JOptionPane.showMessageDialog(PriorityFrame.this, INVALID_PRIORITY_FIELDS_MESSAGE);
                } catch (ComponentExistedValuesException ex) {
                    JOptionPane.showMessageDialog(PriorityFrame.this, PRIORITY_EXISTED_VALUES_MESSAGE);
                }
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

    private Priority createPriorityFromFields() {
        validatePriorityFields();
        Priority priority = new Priority();
        priority.setName(priorityNameTextField.getText());
        if (!priorityWeightTextField.getText().isEmpty()) {
            priority.setWeight(Integer.valueOf(priorityWeightTextField.getText()));
        }
        return priority;
    }

    private void validatePriorityFields() {
        boolean isValid = true;

        priorityNameTextField.setBackground(Color.WHITE);
        if (priorityNameTextField.getText().isEmpty()) {
            isValid = false;
            priorityNameTextField.setBackground(Color.PINK);
        }

        if (!priorityWeightTextField.getText().isEmpty()) {
            try {
                Integer.parseInt(priorityWeightTextField.getText());
                priorityWeightTextField.setBackground(Color.WHITE);
            } catch (NumberFormatException e) {
                isValid = false;
                priorityWeightTextField.setBackground(Color.PINK);
            }
        }

        if (!isValid) {
            throw new InvalidEnteredDataException(INVALID_PRIORITY_FIELDS_MESSAGE);
        }
    }

    private void clearTextFields() {
        priorityNameTextField.setText("");
        priorityWeightTextField.setText("");
    }

    private void addSubscribers() {
        eventSubscribers.put(EventType.CREATE_PRIORITY, data -> priorityTableModel.addRow((Priority) data));
        eventSubscribers.put(EventType.UPDATE_PRIORITY, data -> priorityTableModel.updateRow((Priority) data));
        eventSubscribers.put(EventType.DELETE_PRIORITY, data -> priorityTableModel.deleteRow((Long) data));
        eventPublisher.subscribe(eventSubscribers);
    }
}