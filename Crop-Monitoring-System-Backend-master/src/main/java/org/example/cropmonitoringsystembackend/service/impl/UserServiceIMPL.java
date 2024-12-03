package org.example.cropmonitoringsystembackend.service.impl;

import org.example.cropmonitoringsystembackend.dao.UserDAO;
import org.example.cropmonitoringsystembackend.dto.impl.UserDTO;
import org.example.cropmonitoringsystembackend.entity.impl.User;
import org.example.cropmonitoringsystembackend.exception.DublicateRecordException;
import org.example.cropmonitoringsystembackend.exception.NotFoundException;
import org.example.cropmonitoringsystembackend.service.UserService;
import org.example.cropmonitoringsystembackend.util.Role;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceIMPL implements UserService {

    UserDAO userDAO;
    ModelMapper modelMapper;

    public UserServiceIMPL(UserDAO userDAO, ModelMapper modelMapper) {
        this.userDAO = userDAO;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetailsService userDetailService() {
        return username -> userDAO.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    @Override
    public List<UserDTO> getAllUser() {
        return userDAO.findAll().stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
    }

    @Override
    public UserDTO getUserDetails(String email, Role role) {
        if (!userDAO.existsByEmail(email)) {
            throw new NotFoundException("User email :" + email + " Not Found!");
        }
        return modelMapper.map(userDAO.findByEmailAndRole(email, role), UserDTO.class);
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        if (userDAO.existsByEmail(userDTO.getEmail())) {
            throw new DublicateRecordException("This User " + userDTO.getEmail() + " already have an account.");
        }
        return modelMapper.map(userDAO.save(modelMapper.map(userDTO, User.class)), UserDTO.class);
    }

    @Override
    public void updateUser(String email, UserDTO userDTO) {
        User existingUser = userDAO.findByEmailAndRole(email, userDTO.getRole());

        if (existingUser.getPassword().isEmpty()) {
            throw new NotFoundException("User email :" + email + "Not Found...");
        }

        existingUser.setPassword(userDTO.getPassword());
        existingUser.setRole(userDTO.getRole());

        userDAO.save(existingUser);
    }

    @Override
    public void deleteUser(String email) {
        if (!userDAO.existsByEmail(email)) {
            throw new NotFoundException("User email :" + email + "Not Found...");
        }
        userDAO.deleteByEmail(email);
    }
}
