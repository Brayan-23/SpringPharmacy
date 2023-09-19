package com.generation.pharmacy.controller;

import com.generation.pharmacy.model.Product;
import com.generation.pharmacy.repository.CategoryRepository;
import com.generation.pharmacy.repository.ProductRepository;
import com.generation.pharmacy.utils.ValuesBetween;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @GetMapping
  public ResponseEntity<List<Product>> getAll() {
    return ResponseEntity.ok(productRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getById(@PathVariable Long id) {
    return productRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @GetMapping("/values")
  public ResponseEntity<List<Product>> getById(@RequestBody ValuesBetween valuesBetween) {
    return ResponseEntity.ok(productRepository.findByValueBetween(valuesBetween.getValue1(), valuesBetween.getValue2()));
  }

  @PostMapping
  ResponseEntity<Product> create(@Valid @RequestBody Product product){
    if(categoryRepository.existsById(product.getCategory().getId())){
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(productRepository.save(product));
    }

    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Theme not Exists", null);
  }

  @PutMapping
  ResponseEntity<Product> update(@Valid @RequestBody Product product){
    if (productRepository.existsById(product.getId())){
      if (categoryRepository.existsById(product.getCategory().getId())){
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(product));
      }

      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not Exists", null);
    }
    return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  void delete(@PathVariable Long id){
    Optional<Product> post = productRepository.findById(id);

    if (post.isEmpty()) throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
    productRepository.deleteById(id);
  }

}