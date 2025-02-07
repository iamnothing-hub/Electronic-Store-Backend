package com.electronistore.config;


import com.electronistore.security.JWTAuthenticationEntryPoint;
import com.electronistore.security.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 *todo: Step 5:
 *      Steps to implement jwt in project
 *        5. -> Configure spring security in configuration file
 *        6. -> Create JWTRequest and JWTResponse to receive request data and send Login success response
 *        7. -> Create login api to accept username and password and return token if username and password is correct
 *        8. -> Test the application
 *
 *
 */
@Configuration
@EnableWebSecurity(debug = true)    // for testing purpose
@EnableMethodSecurity(prePostEnabled = true)  // This is for method level security in controller
public class SecurityConfig {

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    // SecurityFilterChain beans,   HttpSecurity will give Xml file
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        /**
         * Configuring URLs
         * CORS concept
         * csrf concept
         */
        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());


        httpSecurity.authorizeHttpRequests(request ->{

            request.requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("ADMIN","NORMAL")
                    .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                    .requestMatchers("/products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                    .requestMatchers("/categories/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/auth/generate-token").permitAll()
                    .requestMatchers("/auth/**").authenticated()
                    .anyRequest().permitAll()
                    ;

        });
//        httpSecurity.httpBasic(Customizer.withDefaults());

        httpSecurity.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
