package edu.sru.thangiah.controller;

import org.springframework.stereotype.Controller;

import edu.sru.thangiah.service.UserService;


/**
 * Controller class responsible for handling user registration activities.
 * It collaborates with the UserService to perform registration operations.
 */
@Controller
//@RequestMapping("/register")
public class UserRegistrationController {
	
	private UserService userService;

	/**
	 * Constructs a UserRegistrationController with the specified UserService.
	 * 
	 * @param userService The service that will handle user registration logic.
	 */
	public UserRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}

}
