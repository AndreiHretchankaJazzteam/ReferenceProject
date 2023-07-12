package com.andrei.referenceproject.service.impl;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.ComponentNotFoundException;
import com.andrei.referenceproject.service.PriorityService;

import java.util.List;
import java.util.Objects;

import static com.andrei.referenceproject.exception.ExceptionMessages.PRIORITY_EXISTED_VALUES_MESSAGE;
import static com.andrei.referenceproject.exception.ExceptionMessages.PRIORITY_NOT_FOUND_MESSAGE;

public class PriorityServiceImpl implements PriorityService {
    private static Long i = 1L;
    private final List<Priority> priorities;

    public PriorityServiceImpl(List<Priority> priorities) {
        this.priorities = priorities;
    }

    @Override
    public Priority savePriority(Priority priority) {
        priorities.stream()
                .filter(p -> p.getName().equals(priority.getName()) || p.getWeight().equals(priority.getWeight()))
                .findFirst()
                .ifPresent(p -> {
                    throw new ComponentExistedValuesException(PRIORITY_EXISTED_VALUES_MESSAGE);
                });
        priority.setId(i);
        i++;
        priorities.add(priority);
        return priority;
    }

    @Override
    public List<Priority> findAllPriorities() {
        return priorities;
    }

    @Override
    public Priority updatePriority(Long id, Priority priority) {
        priorities.stream()
                .filter(p -> (p.getName().equals(priority.getName()) || p.getWeight().equals(priority.getWeight())) && !Objects.equals(p.getId(), id))
                .findFirst()
                .ifPresent(p -> {
                    throw new ComponentExistedValuesException(PRIORITY_EXISTED_VALUES_MESSAGE);
                });
        priority.setId(id);
        priorities.stream()
                .filter(p -> Objects.equals(p.getId(), priority.getId()))
                .findFirst()
                .ifPresent(p -> {
                    int index = priorities.indexOf(p);
                    priorities.set(index, priority);
                });
        return priority;
    }

    @Override
    public void deletePriority(Long id) {
        priorities.stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst()
                .ifPresent(priorities::remove);
    }

    @Override
    public Priority findPriorityById(Long id) {
        return priorities.stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ComponentNotFoundException(String.format(PRIORITY_NOT_FOUND_MESSAGE, id)));
    }
}
