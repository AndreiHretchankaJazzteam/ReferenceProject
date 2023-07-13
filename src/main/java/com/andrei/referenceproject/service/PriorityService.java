package com.andrei.referenceproject.service;

import com.andrei.referenceproject.entity.Priority;

import java.util.List;

public interface PriorityService {
    Priority savePriority(Priority priority);

    List<Priority> findAllPriorities();

    Priority updatePriority(Long id, Priority priority);

    void deletePriority(Long id);

    Priority findPriorityById(Long id);
}
