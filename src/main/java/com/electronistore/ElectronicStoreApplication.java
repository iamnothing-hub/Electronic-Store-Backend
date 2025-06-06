package com.electronistore;

import com.electronistore.config.AppConstants;
import com.electronistore.entity.Role;
import com.electronistore.entity.User;
import com.electronistore.repository.RoleRepository;
import com.electronistore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
//@EnableSwagger2
//@EnableWebMvc //todo: No need to add this annotation in spring boot 3+ version, also no need to code for Swagger configuration
public class ElectronicStoreApplication  implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Role roleAdmin = roleRepository.findByRoleName(AppConstants.ROLE_ADMIN).orElse(null);
		Role roleNormal = roleRepository.findByRoleName(AppConstants.ROLE_NORMAL).orElse(null);

		if(roleAdmin==null){
			roleAdmin = new Role();
			roleAdmin.setRoleId(UUID.randomUUID().toString());
			roleAdmin.setRoleName(AppConstants.ROLE_ADMIN);
			roleRepository.save(roleAdmin);
		}
		if(roleNormal==null){
			roleNormal = new Role();
			roleNormal.setRoleId(UUID.randomUUID().toString());
			roleNormal.setRoleName(AppConstants.ROLE_NORMAL);
			roleRepository.save(roleNormal);
		}

		// Ham ek ADMIN user banayenge
		User user = userRepository.findByEmail("amitkumar@gmail.com").orElse(null);
		if(user == null){
			user = new User();
			user.setUserId(UUID.randomUUID().toString());
			user.setName("Amit");
			user.setPassword(passwordEncoder.encode("Amit@1234"));
			user.setGender("MALE");
			user.setEmail("amitkumar@gmail.com");
			user.setRoles(List.of(roleAdmin,roleNormal));

			userRepository.save(user);
		}
	}
}
