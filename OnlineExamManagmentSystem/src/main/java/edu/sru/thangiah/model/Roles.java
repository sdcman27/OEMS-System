package edu.sru.thangiah.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity class representing a role within the security context of the application. 
 * Roles are used for granting authorities and permissions to users.
 */
@Entity
@Table(name = "roles")
public class Roles {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private String name;
	
	/**
     * Default constructor for JPA.
     */
	public Roles() {}
	
	/**
     * Constructs a new role with the specified ID and name.
     *
     * @param id   the unique identifier for the role
     * @param name the name of the role
     */
	public Roles(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
		
	/**
     * Gets the unique identifier for this role.
     *
     * @return the unique identifier for this role
     */
	public Long getId() {
		return id;
	}
	
	/**
     * Sets the unique identifier for this role.
     *
     * @param id the unique identifier for this role
     */
	public void setId(Long id) {
		this.id = id;
	}
	 /**
     * Gets the name of this role.
     *
     * @return the name of this role
     */
	public String getName() {
		return name;
	}
	
	/**
     * Sets the name of this role.
     *
     * @param name the name of this role
     */
	public void setName(String name) {
		this.name = name;
	}
	
}
