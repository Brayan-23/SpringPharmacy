package com.generation.pharmacy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @NotBlank(message = "The name attribute is mandatory!")
  private String name;

  @NotBlank(message = "The email attribute is mandatory!")
  @Email(message = "The usr attribute must be a valid email!")
  private String email;

  @NotBlank(message = "The password attribute is mandatory!")
  private String password;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
  @JsonIgnoreProperties("user")
  private List<Product> products;

  public User() {}

  public User(Long id, String name, String email, String password, List<Product> products) {
    Id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.products = products;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }
}
