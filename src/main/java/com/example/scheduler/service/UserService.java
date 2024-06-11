package com.example.scheduler.service;

import com.example.scheduler.dtos.UserDto;
import com.example.scheduler.mappers.UserMapper;
import com.example.scheduler.model.UserEntity;
import com.example.scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;


    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity updateUser(Long id, UserEntity userEntityDetails) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity != null) {
            return userRepository.save(userEntityDetails);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto findByEmail(String subject) {
        UserEntity user  = this.userRepository.findByEmail(subject).orElseThrow(() -> new NotFoundException("User not found"));

        return userMapper.toUserDto(user);
    }

    public Optional<UserEntity> findByEmailOptional(String subject) {
        return this.userRepository.findByEmail(subject);
    }
}
