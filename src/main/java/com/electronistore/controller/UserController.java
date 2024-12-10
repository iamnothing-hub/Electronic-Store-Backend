package com.electronistore.controller;


import com.electronistore.dto.ApiResponseMessage;
import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.UserDto;
import com.electronistore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
//    Create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto){
        UserDto dto = userService.createUser(userDto);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);

    }
//    Update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto, @PathVariable("userId") String userId){
        UserDto  dto = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

//    get all users
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            //  -> Ye Pagination ka concept hai.
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy", defaultValue = "name",required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc",required = false) String sortDir

    ){
        PageableResponse<UserDto> userList = userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }
//    get by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getByUserId( @PathVariable String userId){
        UserDto dto = userService.getUserById(userId);
       return new ResponseEntity<>(dto,HttpStatus.OK);
    }
//    get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getByEmail(@PathVariable String email){
       UserDto userDto =  userService.getUserByEmail(email);
       return new ResponseEntity<>(userDto,HttpStatus.OK);
    }



//    delete user
/*todo
        When We are returning message then we have to return in json formate
*       Iske liye hame ek new class banana hoga for creating the object
*       So we will create ApiResponseMessage class in DTO package
* */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        ApiResponseMessage message =  ApiResponseMessage.builder()
                .message("User removed!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }



// Search User
    @GetMapping("/search/{keywords}")
    public  ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords){
        return new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
    }


}
