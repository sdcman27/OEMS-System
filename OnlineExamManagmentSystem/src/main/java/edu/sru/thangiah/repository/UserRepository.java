package edu.sru.thangiah.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sru.thangiah.domain.Instructor;
import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.model.User;
/**
 * JPA repository for {@link User} entities. This interface facilitates CRUD operations and
 * custom queries for User data within the database.
 */
public interface UserRepository extends JpaRepository<User, Long> {
	/**
     * Retrieves a User entity by its username.
     *
     * @param username The username of the User.
     * @return An {@link Optional} containing the User if found, or empty otherwise.
     */
    Optional<User> findByUsername(String username);

	
	//List<User> findByUserFirstNameContaining(String name);


    /**
     * Finds a User entity by its verification code.
     *
     * @param verificationCode The verification code associated with the User.
     * @return The {@link User} entity if found, or null otherwise.
     */
    User findByVerificationCode(String verificationCode);




}
