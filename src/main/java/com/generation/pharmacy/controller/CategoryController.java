package com.generation.pharmacy.controller;

import java.util.List;
import java.util.Optional;

import com.generation.pharmacy.model.Category;
import com.generation.pharmacy.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {

  @Autowired
  private CategoryRepository categoryRepository;

  @GetMapping
  public ResponseEntity<List<Category>> getAll(){
    return ResponseEntity.ok(categoryRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Category> getById(@PathVariable Long id){
    return categoryRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @GetMapping("/description/{description}")
  public ResponseEntity<List<Category>> getByTitle(@PathVariable String description){
    return ResponseEntity.ok(categoryRepository
        .findAllByDescriptionContainingIgnoreCase(description));
  }

  @PostMapping
  public ResponseEntity<Category> post(@Valid @RequestBody Category theme){
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(categoryRepository.save(theme));
  }

  @PutMapping
  public ResponseEntity<Category> put(@Valid @RequestBody Category theme){
    return categoryRepository.findById(theme.getId())
        .map(response -> ResponseEntity.status(HttpStatus.OK)
            .body(categoryRepository.save(theme)))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    Optional<Category> category = categoryRepository.findById(id);

    if(category.isEmpty())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    categoryRepository.deleteById(id);
  }

}