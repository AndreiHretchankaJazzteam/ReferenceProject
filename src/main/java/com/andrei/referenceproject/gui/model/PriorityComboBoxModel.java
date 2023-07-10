package com.andrei.referenceproject.gui.model;

import com.andrei.referenceproject.entity.Priority;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;

public class PriorityComboBoxModel implements ComboBoxModel<Priority> {
    private final List<Priority> priorities;
    private Priority selectedPriority;

    public PriorityComboBoxModel(List<Priority> priorities) {
        this.priorities = priorities;
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedPriority = (Priority) anItem;
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
}
