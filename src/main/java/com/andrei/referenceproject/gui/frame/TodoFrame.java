package com.andrei.referenceproject.gui.frame;

import com.andrei.referenceproject.entity.Todo;

import javax.swing.*;

public class TodoFrame extends JFrame {
    private static final String FRAME_TITLE = "Task creator";
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 500;
    private JPanel rootPanel;
    private JButton acceptButton;
    private JTextField textField1;
    private JTextArea textArea1;
    private JTextField textField2;
    private JComboBox comboBox1;

    private Todo todo;

    public TodoFrame() {
        setTitle(FRAME_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setVisible(true);
    }
    public TodoFrame(Todo todo) {
        this.todo = todo;
        initFrame();
    }

    private void initFrame() {
        setTitle(FRAME_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setVisible(true);
    }
}
