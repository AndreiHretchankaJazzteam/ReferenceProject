package com.andrei.referenceproject.gui.model;

import com.andrei.referenceproject.entity.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriorityComboBoxModelItem {
    private Priority priority;

    @Override
    public String toString() {
        return priority.getName();
    }
}