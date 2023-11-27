package edu.sru.thangiah.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.sru.thangiah.domain.Instructor;
import edu.sru.thangiah.domain.ScheduleManager;


/**
 * JPA repository for {@link ScheduleManager} entities. It provides the mechanism for CRUD operations
 * and custom query execution for ScheduleManager data within the database.
 */
public interface ScheduleManagerRepository extends JpaRepository<ScheduleManager, Long> {
	

    /**
     * Finds a ScheduleManager entity by its username.
     *
     * @param username The username to search for.
     * @return An {@link Optional} containing the matching ScheduleManager if found, or empty if not.
     */
	Optional<ScheduleManager> findBymanagerUsername(String username);  // Updated method name to match field name

	 /**
     * Retrieves a list of ScheduleManager entities where the first name contains the given search parameter.
     *
     * @param searchParam The string to search for within the manager's first name.
     * @return A list of {@link ScheduleManager} entities with first names containing the search parameter.
     */
	List<ScheduleManager> findByManagerFirstNameContaining(String searchParam);
	
	/**
     * Retrieves a list of ScheduleManager entities where the username contains the given search parameter.
     *
     * @param searchParam The string to search for within the manager's username.
     * @return A list of {@link ScheduleManager} entities with usernames containing the search parameter.
     */
	List<ScheduleManager> findByManagerUsernameContaining(String searchParam);
}