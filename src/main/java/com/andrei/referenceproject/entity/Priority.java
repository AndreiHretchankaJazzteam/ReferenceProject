package com.andrei.referenceproject.entity;

import com.andrei.referenceproject.util.Utils;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Priority {
    private Long id;
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
        return Utils.equalsForNullableField(id, priority.id) && name.equals(priority.name) && Utils.equalsForNullableField(weight, priority.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, weight);
    }

    @Override
    public String toString() {
        return name;
    }
}
