package com.andrei.referenceproject.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Todo {
    private String name;
    private String description;

    private Priority priority;

    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return name.equals(todo.name) && description.equals(todo.description) && priority.equals(todo.priority) && date.equals(todo.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, priority, date);
    }
}
