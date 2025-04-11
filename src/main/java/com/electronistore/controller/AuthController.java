package com.electronistore.controller;

import com.electronistore.config.AppConstants;
import com.electronistore.dto.GoogleLoginRequest;
import com.electronistore.dto.JwtRequest;
import com.electronistore.dto.JwtResponse;
import com.electronistore.dto.UserDto;
import com.electronistore.entity.Providers;
import com.electronistore.entity.User;
import com.electronistore.exception.BadRequestException;
import com.electronistore.exception.ResourceNotFoundException;
import com.electronistore.security.JwtHelper;
import com.electronistore.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


/**
 * todo: Step 7:
 *              Create login api to accept username and password and return token if username and password is correct
 * */
@CrossOrigin(origins = "http://localhost:3700")
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "REST APIs for Auth Controller Operations")
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

    @Value("${app.google.client_id}")
    private String googleClientId;

    @Value("${app.google.default_password}")
    private String googleProvidedDEfaultPassword;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){

        logger.info("Username {}, Password {} ", request.getEmail(), request.getPassword());

        // ab ham username aur password ko database se authenticate karte hain using authenticationProvider
        this.doAuthentication(request.getEmail(),request.getPassword());
        // generate token...
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
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

    // Handle Login with google
    // {idToken} will come from Client server means frontend side

    @Hidden   // THis annotation is used for Swagger and this api will not visible in Swagger by this Annotation
    @PostMapping("/login-with-google")
    public ResponseEntity<JwtResponse> handleGoogleLogin(@RequestBody GoogleLoginRequest googleLoginRequest) throws GeneralSecurityException, IOException {
        logger.info("Id Token :{} ", googleLoginRequest.getIdToken());

        // idToken has come from frontend
        // Ab is idToken ko Google se verify karayenge ki aaya hua token sahi hai ki nahi
        GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier
                .Builder(new ApacheHttpTransport(), new GsonFactory())
                .setAudience(List.of(googleClientId))
                .build();

        // (Receive idTokenString by HTTPS POST)
        GoogleIdToken idToken = googleIdTokenVerifier.verify(googleLoginRequest.getIdToken());
        if(idToken != null){
            GoogleIdToken.Payload payload = idToken.getPayload();
            // Access details of user
            String email = payload.getEmail();
            String userName = payload.getSubject();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String given_name = (String) payload.get("given_name");

            logger.info("Name:{} ",name);
            logger.info("Email:{} ",email);
            logger.info("Username:{} ",userName);
            logger.info("Picture:{} ",pictureUrl);

            // to store the user data
            UserDto userDto = new UserDto();
            userDto.setName(name);
            userDto.setImageName(pictureUrl);
            userDto.setEmail(email);
            userDto.setPassword(googleProvidedDEfaultPassword);
            userDto.setAbout("User is created using Google");
            userDto.setProviders(Providers.GOOGLE);

            // Suppose user is login with google again and again so
            // we have to avoid to create user again and again means user can only do login
            UserDto userDto1 = null;
            try {
                logger.info("User is loaded from database");
                userDto1 = userService.getUserByEmail(userDto.getEmail());
                /**
                 *  Suppose ek user pahle manually login kiya with same email and password
                 * Aur fir vo same email id ko google se login kar rha hai to
                 * ye issue create karegi
                 * iske liye ham email id aur password ko match karayenge jo database se aayi hai aur jo google login se aayi hai
                 *
                 * Ye fir ham provider ka use kar sakte hain
                 */
                logger.info(userDto.getProviders().toString());
                if(userDto.getProviders() != null && userDto1.getProviders() != null && userDto1.getProviders().equals(userDto.getProviders())){
                    //continue

                }
                else{
                    throw new org.apache.coyote.BadRequestException("Your email is already registered!! Try to login with right credentials");
                }

            }
            catch (ResourceNotFoundException ex){
                logger.info("User should be created because this is new user");
                userDto1 = userService.createUser(userDto);
            }


            // Authenticate the user
            this.doAuthentication(userDto.getEmail(),userDto.getPassword());

            // convert UserDto to User entity
            User user = mapper.map(userDto1, User.class);

//            create JWT token
            String token = jwtHelper.generateToken(user);

            // send response...
            JwtResponse jwtResponse = JwtResponse.builder().token(token).userDto(mapper.map(user, UserDto.class)).build();

            return ResponseEntity.ok(jwtResponse);

        }
        else{
            logger.info("Invalid Id Token");
            throw new BadRequestException("Invalid Google User!!");
        }

    }

}
