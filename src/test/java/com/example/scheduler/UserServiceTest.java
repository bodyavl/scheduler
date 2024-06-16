package com.example.scheduler;

import com.example.scheduler.dtos.UserDto;
import com.example.scheduler.mappers.UserMapper;
import com.example.scheduler.model.UserEntity;
import com.example.scheduler.repository.UserRepository;
import com.example.scheduler.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.webjars.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");

        userDto = new UserDto();
        userDto.setEmail("test@example.com");
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity));

        List<UserEntity> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(userEntity, users.get(0));
    }

    @Test
    void getUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        UserEntity user = userService.getUserById(1L);

        assertNotNull(user);
        assertEquals(userEntity, user);
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserEntity user = userService.getUserById(1L);

        assertNull(user);
    }

    @Test
    void createUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity savedUser = userService.createUser(userEntity);

        assertNotNull(savedUser);
        assertEquals(userEntity, savedUser);
    }

    @Test
    void updateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity updatedUser = userService.updateUser(1L, userEntity);

        assertNotNull(updatedUser);
        assertEquals(userEntity, updatedUser);
    }

    @Test
    void updateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserEntity updatedUser = userService.updateUser(1L, userEntity);

        assertNull(updatedUser);
    }

    @Test
    void deleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        when(userMapper.toUserDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto foundUser = userService.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals(userDto, foundUser);
    }

    @Test
    void findByEmail_NotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findByEmail("test@example.com"));
    }

    @Test
    void findByEmailOptional() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> foundUser = userService.findByEmailOptional("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(userEntity, foundUser.get());
    }
}
