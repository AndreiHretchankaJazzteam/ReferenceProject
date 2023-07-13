package com.andrei.referenceproject.gui.model;

import com.andrei.referenceproject.entity.Priority;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class PriorityComboBoxModel implements ComboBoxModel<Priority> {
    private static final String DEFAULT_NAME = "default priority";
    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Long DEFAULT_ID = -1L;
    private final List<Priority> priorities;
    private Priority selectedPriority;

    public PriorityComboBoxModel(List<Priority> priorities) {
        this.priorities = new ArrayList<>(priorities);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (priorities.contains(anItem)) {
            selectedPriority = (Priority) anItem;
        } else {
            selectedPriority = priorities.stream()
                    .findFirst()
                    .orElse(new Priority(DEFAULT_ID, DEFAULT_NAME, DEFAULT_WEIGHT));
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedPriority;
    }

    @Override
    public int getSize() {
        return priorities.size();
    }

    @Override
    public Priority getElementAt(int index) {
        return priorities.get(index);
    }

    public Priority getSelectedPriority() {
        if (selectedPriority != null) {
            return selectedPriority;
        }
        return null;
    }

    public void addPriority(Priority priority) {
        priorities.add(priority);
    }

    public void deletePriority(Priority priority) {
        priorities.remove(priority);
    }
}
