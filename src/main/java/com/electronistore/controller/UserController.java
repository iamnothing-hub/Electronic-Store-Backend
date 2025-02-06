package com.electronistore.controller;


import com.electronistore.payload.ApiResponseMessage;
import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.UserDto;
import com.electronistore.payload.ImageResponse;
import com.electronistore.service.ImageService;
import com.electronistore.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${user.profile.image.path}")
    private String uploadImagePath;

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
//    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
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
*       So we will create ApiResponseMessage class in payload package
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

//    Upload user image
    @PostMapping("/image/{userId}")
    public  ResponseEntity<ImageResponse> uploadImage(@RequestParam(value = "userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        String imageName = imageService.uploadImage(image, uploadImagePath);  // upload image path k liye application.properties mein configuration hua hai.

//        Pahle user ko get karenge aur fir updateUser method ke help se user ko update kar denge
        UserDto userDto = userService.getUserById(userId);
        userDto.setImageName(imageName);
        userService.updateUser(userDto,userId);

//        image response prepare karenge
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .message("User image added successfully!!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(imageResponse,HttpStatus.OK);

    }

//    serve(Get) image means Download
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getUserById(userId);
        logger.info("User Detail {}: ", user);
        InputStream resource = imageService.getResource(uploadImagePath, user.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());

    }


}
