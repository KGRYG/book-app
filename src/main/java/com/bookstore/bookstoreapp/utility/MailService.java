package com.bookstore.bookstoreapp.utility;

import com.bookstore.bookstoreapp.domain.Order;
import com.bookstore.bookstoreapp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Component
public class MailService {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender mailSender;

	@Async
	public void sendNewUserEmail(User user, String password) {
		mailSender.send(constructNewUserEmail(user, password));
	}
	
	private SimpleMailMessage constructNewUserEmail(User user, String password) {
		String message="\nPlease use the following credentials to log in and edit your personal information including your own password."
				+ "\nUsername: " + user.getUsername() + "\nPassword: " + password;
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("Bookstore - New User");
		email.setText(message);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}


	
	public MimeMessagePreparator constructOrderConfirmationEmail (User user, Order order, Locale locale) {
		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("user", user);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getEmail());
				email.setSubject("Order Confirmation - " + order.getId());
				email.setText(text,true);
				email.setFrom(env.getProperty("support.email"));
			}
		};
		
		return messagePreparator;
	}

}
