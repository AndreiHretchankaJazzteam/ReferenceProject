package com.andrei.referenceproject.repository;

import com.andrei.referenceproject.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query(value = "SELECT max(t.orderNumber) FROM Todo t")
    Long getMaxOrderNumber();

    List<Todo> findAllByOrderByOrderNumberAsc();
}
