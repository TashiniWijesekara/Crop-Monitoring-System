package org.example.cropmonitoringsystembackend.controller;

import jakarta.validation.Valid;
import org.example.cropmonitoringsystembackend.dto.impl.UserDTO;
import org.example.cropmonitoringsystembackend.service.UserService;
import org.example.cropmonitoringsystembackend.util.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v0/user")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<UserDTO> getAllCustomer(){
        return userService.getAllUser();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    UserDTO saveUser(@Valid @RequestBody UserDTO userDTO){
        return userService.saveUser(userDTO);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateCustomer(@Valid @RequestBody UserDTO userDTO){
        userService.updateUser(userDTO.getEmail(),userDTO);
    }

    @DeleteMapping(value = "/{email}",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteCustomer(@PathVariable("email") String email){
        userService.deleteUser(email);
    }

    @PatchMapping(value = "/{email}/{role}",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    UserDTO getCustomer(@PathVariable("email") String email, @PathVariable("role") Role role){
        return userService.getUserDetails(email ,role);
    }
}