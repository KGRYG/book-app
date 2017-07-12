package com.bookstore.bookstoreapp;

import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.security.Role;
import com.bookstore.bookstoreapp.domain.security.UserRole;
import com.bookstore.bookstoreapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BookstoreAppApplication implements CommandLineRunner {

	@Value("${webmaster.username}")
	private String webmasterUsername;

	@Value("${webmaster.password}")
	private String webmasterPassword;

	@Value("${webmaster.email}")
	private String webmasterEmail;

	@Autowired
	private UserService userService;

	private static final Logger LOG = LoggerFactory.getLogger(BookstoreAppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookstoreAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		User user = new User();
		user.setPassword(webmasterPassword);
		user.setEmail(webmasterEmail);
		user.setUsername(webmasterUsername);

		Set<UserRole> userRoles = new HashSet<>();
		Role role = new Role();
		role.setId(1);
		role.setName("ROLE_ADMIN");
		userRoles.add(new UserRole(user, role));

		userService.createUser(user, userRoles);
		userRoles.clear();

	}
}
