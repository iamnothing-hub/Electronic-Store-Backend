package com.electronistore.service.impl;

import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.UserDto;
import com.electronistore.entity.Role;
import com.electronistore.entity.User;
import com.electronistore.exception.ResourceNotFoundException;
import com.electronistore.helper.PageHelper;
import com.electronistore.repository.RoleRepository;
import com.electronistore.repository.UserRepository;
import com.electronistore.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static Integer count = 10000;
    @Autowired
    private UserRepository userRepository;


//    Model mapper is coming from config.
    @Autowired
    private ModelMapper mapper;

    // User ka image path ka instance lenge aur value application.properties mein se aayega
    @Value("${user.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto userDto) {

//        Generate random id
//        String userId = UUID.randomUUID().toString();

        userDto.setUserId("ELC"+ ++count);

        User user = prepareEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        /**
         * Pahle ham role get karenge agar role mil gya to thik otherwise ham ek by default NORMAL role banayenge
         *
         */
        Role role = new Role();
        role.setRoleId(UUID.randomUUID().toString());
        role.setRoleName("ROLE_NORMAL");

        Role roleNormal = roleRepository.findByRoleName("ROLE_NORMAL").orElse(role);
        // Set role to the user
        user.setRoles(List.of(roleNormal));
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

        /**
         * @return case 1: Agar user password update nahi karna chahta hai
         *                 tab old password ko userRepo se fetch karke update kar denge
         *
         * @return case 2: Agar user password ko update karna chahta hai tab use encrypt karke add kara denge
         */

//        if (!userDto.getPassword().equalsIgnoreCase(user.getPassword()))
//            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//        if(userDto.getPassword().isEmpty()) user.setPassword(user.getPassword());
        if(userDto.getPassword() != null && !userDto.getPassword().isEmpty()) user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//        else user.setPassword(user.getPassword());

        User updatedUser = userRepository.save(user);

        return prepareDto(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not Found!!"));

        // user ko delete karate time userImage ko bhi delete karna hoga

        String fullPath = imagePath + user.getImageName();

        // Ye path folder  mein se image ko remove karega
        try {
            Path path = Paths.get(fullPath);

            Files.delete(path);
        }
        catch (NoSuchFileException ex){
            logger.info("No image is found in folder!!");
            ex.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

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
