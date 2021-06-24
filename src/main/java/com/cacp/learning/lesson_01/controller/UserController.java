package com.cacp.learning.lesson_01.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.cacp.learning.lesson_01.entity.Post;
import com.cacp.learning.lesson_01.entity.User;
import com.cacp.learning.lesson_01.exception.UserNotFoundException;
import com.cacp.learning.lesson_01.persistence.UserHardcodePersistence;
import com.cacp.learning.lesson_01.repository.PostRepository;
import com.cacp.learning.lesson_01.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController()
public class UserController {

    @Autowired
    private UserHardcodePersistence userPersistence;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PostRepository postRepository;

    //GET
    @GetMapping("/users")
    public List<User> findAll(){
        return userRepository.findAll();
    }

    //GET by ID
    @GetMapping("/users/{id}")
    public EntityModel<User> getUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()){
            throw new UserNotFoundException("id " + id);
        }
        EntityModel<User> model = EntityModel.of(user.get());
        // CREATE a link to the method findAll() inside this class using HATEOAS
        WebMvcLinkBuilder lintToUser = linkTo(methodOn(this.getClass()).findAll());
        model.add(lintToUser.withRel("all-users"));
        return model;
    }

    //ADD
    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user);

        // CREATES a link to the new User in the response
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }
    
    @GetMapping("/users/{id}/posts")
    public List<Post> findAllpostsByUser(@PathVariable int id){
    	Optional<User> userOptional = userRepository.findById(id);
    	
    	if(!userOptional.isPresent())
    		throw new UserNotFoundException("id " + id);
    	
        return userOptional.get().getPosts();
    }
    
    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Object> addPost(@Valid @RequestBody Post post, @PathVariable int id){
    	Optional<User> userOptional = userRepository.findById(id);
    	
    	if(!userOptional.isPresent())
    		throw new UserNotFoundException("id " + id);
    	
    	User user = userOptional.get();
    	post.setUser(user);
    	
    	postRepository.save(post);

        // CREATES a link to the new User in the response
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
