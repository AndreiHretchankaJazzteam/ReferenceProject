package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.gui.model.PriorityTableModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PriorityFrame extends JFrame {
    private static final String FRAME_TITLE = "Priority";
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 300;
    private final List<Priority> priorities = new ArrayList<>();

    private JPanel rootPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField priorityNameTextField;
    private JTextField priorityWeightTextField;
    private JTable priorityTable;

    public PriorityFrame() {
        setTitle(FRAME_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setVisible(true);

        generatePriorityList();
        PriorityTableModel priorityTableModel = new PriorityTableModel(priorities);
        priorityTable.setModel(priorityTableModel);
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
