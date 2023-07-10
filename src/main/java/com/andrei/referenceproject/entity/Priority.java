package com.andrei.referenceproject.entity;

import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class Priority {
    private String name;
    private Integer weight;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Priority priority = (Priority) o;
        return name.equals(priority.name) && weight.equals(priority.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight);
    }

    @Override
    public String toString() {
        return name;
    }
}
