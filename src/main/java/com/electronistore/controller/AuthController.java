package com.electronistore.controller;

import com.electronistore.dto.JwtRequest;
import com.electronistore.dto.JwtResponse;
import com.electronistore.dto.UserDto;
import com.electronistore.entity.User;
import com.electronistore.exception.BadRequestException;
import com.electronistore.security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * todo: Step 7:
 *              Create login api to accept username and password and return token if username and password is correct
 * */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper mapper;
    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){

        logger.info("Username {}, Password {} ", request.getUsername(), request.getPassword());

        // ab ham username aur password ko database se authenticate karte hain using authenticationProvider
        this.doAuthentication(request.getUsername(),request.getPassword());
        // generate token...
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtHelper.generateToken(userDetails);
        // send response...
        JwtResponse jwtResponse = JwtResponse.builder().token(token).userDto(mapper.map(userDetails, UserDto.class)).build();


        return ResponseEntity.ok(jwtResponse);
    }

    private void doAuthentication(String username, String password) {

        try{
            Authentication authentication = new UsernamePasswordAuthenticationToken(username,password);
            authenticationManager.authenticate(authentication);
        }
        catch (BadCredentialsException ex){
            throw new BadRequestException("Username or password is not matched!!");
        }

    }
}
