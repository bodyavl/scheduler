package com.example.scheduler.service;

import com.example.scheduler.dtos.CredentialsDto;
import com.example.scheduler.dtos.SignUpDto;
import com.example.scheduler.dtos.UserDto;
import com.example.scheduler.exceptions.AppException;
import com.example.scheduler.mappers.UserMapper;
import com.example.scheduler.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public UserDto login(CredentialsDto credentialsDto) {
        UserEntity user = userService.findByEmailOptional(credentialsDto.email())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto) {
        Optional<UserEntity> optionalUser = userService.findByEmailOptional(userDto.email());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        UserEntity savedUser = userService.createUser(user);

        return userMapper.toUserDto(savedUser);
    }
}
