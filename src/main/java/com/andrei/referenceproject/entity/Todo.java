package com.andrei.referenceproject.entity;

import com.andrei.referenceproject.util.Utils;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Todo {
    private Long id;
    private String name;
    private String description;
    private Priority priority;
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Todo todo = (Todo) o;
        return Utils.equalsForNullableField(id, todo.id) && name.equals(todo.name) && Utils.equalsForNullableField(description, todo.description)
                && Utils.equalsForNullableField(priority, todo.priority) && Utils.equalsForNullableField(date, todo.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, priority, date);
    }
}
