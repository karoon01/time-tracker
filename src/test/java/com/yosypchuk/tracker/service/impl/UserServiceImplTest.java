package com.yosypchuk.tracker.service.impl;

import com.yosypchuk.tracker.exception.EntityAlreadyExistException;
import com.yosypchuk.tracker.exception.EntityNotFoundException;
import com.yosypchuk.tracker.model.DTO.UserDTO;
import com.yosypchuk.tracker.model.entity.User;
import com.yosypchuk.tracker.repository.UserRepository;
import com.yosypchuk.tracker.testUtils.TestUserDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final Integer PAGE = 1;
    private static final Integer SIZE = 5;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterUser() {
        //given
        User expectedUser = TestUserDataUtil.createUser();
        UserDTO userBody = TestUserDataUtil.createUserDto();

        when(userRepository.findByEmail(TestUserDataUtil.MOCK_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(expectedUser);

        //when
        UserDTO actualUser = userService.registerUser(userBody);

        //then
        assertEquals(passwordEncoder.encode(TestUserDataUtil.MOCK_PASSWORD), passwordEncoder.encode(actualUser.getPassword()));
        assertThat(actualUser, allOf(
                hasProperty("id", equalTo(userBody.getId())),
                hasProperty("firstName",equalTo(userBody.getFirstName())),
                hasProperty("lastName",equalTo(userBody.getLastName())),
                hasProperty("email",equalTo(userBody.getEmail())),
                hasProperty("role", equalTo(userBody.getRole()))
        ));
    }

    @Test
    void testRegisterUserThrowsExceptionIfUserIsAlreadyExist() {
        //given
        User user = User.builder().email(TestUserDataUtil.MOCK_EMAIL).build();
        UserDTO userDto = UserDTO.builder().email(TestUserDataUtil.MOCK_EMAIL).build();

        //when
        when(userRepository.findByEmail(TestUserDataUtil.MOCK_EMAIL)).thenReturn(Optional.of(user));

        //then
        assertThrows(EntityAlreadyExistException.class, () -> userService.registerUser(userDto));
    }

    @Test
    void testUpdateUser() {
        //given
        User expectedUser = TestUserDataUtil.createUser();
        UserDTO userBody = TestUserDataUtil.createUpdatedUserDto();

        when(userRepository.findById(TestUserDataUtil.MOCK_ID)).thenReturn(Optional.of(expectedUser));
        when(userRepository.save(any())).thenReturn(expectedUser);

        //when
        UserDTO updatedUser = userService.updateUser(TestUserDataUtil.MOCK_ID, userBody);

        //then
        assertThat(updatedUser, allOf(
                hasProperty("id", equalTo(userBody.getId())),
                hasProperty("firstName",equalTo(userBody.getFirstName())),
                hasProperty("lastName",equalTo(userBody.getLastName())),
                hasProperty("email",equalTo(userBody.getEmail())),
                hasProperty("role", equalTo(userBody.getRole()))
        ));
    }

    @Test
    void testUpdateUserThrowsExceptionIfUserDoesntExist() {
        //given
        UserDTO userBody = UserDTO.builder().email(TestUserDataUtil.MOCK_EMAIL).build();

        //when
        when(userRepository.findById(TestUserDataUtil.MOCK_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(TestUserDataUtil.MOCK_ID, userBody));
    }

    @Test
    void testGetUserById() {
        //given
        User expectedUser = TestUserDataUtil.createUser();
        when(userRepository.findById(TestUserDataUtil.MOCK_ID)).thenReturn(Optional.of(expectedUser));

        //when
        UserDTO actualUser = userService.getUser(TestUserDataUtil.MOCK_ID);

        //then
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertThat(actualUser, allOf(
                hasProperty("id", equalTo(actualUser.getId())),
                hasProperty("firstName", equalTo(actualUser.getFirstName())),
                hasProperty("lastName", equalTo(actualUser.getLastName())),
                hasProperty("email",equalTo(actualUser.getEmail())),
                hasProperty("role", equalTo(actualUser.getRole()))
        ));
    }

    @Test
    void testGetUserByIdThrowsExceptionIfUserDoesntExist() {
        //when
        when(userRepository.findById(TestUserDataUtil.MOCK_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> userService.getUser(TestUserDataUtil.MOCK_ID));
    }

    @Test
    void testGetByEmail() {
        //given
        User expectedUser = TestUserDataUtil.createUser();
        when(userRepository.findByEmail(TestUserDataUtil.MOCK_EMAIL)).thenReturn(Optional.of(expectedUser));

        //when
        UserDTO actualUser = userService.getUserByEmail(TestUserDataUtil.MOCK_EMAIL);

        //then
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertThat(actualUser, allOf(
                hasProperty("id", equalTo(actualUser.getId())),
                hasProperty("firstName", equalTo(actualUser.getFirstName())),
                hasProperty("lastName", equalTo(actualUser.getLastName())),
                hasProperty("email",equalTo(actualUser.getEmail())),
                hasProperty("role", equalTo(actualUser.getRole()))
        ));
    }

    @Test
    void testGetUserByEmailThrowsExceptionIfUserDoesntExist() {
        //when
        when(userRepository.findByEmail(TestUserDataUtil.MOCK_EMAIL)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(TestUserDataUtil.MOCK_EMAIL));
    }

    @Test
    void testGetAllUsers() {
        //given
        User expectedUser = TestUserDataUtil.createUser();
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE);
        List<User> usersList = Collections.singletonList(expectedUser);
        Page<User> usersPage = new PageImpl<>(usersList);
        when(userRepository.findAll(pageRequest)).thenReturn(usersPage);

        //when
        List<UserDTO> users = userService.getAllUsers();

        //then
        assertThat(users, hasSize(1));
    }

    @Test
    void testDeleteUser() {
        //given
        User user = TestUserDataUtil.createUser();
        when(userRepository.findById(TestUserDataUtil.MOCK_ID)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any());

        //when
        userService.deleteUser(TestUserDataUtil.MOCK_ID);

        //then
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUserThrowsExceptionIfUserDoesntExist() {
        //when
        when(userRepository.findById(TestUserDataUtil.MOCK_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(TestUserDataUtil.MOCK_ID));
    }

}