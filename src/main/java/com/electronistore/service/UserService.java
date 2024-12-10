package com.electronistore.service;

import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.UserDto;


import java.util.List;

public interface UserService {

//    Create user
    UserDto createUser(UserDto userDto);

//    Update
    UserDto updateUser(UserDto userDto, String userId);

//    Delete
    void deleteUser(String userId);

//    Get all users
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

//    Get user by id
    UserDto getUserById(String userId);

//    Get user by email id
    UserDto getUserByEmail(String email);

//    Search user
    List<UserDto> searchUser(String keyword);
}
