package com.andrei.referenceproject.gui.model;

import com.andrei.referenceproject.entity.Todo;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

    static {
        COLUMNS.put(COLUMN_INDEX_TITLE, COLUMN_NAME_TITLE);
        COLUMNS.put(COLUMN_INDEX_DESCRIPTION, COLUMN_NAME_DESCRIPTION);
        COLUMNS.put(COLUMN_INDEX_DATE, COLUMN_NAME_DATE);
        COLUMNS.put(COLUMN_INDEX_PRIORITY, COLUMN_NAME_PRIORITY);
    }

    public TodoTableModel(List<Todo> todos) {
        this.todos = new ArrayList<>(todos);
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
        Class<?> columnClass;
        switch (columnIndex) {
            case COLUMN_INDEX_DATE:
                columnClass = LocalDate.class;
                break;
            default:
                columnClass = String.class;
                break;
        }
        return columnClass;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Todo todo = todos.get(rowIndex);
        Object valueAt = null;
        switch (columnIndex) {
            case COLUMN_INDEX_TITLE:
                valueAt = todo.getName();
                break;
            case COLUMN_INDEX_DESCRIPTION:
                valueAt = todo.getDescription();
                break;
            case COLUMN_INDEX_DATE:
                valueAt = todo.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                break;
            case COLUMN_INDEX_PRIORITY:
                valueAt = todo.getPriority();
                break;
        }
        return valueAt;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Todo todo = todos.get(rowIndex);
        switch (columnIndex) {
            case COLUMN_INDEX_TITLE:
                todo.setName((String) aValue);
                break;
            case COLUMN_INDEX_DESCRIPTION:
                todo.setDescription((String) aValue);
                break;
            case COLUMN_INDEX_DATE:
                todo.setDate((LocalDate) aValue);
                break;
            case COLUMN_INDEX_PRIORITY:
                todo.setPriority((String) aValue);
                break;
        }
    }

    public void addRow(Todo todo) {
        todos.add(todo);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public Todo getSelectedTodo(int index) {
        return todos.get(index);
    }

    public void updateRow(Todo updatedTodo) {
        todos.stream()
                .filter(todo -> todo.equals(updatedTodo))
                .findFirst()
                .ifPresent(todo -> {
                    int index = todos.indexOf(todo);
                    todos.set(index, updatedTodo);
                    fireTableRowsUpdated(index, index);
                });
    }
}
