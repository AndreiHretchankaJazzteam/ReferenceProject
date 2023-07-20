package com.andrei.referenceproject.entity;

import com.andrei.referenceproject.util.Utils;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "todo")
public class Todo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private String description;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "priority_id")
    private Priority priority;
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "order_number", nullable = false)
    private Long orderNumber;

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
