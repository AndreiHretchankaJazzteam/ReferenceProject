package com.andrei.referenceproject.service.impl;

import com.andrei.referenceproject.entity.Priority;
import com.andrei.referenceproject.entity.Todo;
import com.andrei.referenceproject.exception.ComponentExistedValuesException;
import com.andrei.referenceproject.exception.ComponentNotFoundException;
import com.andrei.referenceproject.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceImplTest {
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        List<Todo> todos = new ArrayList<>();
        todos.add(createFirstTodo());
        todos.add(createSecondTodo());
        todoService = new TodoServiceImpl(todos);
    }

    @ParameterizedTest
    @MethodSource("prepareTodoToSave")
    void saveTodoTest(Todo todo) {
        Todo expectedTodo = new Todo();
        expectedTodo.setId(1L);
        expectedTodo.setName("Third");

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
        todoService.swapTodo(0, 1);
        List<Todo> actualTodos = todoService.findAllTodos();

        assertEquals(expectedTodos, actualTodos);
    }

    @Test
    void deleteTodoTest() {
        Long id = -1L;
        todoService.deleteTodo(id);

        assertThrows(ComponentNotFoundException.class, () -> todoService.findTodoById(id));
    }

    @ParameterizedTest
    @MethodSource("prepareFirstTodo")
    void findTodoByIdTest(Todo expectedTodo) {
        Todo actualTodo = todoService.findTodoById(-1L);

        assertEquals(expectedTodo, actualTodo);
    }

    public static Stream<Arguments> prepareTodoToSave() {
        Todo todo = new Todo();
        todo.setName("Third");
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
                createSecondTodo()
        )));
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
        return Stream.of(Arguments.of(List.of(
                createSecondTodo(),
                createFirstTodo()
        )));
    }

    public static Stream<Arguments> prepareFirstTodo() {
        return Stream.of(Arguments.of(createFirstTodo()));
    }

    private static Todo createFirstTodo() {
        return new Todo(
                -1L,
                "First",
                "First",
                new Priority(-1L, "First priority", 1),
                LocalDate.of(2023, 7, 12)

        );
    }

    private static Todo createSecondTodo() {
        return new Todo(
                -2L,
                "Second",
                "Second",
                new Priority(-2L, "Second priority", 2),
                LocalDate.of(2023, 7, 12)
        );
    }
}