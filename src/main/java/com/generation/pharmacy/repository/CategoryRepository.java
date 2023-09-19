package com.generation.pharmacy.repository;

import com.generation.pharmacy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository  extends JpaRepository<Category, Long> {

  List<Category> findAllByDescriptionContainingIgnoreCase(@Param("description") String description);

}
