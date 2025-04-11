package com.electronistore.controller;


import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.RoleDto;
import com.electronistore.dto.UserDto;
import com.electronistore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc   // It will create MockMvc bean
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private UserDto userDto;
    private RoleDto roleDto;
    @BeforeEach
    public void init(){

        roleDto = RoleDto.builder().roleId("vyufssstrgcf").roleName("NORMAL").build();

        userDto = UserDto.builder()
                .name("Amit")
                .email("amit@gmail.com")
                .about("Full Stack Developer")
                .roles(List.of(roleDto))
                .gender("Male")
                .password("Amit@1234")
                .imageName("abc.png")
                .build();
    }

    @Test
    public void createUserTest() throws Exception {

        //It will take => /users + POST method + user data as JSON Format
        // Return => User data as JSON + Status CREATED

        Mockito.when(userService.createUser(Mockito.any())).thenReturn(userDto);

        // Actual request test URL
       this.mockMvc.perform(
               MockMvcRequestBuilders.post("/users")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(convertObjectToJSONString(userDto))
                       .contentType(MediaType.APPLICATION_JSON))

               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").exists())
               .andExpect(jsonPath("$.email").value("amit@gmail.com"));


    }

    private String convertObjectToJSONString(Object userDto) {

        try {
            return new ObjectMapper().writeValueAsString(userDto);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @Test
    public void updateUserTest() throws Exception {

        // User Dto data + Ok + Put

        String userId = "123";
        Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(userDto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/users/"+userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWl0a3VtYXJAZ21haWwuY29tIiwiaWF0IjoxNzQwODg5MTU0LCJleHAiOjE3NDA4OTA5NTR9.AdkOO-qK4Tmuztb1OIzStrsJ_x2umq9OKmn7A9rCf08")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJSONString(userDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());


    }


    @Test
    public  void getAllUsersTest() throws Exception {

        UserDto userDto1 = UserDto.builder()
                .name("Amit Kumar")
                .email("amitkumar@gmail.com")
                .about("Full Stack Developer")
                .roles(List.of(roleDto))
                .gender("Male")
                .password("Amit@1234")
                .imageName("abc.png")
                .build();

        UserDto userDto2 = UserDto.builder()
                .name("Shailesh")
                .email("shailesh@gmail.com")
                .about("Full Stack Developer")
                .roles(List.of(roleDto))
                .gender("Male")
                .password("Shailesh@1234")
                .imageName("abc.png")
                .build();

        PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(List.of(userDto,userDto1,userDto2));
        pageableResponse.setLastPage(false);
        pageableResponse.setPageNumber(10);
        pageableResponse.setPageSize(100);
        pageableResponse.setTotalPages(1000);


        Mockito.when(userService.getAllUsers(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON).
                content(convertObjectToJSONString(pageableResponse)).accept(MediaType.APPLICATION_JSON
                ))
                .andDo(print())
                .andExpect(status().isOk());

    }

}
