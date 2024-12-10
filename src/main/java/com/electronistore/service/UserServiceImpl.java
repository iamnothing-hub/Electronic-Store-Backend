package com.electronistore.service;

import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.UserDto;
import com.electronistore.entity.User;
import com.electronistore.exception.ResourceNotFoundException;
import com.electronistore.helper.PageHelper;
import com.electronistore.repository.UserRepository;
import lombok.experimental.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private static Integer count = 10000;
    @Autowired
    private UserRepository userRepository;

//    Model mapper is coming from config.
    @Autowired
    private ModelMapper mapper;


    @Override
    public UserDto createUser(UserDto userDto) {

//        Generate random id
//        String userId = UUID.randomUUID().toString();

        userDto.setUserId("ELC"+ ++count);

        User user = prepareEntity(userDto);
        User savedUser = userRepository.save(user);

        UserDto newUserDto = prepareDto(savedUser);
        return newUserDto;
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found for given Id!!"));
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());

        User updatedUser = userRepository.save(user);

        return prepareDto(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not Found!!"));
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

//        Sort sort = Sort.by(sortBy);
//        conditionaly render with ascending or descending order
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        //-> Pagination Concept
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> response = PageHelper.getPageableResponse(page,UserDto.class); //-> PageHelper is class in Helper Package
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found for given Id"));
        return prepareDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User Not Found for given Email Id!!"));
        return prepareDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
       List<User> users =  userRepository.findByNameContaining(keyword);

       List<UserDto> dtos = users.stream().map(user -> prepareDto(user)).collect(Collectors.toList());
//        List<UserDto> dtos = users.stream().map(this::prepareDto).toList();
        return dtos;
    }



    public User prepareEntity(UserDto userDto){

       /* User user = User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .about(userDto.getAbout())
                .email(userDto.getEmail())
                .gender(userDto.getGender())
                .password(userDto.getPassword())
                .imageName(userDto.getImageName())
                .build();*/
        return mapper.map(userDto, User.class);
    }

    public  UserDto prepareDto(User user){

        /*UserDto userDto = UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .about(user.getAbout())
                .email(user.getEmail())
                .gender(user.getGender())
                .imageName(user.getImageName())
                .password(user.getPassword())
                .build();*/

//      todo  map(object, source destination class).
        return mapper.map(user, UserDto.class);
    }
}
