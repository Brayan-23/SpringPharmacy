package com.generation.pharmacy.service;

import java.util.Objects;
import java.util.Optional;

import com.generation.pharmacy.model.User;
import com.generation.pharmacy.model.UserLogin;
import com.generation.pharmacy.repository.UserRepository;
import com.generation.pharmacy.secueity.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  public Optional<User> registerUser(User user) {

    if (userRepository.findByEmail(user.getEmail()).isPresent())
      return Optional.empty();

    user.setPassword(passwordEncrypt(user.getPassword()));

    return Optional.of(userRepository.save(user));

  }

  public Optional<User> updateUser(User user) {

    if(userRepository.findById(user.getId()).isPresent()) {

      Optional<User> searchUser = userRepository.findByEmail(user.getEmail());

      if ( (searchUser.isPresent()) && (!Objects.equals(searchUser.get().getId(), user.getId())))
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not exists!", null);

      user.setPassword(passwordEncrypt(user.getPassword()));

      return Optional.of(userRepository.save(user));

    }

    return Optional.empty();

  }

  public Optional<UserLogin> authenticateUser(Optional<UserLogin> userLogin) {

    var credenciais = new UsernamePasswordAuthenticationToken(userLogin.get().getEmail(), userLogin.get().getPassword());

    Authentication authentication = authenticationManager.authenticate(credenciais);

    if (authentication.isAuthenticated()) {

      Optional<User> usuario = userRepository.findByEmail(userLogin.get().getEmail());

      if (usuario.isPresent()) {

        userLogin.get().setId(usuario.get().getId());
        userLogin.get().setName(usuario.get().getName());
        userLogin.get().setToken(generateToken(userLogin.get().getEmail()));
        userLogin.get().setPassword("");

        return userLogin;

      }

    }

    return Optional.empty();

  }

  private String passwordEncrypt(String password) {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    return encoder.encode(password);

  }

  private String generateToken(String user) {
    return "Bearer " + jwtService.generateToken(user);
  }

}