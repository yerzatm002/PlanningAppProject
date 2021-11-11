package kz.Meirambekuly.Project;

import kz.Meirambekuly.Project.domain.Role;
import kz.Meirambekuly.Project.domain.User;
import kz.Meirambekuly.Project.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
//			userService.saveRole(new Role(null, "USER"));
//			userService.saveRole(new Role(null, "MANAGER"));
//			userService.saveRole(new Role(null, "ADMIN"));

//			userService.saveUser(new User(null, "John Adams", "john", "1234", new ArrayList<>(), null));
//			userService.saveUser(new User(null, "Harry Kane", "kane", "1234", new ArrayList<>(), new ArrayList<>()));
//			userService.saveUser(new User(null, "Kai Havertz", "kai", "1234", new ArrayList<>(), new ArrayList<>()));
//			userService.saveUser(new User(null, "Mason Mount", "mason", "1234", new ArrayList<>(), new ArrayList<>()));
//
//			userService.addRoleToUser("john","ADMIN");
//			userService.addRoleToUser("john","ROLE_MANAGER");
//			userService.addRoleToUser("kai","ROLE_USER");
//			userService.addRoleToUser("kai","ROLE_MANAGER");
//			userService.addRoleToUser("mason","ROLE_USER");
//			userService.addRoleToUser("mason","ROLE_MANAGER");
//			userService.addRoleToUser("mason","ROLE_ADMIN");
		};
	}
}
