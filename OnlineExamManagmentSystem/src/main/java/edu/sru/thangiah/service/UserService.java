package edu.sru.thangiah.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.sru.thangiah.model.User;
import edu.sru.thangiah.web.dto.UserRegistrationDto;



/**
 * Interface for user management service that extends the Spring Security {@link UserDetailsService}.
 * <p>
 * This interface defines user-related operations which include saving user details
 * from a registration data transfer object and loading user-specific data for authentication purposes.
 * </p>
 * 
 * @see UserDetailsService
 */
@Service
public interface UserService extends UserDetailsService{
	
	 /**
     * Saves a new user to the database based on the provided registration data.
     *
     * @param registrationDto A DTO containing the user's registration information.
     * @return The persisted {@link User} entity.
     */
	User save(UserRegistrationDto registrationDto);
	
	   /**
     * Loads the user's details given the username, required for user authentication.
     *
     * @param username The username of the user whose details are to be loaded.
     * @return A {@link UserDetails} instance representing the user's details.
     * @throws UsernameNotFoundException If the user with the given username is not found.
     */
	
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

	
}
