package com.example.crudWithSolid.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.crudWithSolid.entity.User;
import com.example.crudWithSolid.model.JsonResponse;
import com.example.crudWithSolid.repository.UserRepository;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/users") // This means URL's start with /demo (after Application path)
public class UserController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @PostMapping(path = "/") 
    public @ResponseBody ResponseEntity<JsonResponse> addNewUser(@RequestParam String name,
            @RequestParam String email) {

        if (!userRepository.findByEmail(email).isEmpty()) {
            return new ResponseEntity<>(new JsonResponse("Already exists user with email"), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);

        return new ResponseEntity<>(new JsonResponse("User saved with success"), HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path = "/")
    public @ResponseBody Iterable<User> findUserByNameOrEmail(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "email", defaultValue = "") String email) {

        if (!name.isBlank()) {
            return userRepository.findByName(name);
        }
        // This returns a JSON or XML with the users
        return userRepository.findByEmail(email);
    }

    @GetMapping(path = "/{Id}")
    public ResponseEntity<User> findUser(
            @PathVariable(value = "Id") Integer id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User getUser = user.get();

            return new ResponseEntity<>(getUser, HttpStatus.OK);
        }

        // This returns a JSON or XML with the users
        return new ResponseEntity<>(new JsonResponse("User not found"), HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/{Id}")
    public ResponseEntity<JsonResponse> updateUser(
            @PathVariable(value = "Id") Integer id,
            @RequestParam String name,
            @RequestParam String email) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User updateUser = user.get();

            updateUser.setName(name);
            updateUser.setEmail(email);
            userRepository.save(updateUser);

            return new ResponseEntity<>(new JsonResponse("User update with success"), HttpStatus.OK);
        }

        // This returns a JSON or XML with the users
        return new ResponseEntity<>(new JsonResponse("User not found"), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/{Id}")
    public ResponseEntity<JsonResponse> deleteUser(@PathVariable(value = "Id") Integer id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {

            userRepository.deleteById(id);

            return new ResponseEntity<>(new JsonResponse("User delete with success"), HttpStatus.OK);
        }

        // This returns a JSON or XML with the users
        return new ResponseEntity<>(new JsonResponse("User not found"), HttpStatus.NOT_FOUND);
    }
}