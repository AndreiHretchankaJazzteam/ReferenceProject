package com.andrei.referenceproject.service.impl;

import com.andrei.referenceproject.Application;
import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.exception.ComponentDeletedException;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.ComponentNotFoundException;
import com.andrei.referenceproject.service.PriorityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Sql(scripts = {"/drop_schema.sql", "/create_schema.sql", "/insert_schema.sql"})
class PriorityServiceImplTest {
    private static final Long FIRST_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final Long THIRD_ID = 3L;
    private static final Long FOURTH_ID = 4L;

    @Autowired
    private PriorityService priorityService;

    @ParameterizedTest
    @MethodSource("preparePriorityToSave")
    void savePriorityTest(Priority priority) {
        Priority expectedPriority = createFourthPriority();

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
    @MethodSource({"prepareAllPrioritiesWithNewPriority"})
    void findAllPrioritiesWithNewPriorityTest(List<Priority> expectedPriorities, Priority priority) {
        priorityService.savePriority(priority);

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
        Long id = THIRD_ID;
        priorityService.deletePriority(id);

        assertThrows(ComponentNotFoundException.class, () -> priorityService.findPriorityById(id));
    }

    @Test
    void deletePriorityWithNonExistentIdTest() {
        Long nonExistentId = 22L;

        assertThrows(ComponentNotFoundException.class, () -> priorityService.deletePriority(nonExistentId));
    }

    @Test
    void deleteUsedPriorityTest() {
        Long usedId = 1L;

        assertThrows(ComponentDeletedException.class, () -> priorityService.deletePriority(usedId));
    }

    @ParameterizedTest
    @MethodSource("prepareFirstPriorityToUpdate")
    void updatePriorityWithNonExistentIdTest(Priority priority) {
        Long nonExistentId = 22L;

        assertThrows(ComponentNotFoundException.class, () -> priorityService.updatePriority(nonExistentId, priority));
    }

    @ParameterizedTest
    @MethodSource("prepareFirstPriority")
    void findPriorityByIdTest(Priority expectedPriority) {
        Priority actualPriority = priorityService.findPriorityById(FIRST_ID);

        assertEquals(expectedPriority, actualPriority);
    }

    public static Stream<Arguments> prepareAllPrioritiesWithNewPriority() {
        Priority priority = new Priority();
        priority.setName("Fourth");
        priority.setWeight(4);
        return Stream.of(Arguments.of(List.of(
                createFirstPriority(),
                createSecondPriority(),
                createThirdPriority(),
                createFourthPriority()
        ), priority));
    }

    public static Stream<Arguments> preparePriorityToSave() {
        Priority priority = new Priority();
        priority.setName("Fourth");
        priority.setWeight(4);
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
                createSecondPriority(),
                createThirdPriority()
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
        Priority priority = new Priority();
        priority.setId(FIRST_ID);
        priority.setName("First");
        priority.setWeight(1);
        return priority;
    }

    private static Priority createSecondPriority() {
        Priority priority = new Priority();
        priority.setId(SECOND_ID);
        priority.setName("Second");
        priority.setWeight(2);
        return priority;
    }

    private static Priority createThirdPriority() {
        Priority priority = new Priority();
        priority.setId(THIRD_ID);
        priority.setName("Third");
        priority.setWeight(3);
        return priority;
    }

    private static Priority createFourthPriority() {
        Priority priority = new Priority();
        priority.setId(FOURTH_ID);
        priority.setName("Fourth");
        priority.setWeight(4);
        return priority;
    }
}