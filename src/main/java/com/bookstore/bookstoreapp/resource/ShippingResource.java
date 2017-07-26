package com.bookstore.bookstoreapp.resource;

import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.UserShipping;
import com.bookstore.bookstoreapp.service.UserService;
import com.bookstore.bookstoreapp.service.UserShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/shipping")
public class ShippingResource {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserShippingService userShippingService;
	
	@RequestMapping(value="/add", method= RequestMethod.POST)
	public ResponseEntity addNewUserShippingPost(@RequestBody UserShipping userShipping, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		userService.updateUserShipping(userShipping, user);
		
		return new ResponseEntity("Shipping Added(Updated) Successfully!", HttpStatus.OK);
	}
	
	@RequestMapping("/getUserShippingList")
	public List<UserShipping> getUserShippingList(Principal principal){
		User user = userService.findByUsername(principal.getName());
		return user.getUserShippingList();
	}
	
	@RequestMapping(value="/remove", method= RequestMethod.POST)
	public ResponseEntity removeUserShippingPost(@RequestBody String id, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		userShippingService.removeById(Long.parseLong(id), user);
		return new ResponseEntity("Shipping Removed Successfully!", HttpStatus.OK);
	}
	
	@RequestMapping(value="/setDefault", method= RequestMethod.POST)
	public ResponseEntity setDefaultUserShippingPost(@RequestBody String id, Principal principal){
		User user = userService.findByUsername(principal.getName());
		userService.setUserDefaultShipping(Long.parseLong(id), user);
		
		return new ResponseEntity("Set Default Shipping Successfully!", HttpStatus.OK);
	}
}
