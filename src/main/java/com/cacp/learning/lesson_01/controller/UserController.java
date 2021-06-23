package com.cacp.learning.lesson_01.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.cacp.learning.lesson_01.entity.User;
import com.cacp.learning.lesson_01.exception.UserNotFoundException;
import com.cacp.learning.lesson_01.persistence.UserHardcodePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController()
public class UserController {

    @Autowired
    private UserHardcodePersistence userPersistence;

    //GET
    @GetMapping("/users")
    public List<User> findAll(){
        return userPersistence.findAll();
    }

    //GET by ID
    @GetMapping("/users/{id}")
    public EntityModel<User> getUser(@PathVariable int id){
        User user = userPersistence.findUser(id);
        if(user == null){
            throw new UserNotFoundException("id " + id);
        }
        EntityModel<User> model = EntityModel.of(user);
        // CREATE a link to the method findAll() inside this class using HATEOAS
        WebMvcLinkBuilder lintToUser = linkTo(methodOn(this.getClass()).findAll());
        model.add(lintToUser.withRel("all-users"));
        return model;
    }

    //ADD
    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@Valid @RequestBody User user){
        User savedUser = userPersistence.addUser(user);

        // CREATES a link to the new User in the response
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable int id){
        User user = userPersistence.deleteUser(id);
        if(user == null){
            throw new UserNotFoundException("id " + id);
        }
        return user;
    }
}
