package com.andrei.referenceproject.gui.model;

import com.andrei.referenceproject.entity.Todo;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoTableModel extends AbstractTableModel {
    private static final Map<Integer, String> COLUMNS = new HashMap<>();

    private static final String COLUMN_NAME_TITLE = "Title";
    private static final String COLUMN_NAME_DESCRIPTION = "Description";
    private static final String COLUMN_NAME_DATE = "Date";
    private static final String COLUMN_NAME_PRIORITY = "Priority";

    public static final int COLUMN_INDEX_TITLE = 0;
    public static final int COLUMN_INDEX_DESCRIPTION = 1;
    public static final int COLUMN_INDEX_DATE = 2;
    public static final int COLUMN_INDEX_PRIORITY = 3;

    private final List<Todo> todos;
    private final PriorityComboBoxModel prioritiesModel;


    static {
        COLUMNS.put(COLUMN_INDEX_TITLE, COLUMN_NAME_TITLE);
        COLUMNS.put(COLUMN_INDEX_DESCRIPTION, COLUMN_NAME_DESCRIPTION);
        COLUMNS.put(COLUMN_INDEX_DATE, COLUMN_NAME_DATE);
        COLUMNS.put(COLUMN_INDEX_PRIORITY, COLUMN_NAME_PRIORITY);
    }

    public TodoTableModel(List<Todo> todos, PriorityComboBoxModel prioritiesModel) {
        this.todos = new ArrayList<>(todos);
        this.prioritiesModel = prioritiesModel;
    }

    @Override
    public int getRowCount() {
        return todos.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMNS.get(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> columnClass = switch (columnIndex) {
            case COLUMN_INDEX_DATE -> LocalDate.class;
            case COLUMN_INDEX_PRIORITY -> PriorityComboBoxModelItem.class;
            default -> String.class;
        };
        return columnClass;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Todo todo = todos.get(rowIndex);
        Object valueAt = switch (columnIndex) {
            case COLUMN_INDEX_TITLE -> todo.getName();
            case COLUMN_INDEX_DESCRIPTION -> todo.getDescription();
            case COLUMN_INDEX_DATE -> todo.getDate();
            case COLUMN_INDEX_PRIORITY -> prioritiesModel.getPriorityComboBoxModelItem(todo.getPriority());
            default -> null;
        };
        return valueAt;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Todo todo = todos.get(rowIndex);
        switch (columnIndex) {
            case COLUMN_INDEX_TITLE -> todo.setName((String) aValue);
            case COLUMN_INDEX_DESCRIPTION -> todo.setDescription((String) aValue);
            case COLUMN_INDEX_DATE -> todo.setDate((LocalDate) aValue);
            case COLUMN_INDEX_PRIORITY -> todo.setPriority(((PriorityComboBoxModelItem) aValue).getPriority());
        }
    }
}
