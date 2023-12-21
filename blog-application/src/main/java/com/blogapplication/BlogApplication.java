package com.blogapplication;

import com.blogapplication.config.AppConstants;
import com.blogapplication.entities.Role;
import com.blogapplication.repositories.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class BlogApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder pass;

	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {

		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
        return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.pass.encode("123"));

		try{

			Role role = new Role();
			role.setRoleId(AppConstants.ADMIN_USER);
			role.setRoleName("ROLE_ADMIN");

			Role role1 = new Role();
			role1.setRoleId(AppConstants.NORMAL_USER);
			role1.setRoleName("ROLE_NORMAL");

			List<Role> roles = List.of(role,role1);

			List<Role> result = this.roleRepo.saveAll(roles);

			result.forEach(r -> {
				System.out.println(r.getRoleName());
			});

		}catch(Exception e){
            e.printStackTrace();
		}
	}
}
