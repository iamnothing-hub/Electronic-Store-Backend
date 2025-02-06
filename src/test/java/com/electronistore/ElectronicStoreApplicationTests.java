package com.electronistore;

import com.electronistore.entity.User;
import com.electronistore.repository.UserRepository;
import com.electronistore.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtHelper jwtHelper;
	@Test
	void contextLoads() {
	}

	@Test
	void testJwtToken(){
		System.out.println("This is jwt token.");
		User user = userRepository.findByEmail("amit@gmail.com").get();

		String token = jwtHelper.generateToken(user);
		System.out.println("Token: " + token);
		System.out.println("**********************************************************************************");
		String username = jwtHelper.getUsernameFromToken(token);
		System.out.println("Username from Token: "+username);
		System.out.println("**********************************************************************************");
		System.out.println("Is Token Expired: "+ jwtHelper.isTokenExpired(token));
		System.out.println("**********************************************************************************");
	}

}
