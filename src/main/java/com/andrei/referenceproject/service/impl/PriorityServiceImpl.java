package com.andrei.referenceproject.service.impl;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.exception.ComponentDeleteException;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.ComponentNotFoundException;
import com.andrei.referenceproject.repository.PriorityRepository;
import com.andrei.referenceproject.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.andrei.referenceproject.exception.ExceptionMessages.*;

@Service
public class PriorityServiceImpl implements PriorityService {
    private final PriorityRepository priorityRepository;

    @Autowired
    public PriorityServiceImpl(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    @Override
    public Priority savePriority(Priority priority) {
        try {
            return priorityRepository.save(priority);
        } catch (DataIntegrityViolationException e) {
            throw new ComponentExistedValuesException(PRIORITY_EXISTED_VALUES_MESSAGE);
        }
    }

    @Override
    public List<Priority> findAllPriorities() {
        return priorityRepository.findAllByOrderByWeightAsc();
    }

    @Override
    public Priority updatePriority(Long id, Priority priority) {
        try {
            findPriorityById(id);
            priority.setId(id);
            return priorityRepository.save(priority);
        } catch (DataIntegrityViolationException e) {
            throw new ComponentExistedValuesException(PRIORITY_EXISTED_VALUES_MESSAGE);
        }
    }

    @Override
    public void deletePriority(Long id) {
        try {
            findPriorityById(id);
            priorityRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ComponentDeleteException(DELETE_BEING_USED_PRIORITY_MESSAGE);
        }

    }

    @Override
    public Priority findPriorityById(Long id) {
        return priorityRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException(String.format(PRIORITY_NOT_FOUND_MESSAGE, id)));
    }
}
