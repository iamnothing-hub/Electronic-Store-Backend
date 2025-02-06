package com.electronistore;

import com.electronistore.entity.Role;
import com.electronistore.entity.User;
import com.electronistore.repository.RoleRepository;
import com.electronistore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
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

		Role roleAdmin = roleRepository.findByRoleName("ROLE_ADMIN").orElse(null);
		Role roleNormal = roleRepository.findByRoleName("ROLE_NORMAL").orElse(null);

		if(roleAdmin==null){
			roleAdmin = new Role();
			roleAdmin.setRoleId(UUID.randomUUID().toString());
			roleAdmin.setRoleName("ROLE_ADMIN");
			roleRepository.save(roleAdmin);
		}
		if(roleNormal==null){
			roleNormal = new Role();
			roleNormal.setRoleId(UUID.randomUUID().toString());
			roleNormal.setRoleName("ROLE_NORMAL");
			roleRepository.save(roleNormal);
		}

		// Ham ek ADMIN user banayenge
		User user = userRepository.findByEmail("amit@gmail.com").orElse(null);
		if(user == null){
			user = new User();
			user.setUserId(UUID.randomUUID().toString());
			user.setName("Amit");
			user.setPassword(passwordEncoder.encode("Amit@1234"));
			user.setGender("MALE");
			user.setEmail("amit@gmail.com");
			user.setRoles(List.of(roleAdmin));

			userRepository.save(user);
		}
	}
}
