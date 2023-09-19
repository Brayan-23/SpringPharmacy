package com.generation.pharmacy.controller;

import java.util.List;
import java.util.Optional;

import com.generation.pharmacy.model.User;
import com.generation.pharmacy.model.UserLogin;
import com.generation.pharmacy.repository.UserRepository;
import com.generation.pharmacy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @GetMapping
  public ResponseEntity <List<User>> getAll(){

    return ResponseEntity.ok(userRepository.findAll());

  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getById(@PathVariable Long id) {
    return userRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/login")
  public ResponseEntity<UserLogin> authenticateUser(@RequestBody Optional<UserLogin> userLogin){

    return userService.authenticateUser(userLogin)
        .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }


  @PostMapping("/register")
  public ResponseEntity<User> postUser(@RequestBody @Valid User user) {

    return userService.registerUser(user)
        .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
        .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

  }

  @PutMapping("/update")
  public ResponseEntity<User> putUsuario(@Valid @RequestBody User user) {

    return userService.updateUser(user)
        .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

  }
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  void delete(@PathVariable Long id) {
    Optional<User> user = userRepository.findById(id);

    if (user.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    userRepository.deleteById(id);
  }
}