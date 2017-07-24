package com.bookstore.bookstoreapp.resource;

import com.bookstore.bookstoreapp.config.SecurityUtility;
import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.security.Role;
import com.bookstore.bookstoreapp.service.UserService;
import com.bookstore.bookstoreapp.utility.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.HashMap;
@RestController
@RequestMapping("/user")
public class UserResource {

	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;

	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public ResponseEntity newUserPost(@RequestBody HashMap<String, String> mapper) throws Exception {
		String username = mapper.get("username");
		String userEmail = mapper.get("email");

		if (userService.findByUsername(username) != null) {
			return new ResponseEntity("usernameExists", HttpStatus.BAD_REQUEST);
		}

		if (userService.findByEmail(userEmail) != null) {
			return new ResponseEntity("emailExists", HttpStatus.BAD_REQUEST);
		}

		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);

		userService.createUser(user, Role.ROLE_USER);

		return new ResponseEntity("User Added Successfully!", HttpStatus.OK);

	}

	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
	public ResponseEntity forgetPasswordPost(@RequestBody HashMap<String, String> mapper) throws Exception {

		User user = userService.findByEmail(mapper.get("email"));

		if (user == null) return new ResponseEntity("Email not found", HttpStatus.BAD_REQUEST);

		String password = SecurityUtility.randomPassword();
		user.setPassword(bCryptPasswordEncoder.encode(password));
		userService.save(user);

        mailService.sendNewUserEmail(user, password);

		return new ResponseEntity("Email sent!", HttpStatus.OK);

	}

	@RequestMapping(value="/updateUserInfo", method= RequestMethod.POST)
	public ResponseEntity profileInfo(@RequestBody HashMap<String, Object> mapper) throws Exception{
		
		int id = (Integer) mapper.get("id");
		String email = (String) mapper.get("email");
		String username = (String) mapper.get("username");
		String firstName = (String) mapper.get("firstName");
		String lastName = (String) mapper.get("lastName");
		String newPassword = (String) mapper.get("newPassword");
		String currentPassword = (String) mapper.get("currentPassword");
		
		User currentUser = userService.findById(Long.valueOf(id));
		
		if(currentUser == null) return new ResponseEntity("User not found", HttpStatus.BAD_REQUEST);
		
		if(userService.findByEmail(email) != null) {
			if(userService.findByEmail(email).getId() != currentUser.getId()) {
				return new ResponseEntity("Email not found!", HttpStatus.BAD_REQUEST);
			}
		}
		
		if(userService.findByUsername(username) != null) {
			if(userService.findByUsername(username).getId() != currentUser.getId()) {
				return new ResponseEntity("Username not found!", HttpStatus.BAD_REQUEST);
			}
		}
			String dbPassword = currentUser.getPassword();
			
			if(null != currentPassword)
			if(bCryptPasswordEncoder.matches(currentPassword, dbPassword)) {
				if(newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
					currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
				}
				currentUser.setEmail(email);
			} else {
				return new ResponseEntity("Incorrect current password!", HttpStatus.BAD_REQUEST);
			}
		
		
		currentUser.setFirstName(firstName);
		currentUser.setLastName(lastName);
		currentUser.setUsername(username);
		
		
		userService.save(currentUser);
		
		return new ResponseEntity("Update Success", HttpStatus.OK);
	}

	@RequestMapping("/getCurrentUser")
	public User getCurrentUser(Principal principal) {
		User user = new User();
		if (null != principal) user = userService.findByUsername(principal.getName());
		return user;
	}

}
