/* Seth Chritzman 																				    */ 
/*  JpaRepository is a JPA (Java Persistence API) specific extension of Repository. It contains the */
/*  full API of CrudRepository and PagingAndSortingRepository. So it contains API for basic CRUD    */
/*  operations and also API for pagination and sorting. 										    */	
/*  https://www.geeksforgeeks.org/spring-boot-jparepository-with-example/#						    */												



package edu.sru.thangiah.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import edu.sru.thangiah.domain.Administrator;
import edu.sru.thangiah.domain.Instructor;

/**
 * JPA repository for {@link Administrator} entities. This interface extends JpaRepository to provide
 * standard CRUD operations for Administrator data and includes additional querying capabilities.
 */
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

	/**
     * Retrieves an Administrator entity by its username.
     *
     * @param username The username to search for.
     * @return An {@link Optional} containing the matching Administrator if found, or empty if not.
     */
	Optional<Administrator> findByAdminUsername(String username);

	/**
     * Finds a list of Administrator entities where the username contains the given string.
     *
     * @param instructorUser The string to search for within the administrator's username.
     * @return A list of {@link Administrator} entities with usernames containing the search parameter.
     */
	List<Administrator> findByAdminUsernameContaining(String instructorUser);

}

