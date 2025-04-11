package com.electronistore.service;


import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.UserDto;
import com.electronistore.entity.Role;
import com.electronistore.entity.User;
import com.electronistore.repository.RoleRepository;
import com.electronistore.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//@ExtendWith((MockitoExtension.class))
@SpringBootTest
public class UserServiceTest {

    @Autowired
//    @InjectMocks
    private UserService userService;

    @Autowired
    private ModelMapper mapper;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private Role role;
    private String roleName;
    @BeforeEach
    public void init(){

        role = Role.builder().roleId("abcd").roleName("NORMAL").build();
        user = User.builder()
                .name("Amit Kumar")
                .email("amit@gmail.com")
                .about("This is testing purpose file")
                .gender("Male")
                .imageName("amit.jpeg")
                .password("Amit@123")
                .roles(List.of(role))
                .build();

        roleName = "NORMAL";
    }
    // Create User
    @Test
    public void createUserTest(){

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findByRoleName(Mockito.anyString())).thenReturn(Optional.ofNullable(role));

        UserDto user1 = userService.createUser(mapper.map(user, UserDto.class));

//        System.out.println(user1.getName());

        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Amit Kumar",user1.getName());
    }

    // Update User

    @Test
    public void updateUserTest(){

        String userId = "";

        UserDto userDto = UserDto.builder()
                .name("Amit Kumar")
                .email("amitkumar@gmail.com")
                .about("This is testing purpose file")
                .gender("Male")
                .imageName("amit.jpeg")
//                .password("Amit@1234")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(user));

//        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("EncryptedPassword");
        // ✅ Mock password encoding behavior
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto userDto1 = userService.updateUser(userDto, userId);

        // ✅ Verify that password was updated & encrypted
//        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(user.getPassword());

        System.out.println(userDto1.getName());

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getName(),userDto1.getName(), "Name is not updated in validation process");
//        Assertions.assertNotEquals("EncryptedPassword",userDto1.getPassword(),"User is not updated or encrypted");
    }


    @Test
    public void deleteUserTest(){
        String userId = "";

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));

        userService.deleteUser(userId);
        // Yahan pr kuchh return nhi ho raha hai to fir ham verify method use karenge to validate
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }

    // Get all users
    @Test
    public void getAllUsersTest(){

       User user1 = User.builder()
                .name("Shailesh")
                .email("shailesh@gmail.com")
                .about("This is testing purpose file")
                .gender("Male")
                .imageName("amit.jpeg")
                .password("Amit@123")
                .roles(List.of(role))
                .build();

       User user2 = User.builder()
                .name("Sunil")
                .email("sunil@gmail.com")
                .about("This is testing purpose file")
                .gender("Male")
                .imageName("amit.jpeg")
                .password("Amit@123")
                .roles(List.of(role))
                .build();

        List<User> userList = Arrays.asList(user,user1,user2);

        Page<User> page = new PageImpl(userList);

        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

/*        Sort sort = Sort.by("name").descending();

        Pageable pageable = PageRequest.of(1, 5, sort);
        PageableResponse<UserDto> allUsers = userService.getAllUsers(pageable);*/

        PageableResponse<UserDto> allUsers = userService.getAllUsers(1, 5, "name", "desc");

        Assertions.assertEquals(3, allUsers.getContent().size());

    }

    // get user by id test
    @Test
    public void getUserByIdTest(){
        String userId = "absghsav";
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(user));
        UserDto userById = userService.getUserById(userId);
        Assertions.assertNotNull(userById);
        Assertions.assertEquals(userById.getName(),user.getName());
    }

    @Test
    public void getUserByEmailTest(){
        String email = "amit@gmail.com";

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.ofNullable(user));

        UserDto userByEmail = userService.getUserByEmail(email);

        Assertions.assertNotNull(userByEmail);
        Assertions.assertEquals(userByEmail.getName(),user.getName(),"Name is not matched!!");
        Assertions.assertEquals(userByEmail.getEmail(),user.getEmail(),"Email is not matched!!");
    }

    @Test
    public void searchUserTest(){

        String searchKeyword = "Kumar";

        User user1 = User.builder()
                .name("Amit Kumar")
                .email("shailesh@gmail.com")
                .about("This is testing purpose file")
                .gender("Male")
                .imageName("amit.jpeg")
                .password("Amit@123")
                .roles(List.of(role))
                .build();

        User user2 = User.builder()
                .name("Sunil Kumar")
                .email("sunil@gmail.com")
                .about("This is testing purpose file")
                .gender("Male")
                .imageName("amit.jpeg")
                .password("Amit@123")
                .roles(List.of(role))
                .build();

        List<User> userList = Arrays.asList(user,user1,user2);


//        Mockito.when(userRepository.findByNameContaining(Mockito.anyString())).thenReturn(userList);
        Mockito.when(userRepository.findByNameContaining(searchKeyword)).thenReturn(userList);


        List<UserDto> userDtos = userService.searchUser(searchKeyword);
//        System.out.println(userDtos.size());
        Assertions.assertEquals(3,userDtos.size());
    }
}
