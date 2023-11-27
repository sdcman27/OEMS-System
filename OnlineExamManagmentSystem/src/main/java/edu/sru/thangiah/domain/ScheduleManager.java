package edu.sru.thangiah.domain;

import java.util.List;

import org.springframework.lang.NonNull;

import edu.sru.thangiah.model.Roles;
import edu.sru.thangiah.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Entity class representing a schedule manager within the system.
 * Maps to the ScheduleManager table in the database and includes relationships to roles.
 */
@Entity
@Table(name = "ScheduleManager", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class ScheduleManager {


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@NonNull
    @Column(name = "id")
    private Long managerId;
    
    @NonNull
    @Column(name = "first_name")
    private String managerFirstName;
    
    @NonNull
    @Column(name = "last_name")
    private String managerLastName;
    
    @NonNull
    @Column(name = "email")
    private String managerEmail;
    
    @NonNull
    @Column(name = "password")
    private String managerPassword;
    
    @NonNull
    @Column(name = "username")
    private String managerUsername;
    
    @ManyToMany
    @JoinColumn(name = "role_id")
    private List<Roles> roles;
        

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getManagerFirstName() {
		return managerFirstName;
	}

	public void setManagerFirstName(String managerFirstName) {
		this.managerFirstName = managerFirstName;
	}

	public String getManagerLastName() {
		return managerLastName;
	}

	public void setManagerLastName(String managerLastName) {
		this.managerLastName = managerLastName;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	public String getManagerPassword() {
		return managerPassword;
	}

	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}

	public String getManagerUsername() {
		return managerUsername;
	}

	public void setManagerUsername(String managerUsername) {
		this.managerUsername = managerUsername;
	}

	

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}

	 /**
     * Sets the schedule manager's attributes based on a User entity.
     *
     * @param scheduleManager The User entity from which to copy properties.
     */
	public void setUser(User scheduleManager) {
		this.managerId = scheduleManager.getId();	
		this.managerFirstName = scheduleManager.getFirstName();
	    this.managerLastName = scheduleManager.getLastName();
	    this.managerEmail = scheduleManager.getEmail();
	    this.managerPassword = scheduleManager.getPassword();
	    this.managerUsername = scheduleManager.getUsername();
	    this.roles = scheduleManager.getRoles();
	}

	  /**
     * Default constructor for JPA.
     */
	public ScheduleManager() {}

	  /**
     * Constructs a new ScheduleManager with the specified details.
     *
     * @param managerId        The unique ID of the schedule manager.
     * @param managerFirstName The first name of the schedule manager.
     * @param managerLastName  The last name of the schedule manager.
     * @param managerEmail     The email address of the schedule manager.
     * @param managerPassword  The password for the schedule manager.
     * @param managerUsername  The username for the schedule manager.
     * @param roles            The roles associated with the schedule manager.
     */
	public ScheduleManager(Long managerId, String managerFirstName, String managerLastName, String managerEmail,
			String managerPassword, String managerUsername, List<Roles> roles) {
		super();
		this.managerId = managerId;
		this.managerFirstName = managerFirstName;
		this.managerLastName = managerLastName;
		this.managerEmail = managerEmail;
		this.managerPassword = managerPassword;
		this.managerUsername = managerUsername;
		this.roles = roles;
	}

}