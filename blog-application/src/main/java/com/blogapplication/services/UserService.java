package com.blogapplication.services;

import com.blogapplication.payloads.UserDto;
import java.util.List;

public interface UserService {

    UserDto registerNewUser(UserDto user);

    UserDto createUser(UserDto user); //using dto we are taking only the required data so that the main data is not exposed.
    UserDto updateUser(UserDto user, Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void deleteUser(Integer userId);
}
