package edu.sru.thangiah.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.sru.thangiah.model.User;
import edu.sru.thangiah.repository.UserRepository;
import edu.sru.thangiah.web.dto.UserRegistrationDto;


/**
 * Service implementation for user management operations.
 * <p>
 * This class implements the {@link UserService} interface and provides
 * concrete implementations for the methods to handle user registration
 * and to load user details for authentication purposes.
 * </p>
 *
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

	/*
	 * public User registerNewUserAccount(UserRegistrationDto userDto) { // Create a
	 * User object from the registration data User user = new User( null,
	 * userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
	 * userDto.getPassword(), userDto.getUsername(), "ROLE_USER", false, null );
	 * 
	 * // Save the user to the database return userRepository.save(user); }
	 */

    /**
     * Registers a new user account with the information provided from the registration DTO.
     *
     * @param registrationDto the data transfer object containing user registration details
     * @return the registered {@link User} entity after saving it to the database
     */
	@Override
	public User save(UserRegistrationDto registrationDto) {
		User user = new User(null, registrationDto.getFirstName(), registrationDto.getLastName(),
				registrationDto.getEmail(), registrationDto.getPassword(), registrationDto.getUsername(),
				registrationDto.getVerificationCode(), false, null);

		return userRepository.save(user);

	}
	
	/**
     * Loads the user-specific data necessary for authentication.
     *
     * @param username the username identifying the user whose data is to be loaded
     * @return the {@link UserDetails} of the requested user
     * @throws UsernameNotFoundException if the user with the given username is not found
     */

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User user = userRepository.findByUsername(username).orElse(null);

	    if (user == null) {
	        throw new UsernameNotFoundException("User not found with username: " + username);
	    }

	    return user;
	}


}
