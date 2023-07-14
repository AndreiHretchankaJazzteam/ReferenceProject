package com.andrei.referenceproject.service.impl;

import com.andrei.referenceproject.Application;
import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.ComponentNotFoundException;
import com.andrei.referenceproject.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Sql(scripts = {"/drop_schema.sql", "/create_schema.sql", "/insert_schema.sql"})
class TodoServiceImplTest {
    private static final Long FIRST_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final Long THIRD_ID = 3L;
    private static final Long FOURTH_ID = 4L;

    @Autowired
    private TodoService todoService;

    @ParameterizedTest
    @MethodSource("prepareTodoToSave")
    void saveTodoTest(Todo todo) {
        Todo expectedTodo = new Todo();
        expectedTodo.setId(4L);
        expectedTodo.setName("New");
        expectedTodo.setOrderNumber(4L);

        Todo actualTodo = todoService.saveTodo(todo);

        assertEquals(expectedTodo, actualTodo);
    }

    @ParameterizedTest
    @MethodSource("prepareTodoWithExistedNameToSave")
    void saveTodoExistedNameTest(Todo todo) {
        assertThrows(ComponentExistedValuesException.class, () -> todoService.saveTodo(todo));
    }

    @ParameterizedTest
    @MethodSource("prepareAllTodos")
    void findAllTodosTest(List<Todo> expectedTodos) {
        List<Todo> actualTodos = todoService.findAllTodos();

        assertEquals(expectedTodos, actualTodos);
    }

    @ParameterizedTest
    @MethodSource("prepareAllTodosWithNewTodo")
    void findAllTodosWithNewTodoTest(List<Todo> expectedTodos, Todo todo) {
        todoService.saveTodo(todo);

        List<Todo> actualTodos = todoService.findAllTodos();

        assertEquals(expectedTodos, actualTodos);
    }

    @ParameterizedTest
    @MethodSource("prepareFirstTodoToUpdate")
    void updateTodoTest(Todo expectedTodo) {
        Todo actualTodo = todoService.updateTodo(expectedTodo.getId(), expectedTodo);

        assertEquals(expectedTodo, actualTodo);
    }

    @ParameterizedTest
    @MethodSource("prepareFirstTodoWithExistedNameToUpdate")
    void updateTodoExistedNameTest(Todo todo) {
        assertThrows(ComponentExistedValuesException.class, () -> todoService.updateTodo(todo.getId(), todo));
    }

    @ParameterizedTest
    @MethodSource("prepareSwappedTodos")
    void swapTodoTest(List<Todo> expectedTodos) {
        todoService.swapTodo(createFirstTodo(), createSecondTodo());
        List<Todo> actualTodos = todoService.findAllTodos();

        assertEquals(expectedTodos, actualTodos);
    }

    @Test
    void deleteTodoTest() {
        Long id = FIRST_ID;
        todoService.deleteTodo(id);

        assertThrows(ComponentNotFoundException.class, () -> todoService.findTodoById(id));
    }

    @Test
    void deleteTodoWithNonExistentIdTest() {
        Long nonExistentId = 22L;

        assertThrows(ComponentNotFoundException.class, () -> todoService.deleteTodo(nonExistentId));
    }

    @ParameterizedTest
    @MethodSource("prepareFirstTodoToUpdate")
    void updateTodoWithNonExistentIdTest(Todo todo) {
        Long nonExistentId = 22L;

        assertThrows(ComponentNotFoundException.class, () -> todoService.updateTodo(nonExistentId, todo));
    }

    @ParameterizedTest
    @MethodSource("prepareFirstTodo")
    void findTodoByIdTest(Todo expectedTodo) {
        Todo actualTodo = todoService.findTodoById(FIRST_ID);

        assertEquals(expectedTodo, actualTodo);
    }

    public static Stream<Arguments> prepareTodoToSave() {
        Todo todo = new Todo();
        todo.setName("New");
        todo.setOrderNumber(4L);
        return Stream.of(Arguments.of(todo));
    }

    public static Stream<Arguments> prepareTodoWithExistedNameToSave() {
        Todo todo = new Todo();
        todo.setName("First");
        return Stream.of(Arguments.of(todo));
    }

    public static Stream<Arguments> prepareAllTodos() {
        return Stream.of(Arguments.of(List.of(
                createFirstTodo(),
                createSecondTodo(),
                createThirdTodo()
        )));
    }

    public static Stream<Arguments> prepareAllTodosWithNewTodo() {
        Todo todo = new Todo();
        todo.setName("Fourth");
        todo.setDescription("Fourth");
        Priority priority = new Priority();
        priority.setId(FIRST_ID);
        priority.setWeight(1);
        todo.setPriority(priority);
        todo.setDate(LocalDate.of(2023, 7, 14));
        return Stream.of(Arguments.of(List.of(
                createFirstTodo(),
                createSecondTodo(),
                createThirdTodo(),
                createFourthTodo()
        ), todo));
    }

    public static Stream<Arguments> prepareFirstTodoToUpdate() {
        Todo todo = createFirstTodo();
        todo.setName("Updated");
        return Stream.of(Arguments.of(todo));
    }

    public static Stream<Arguments> prepareFirstTodoWithExistedNameToUpdate() {
        Todo todo = createFirstTodo();
        todo.setName("Second");
        return Stream.of(Arguments.of(todo));
    }

    public static Stream<Arguments> prepareSwappedTodos() {
        Todo todo1 = createFirstTodo();
        todo1.setOrderNumber(2L);
        Todo todo2 = createSecondTodo();
        todo2.setOrderNumber(1L);
        return Stream.of(Arguments.of(List.of(
                todo2,
                todo1,
                createThirdTodo()
        )));
    }

    public static Stream<Arguments> prepareFirstTodo() {
        return Stream.of(Arguments.of(createFirstTodo()));
    }

    private static Todo createFirstTodo() {
        Todo todo = new Todo();
        todo.setId(FIRST_ID);
        todo.setName("First");
        todo.setDescription("First");
        todo.setOrderNumber(1L);
        todo.setDate(LocalDate.of(2023, 7, 14));
        Priority priority = new Priority();
        priority.setId(FIRST_ID);
        priority.setWeight(1);
        priority.setName("First");
        todo.setPriority(priority);
        return todo;
    }

    private static Todo createSecondTodo() {
        Todo todo = new Todo();
        todo.setId(SECOND_ID);
        todo.setName("Second");
        todo.setDescription("Second");
        todo.setOrderNumber(2L);
        todo.setDate(LocalDate.of(2023, 7, 14));
        Priority priority = new Priority();
        priority.setId(SECOND_ID);
        priority.setWeight(2);
        priority.setName("Second");
        todo.setPriority(priority);
        return todo;
    }

    private static Todo createThirdTodo() {
        Todo todo = new Todo();
        todo.setId(THIRD_ID);
        todo.setName("Third");
        todo.setDescription("Third");
        todo.setOrderNumber(3L);
        todo.setDate(LocalDate.of(2023, 7, 14));
        Priority priority = new Priority();
        priority.setId(FIRST_ID);
        priority.setWeight(1);
        priority.setName("First");
        todo.setPriority(priority);
        return todo;
    }

    private static Todo createFourthTodo() {
        Todo todo = new Todo();
        todo.setId(FOURTH_ID);
        todo.setName("Fourth");
        todo.setDescription("Fourth");
        todo.setOrderNumber(4L);
        todo.setDate(LocalDate.of(2023, 7, 14));
        Priority priority = new Priority();
        priority.setId(FIRST_ID);
        priority.setWeight(1);
        priority.setName("First");
        todo.setPriority(priority);
        return todo;
    }
}
