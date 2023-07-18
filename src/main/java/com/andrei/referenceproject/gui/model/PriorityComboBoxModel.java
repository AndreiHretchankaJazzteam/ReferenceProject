package com.andrei.referenceproject.gui.model;

import com.andrei.referenceproject.entity.Priority;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class PriorityComboBoxModel implements ComboBoxModel<Priority> {
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

    public Priority getSelectedPriority() {
        if (selectedPriority != null) {
            return selectedPriority;
        }
        return null;
    }

    public void addPriority(Priority priority) {
        priorities.add(priority);
    }

    public void deletePriority(Long id) {
        priorities.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .ifPresent(priorities::remove);
    }

    public void updatePriority(Priority priority) {
        priorities.stream()
                .filter(p -> p.getId().equals(priority.getId()))
                .findFirst()
                .ifPresent(p -> {
                    p.setName(priority.getName());
                    p.setWeight(priority.getWeight());
                });
    }

    public Priority getPriority(Priority priority) {
        return priorities.stream()
                .filter(p -> p.getId().equals(priority.getId()))
                .findFirst()
                .orElse(priorities.get(0));
    }
}
