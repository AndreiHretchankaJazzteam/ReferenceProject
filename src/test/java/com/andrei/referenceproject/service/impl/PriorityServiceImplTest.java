package com.andrei.referenceproject.service.impl;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.ComponentNotFoundException;
import com.andrei.referenceproject.service.PriorityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PriorityServiceImplTest {
    private PriorityService priorityService;

    @BeforeEach
    void setUp() {
        List<Priority> priorities = new ArrayList<>();
        priorities.add(createFirstPriority());
        priorities.add(createSecondPriority());
        priorityService = new PriorityServiceImpl(priorities);
    }

    @ParameterizedTest
    @MethodSource("preparePriorityToSave")
    void savePriorityTest(Priority priority) {
        Priority expectedPriority = new Priority(1L, "Third", 3);

        Priority actualPriority = priorityService.savePriority(priority);

        assertEquals(expectedPriority, actualPriority);
    }

    @ParameterizedTest
    @MethodSource("preparePriorityWithExistedNameToSave")
    void savePriorityExistedNameTest(Priority priority) {
        assertThrows(ComponentExistedValuesException.class, () -> priorityService.savePriority(priority));
    }

    @ParameterizedTest
    @MethodSource("preparePriorityWithExistedWeightToSave")
    void savePriorityExistedWeightTest(Priority priority) {
        assertThrows(ComponentExistedValuesException.class, () -> priorityService.savePriority(priority));
    }

    @ParameterizedTest
    @MethodSource("prepareAllPriorities")
    void findAllPrioritiesTest(List<Priority> expectedPriorities) {
        List<Priority> actualPriorities = priorityService.findAllPriorities();

        assertEquals(expectedPriorities, actualPriorities);
    }

    @ParameterizedTest
    @MethodSource("prepareFirstPriorityToUpdate")
    void updatePriorityTest(Priority expectedPriority) {
        Priority actualPriority = priorityService.updatePriority(expectedPriority.getId(), expectedPriority);

        assertEquals(expectedPriority, actualPriority);
    }

    @ParameterizedTest
    @MethodSource("prepareFirstPriorityWithExistedNameToUpdate")
    void updatePriorityExistedNameTest(Priority priority) {
        assertThrows(ComponentExistedValuesException.class, () -> priorityService.updatePriority(priority.getId(), priority));
    }

    @ParameterizedTest
    @MethodSource("prepareFirstPriorityWithExistedWeightToUpdate")
    void updatePriorityExistedWeightTest(Priority priority) {
        assertThrows(ComponentExistedValuesException.class, () -> priorityService.updatePriority(priority.getId(), priority));
    }

    @Test
    void deletePriorityTest() {
        Long id = -1L;
        priorityService.deletePriority(id);

        assertThrows(ComponentNotFoundException.class, () -> priorityService.findPriorityById(id));
    }

    @ParameterizedTest
    @MethodSource("prepareFirstPriority")
    void findPriorityByIdTest(Priority expectedPriority) {
        Priority actualPriority = priorityService.findPriorityById(-1L);

        assertEquals(expectedPriority, actualPriority);
    }

    public static Stream<Arguments> preparePriorityToSave() {
        Priority priority = new Priority();
        priority.setName("Third");
        priority.setWeight(3);
        return Stream.of(Arguments.of(priority));
    }

    public static Stream<Arguments> preparePriorityWithExistedNameToSave() {
        Priority priority = new Priority();
        priority.setName("First");
        return Stream.of(Arguments.of(priority));
    }

    public static Stream<Arguments> preparePriorityWithExistedWeightToSave() {
        Priority priority = new Priority();
        priority.setWeight(1);
        return Stream.of(Arguments.of(priority));
    }

    public static Stream<Arguments> prepareAllPriorities() {
        return Stream.of(Arguments.of(List.of(
                createFirstPriority(),
                createSecondPriority()
        )));
    }

    public static Stream<Arguments> prepareFirstPriorityToUpdate() {
        Priority priority = createFirstPriority();
        priority.setName("Updated");
        priority.setWeight(111);
        return Stream.of(Arguments.of(priority));
    }

    public static Stream<Arguments> prepareFirstPriorityWithExistedNameToUpdate() {
        Priority priority = createFirstPriority();
        priority.setName("Second");
        return Stream.of(Arguments.of(priority));
    }

    public static Stream<Arguments> prepareFirstPriorityWithExistedWeightToUpdate() {
        Priority priority = createFirstPriority();
        priority.setWeight(2);
        return Stream.of(Arguments.of(priority));
    }

    public static Stream<Arguments> prepareFirstPriority() {
        return Stream.of(Arguments.of(createFirstPriority()));
    }
    private static Priority createFirstPriority() {
        return new Priority(-1L, "First", 1);
    }

    private static Priority createSecondPriority() {
        return new Priority(-2L, "Second", 2);
    }
}