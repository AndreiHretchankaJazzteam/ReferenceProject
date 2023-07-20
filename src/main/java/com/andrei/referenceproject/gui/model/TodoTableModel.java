package com.andrei.referenceproject.gui.model;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.exception.ComponentNotFoundException;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;

import java.util.*;
import java.util.function.Consumer;

import static com.andrei.referenceproject.exception.ExceptionMessages.TODO_NOT_FOUND_MESSAGE;

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
    private Consumer<Todo> updateRowCallback;

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
            case COLUMN_INDEX_PRIORITY -> Priority.class;
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
        Object valueAt = null;
        switch (columnIndex) {
            case COLUMN_INDEX_TITLE -> valueAt = todo.getName();
            case COLUMN_INDEX_DESCRIPTION -> valueAt = todo.getDescription();
            case COLUMN_INDEX_DATE -> valueAt = todo.getDate();
            case COLUMN_INDEX_PRIORITY -> {
                if (todo.getPriority() != null) {
                    valueAt = prioritiesModel.getPriority(todo.getPriority());
                }
            }
        }
        return valueAt;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Todo todo = todos.get(rowIndex);
        switch (columnIndex) {
            case COLUMN_INDEX_TITLE -> todo.setName((String) aValue);
            case COLUMN_INDEX_DESCRIPTION -> todo.setDescription((String) aValue);
            case COLUMN_INDEX_DATE -> todo.setDate((LocalDate) aValue);
            case COLUMN_INDEX_PRIORITY -> todo.setPriority((Priority) aValue);
        }

        if (updateRowCallback != null) {
            updateRowCallback.accept(todo);
        }
    }

    public void addRow(Todo todo) {
        todos.add(todo);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public void updateRow(Todo updatedTodo) {
        todos.stream()
                .filter(todo -> todo.getId().equals(updatedTodo.getId()))
                .findFirst()
                .ifPresent(todo -> {
                    int index = todos.indexOf(todo);
                    todos.set(index, updatedTodo);
                    fireTableRowsUpdated(index, index);
                });
    }

    public Todo getSelectedTodo(int index) {
        return todos.get(index);
    }

    public void deleteRow(Long id) {
        todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .ifPresent(todo -> {
                    int index = todos.indexOf(todo);
                    todos.remove(index);
                    fireTableRowsDeleted(index, index);
                });
    }

    public void swapRow(List<Todo> todoList) {
        int firstIndex = todos.indexOf(todoList.get(1));
        int secondIndex = todos.indexOf(todoList.get(0));
        Collections.swap(this.todos, firstIndex, secondIndex);
        fireTableDataChanged();
    }

    public int getSwapRowToIndex(List<Todo> todoList) {
        return todos.indexOf(todoList.get(1));
    }

    public int getSwapRowIndex(List<Todo> todoList) {
        return todos.indexOf(todoList.get(0));
    }

    public int getRowIndexByTodoId(Long id) {
        Todo todo = todos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ComponentNotFoundException(TODO_NOT_FOUND_MESSAGE));
        return todos.indexOf(todo);
    }

    public void setUpdateValueCallback(Consumer<Todo> callback) {
        this.updateRowCallback = callback;
    }
}
