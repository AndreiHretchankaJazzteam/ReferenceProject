package com.andrei.referenceproject.gui.model;

import com.andrei.referenceproject.entity.Priority;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;
import java.util.stream.Collectors;

public class PriorityComboBoxModel implements ComboBoxModel<PriorityComboBoxModelItem> {
    private final List<PriorityComboBoxModelItem> items;

    private PriorityComboBoxModelItem selectedItem;

    public PriorityComboBoxModel(List<Priority> priorities) {
        this.items = priorities.stream()
                .map(PriorityComboBoxModelItem::new)
                .collect(Collectors.toList());
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = (PriorityComboBoxModelItem) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public PriorityComboBoxModelItem getElementAt(int index) {
        return items.get(index);
    }

    public PriorityComboBoxModelItem getPriorityComboBoxModelItem(Priority priority) {
        return items.stream()
                .filter(item -> item.getPriority().equals(priority))
                .findFirst()
                .orElse(items.get(0));
    }
}
