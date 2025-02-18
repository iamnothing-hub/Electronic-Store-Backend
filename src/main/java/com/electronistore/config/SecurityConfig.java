package com.electronistore.config;


import com.electronistore.security.JWTAuthenticationEntryPoint;
import com.electronistore.security.JWTAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;


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


    // THis is swagger urls
    private final String[] PUBLIC_URLs = {
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
    };

    // SecurityFilterChain beans,   HttpSecurity will give Xml file
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {



        /**
         * Configuring URLs
         * CORS concept
         * csrf concept
         */
//        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
//        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        httpSecurity.cors(httpSecurityCorsConfigurer ->
                httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();

                        //origins
                        //methods
                        // Isase kewal ek origin access hoga
//                        corsConfiguration.addAllowedOrigin("http://localhost:3000");

                        // Suppose I want to access more than one origin like 3000, 3100 etc.
                        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:3100","http://localhost:4200"));
                        // Suppose I want to give access for all origin so that we use *
                     //   corsConfiguration.setAllowedOrigins(List.of("*"));  // Not recommanded in Production

                        // For Methods
//                        corsConfiguration.setAllowedMethods(List.of("*"));  //isko use karenge to credentials ko false karna padega
                        corsConfiguration.setAllowedOriginPatterns(List.of("*"));// this is better than upper code
                        // Method lagana jaruri hai otherwise give error of CORS not allowed to pass the data
                        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        // For Credentials
                        corsConfiguration.setAllowCredentials(true);

                        // For Headers
                        corsConfiguration.setAllowedHeaders(List.of("*"));

                        // For MaxAge means Cleint data ko kitne time tak cache memory mein rakh sakta hai
                        // Ye second mein value leti hai
                        corsConfiguration.setMaxAge(3000L);
                        return corsConfiguration;
                    }
                })
                );
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());


        httpSecurity.authorizeHttpRequests(request ->{

            request.requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole(AppConstants.ROLE_ADMIN)
                    .requestMatchers(PUBLIC_URLs).permitAll()   //-> THis is swagger endpoints
                    .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole(AppConstants.ROLE_ADMIN,AppConstants.ROLE_NORMAL)
                    .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                    .requestMatchers("/products/**").hasRole(AppConstants.ROLE_ADMIN)
                    .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                    .requestMatchers("/categories/**").hasRole(AppConstants.ROLE_ADMIN)
                    .requestMatchers(HttpMethod.POST, "/auth/generate-token","/auth/login-with-google").permitAll()
//                    .requestMatchers(HttpMethod.POST, "/auth/login-with-google").permitAll()
                    .requestMatchers("/auth/**").permitAll()
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


    //AuthenticationManager k liye ek bean banayenge
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return  builder.getAuthenticationManager();
    }

}
