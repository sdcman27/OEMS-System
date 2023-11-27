package edu.sru.thangiah.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sru.thangiah.model.Roles;

/**
 * JPA repository for {@link Roles} entities. Provides a standard set of CRUD operations on Roles,
 * as well as the ability to find a role by its name.
 */
public interface RoleRepository extends JpaRepository<Roles, Long> {
	
	/**
     * Retrieves a role by its name.
     *
     * @param name The name of the role to find.
     * @return An {@link Optional} containing the role if found, or empty otherwise.
     */
    Optional<Roles> findByName(String name);
    
}