package com.andrei.referenceproject.gui.model;

import com.andrei.referenceproject.entity.Priority;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriorityTableModel extends AbstractTableModel {
    private static final Map<Integer, String> COLUMNS = new HashMap<>();

    private static final String COLUMN_NAME_TITLE = "Title";
    private static final String COLUMN_NAME_DESCRIPTION = "Weight";
    public static final int COLUMN_INDEX_TITLE = 0;
    public static final int COLUMN_INDEX_WEIGHT = 1;

    private final List<Priority> priorities;

    static {
        COLUMNS.put(COLUMN_INDEX_TITLE, COLUMN_NAME_TITLE);
        COLUMNS.put(COLUMN_INDEX_WEIGHT, COLUMN_NAME_DESCRIPTION);
    }

    public PriorityTableModel(List<Priority> priorities) {
        this.priorities = new ArrayList<>(priorities);
    }


    @Override
    public int getRowCount() {
        return priorities.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Priority priority = priorities.get(rowIndex);
        Object valueAt = switch (columnIndex) {
            case COLUMN_INDEX_TITLE -> priority.getName();
            case COLUMN_INDEX_WEIGHT -> priority.getWeight();
            default -> null;
        };
        return valueAt;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMNS.get(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMNS.get(columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
