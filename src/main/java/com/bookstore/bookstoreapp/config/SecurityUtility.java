package com.bookstore.bookstoreapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class SecurityUtility {
	
	@Bean
	public static String randomPassword() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		
		while(salt.length() < 18) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		
		String saltStr = salt.toString();
		return saltStr;
	}
}
