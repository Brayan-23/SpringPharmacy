package com.generation.pharmacy.repository;

import com.generation.pharmacy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Long> {

  List<Product> findByValueBetween(Float value1, Float value2);
}
