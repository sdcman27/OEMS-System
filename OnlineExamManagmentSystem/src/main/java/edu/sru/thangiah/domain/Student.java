package edu.sru.thangiah.domain;


import java.util.Set; 

import org.springframework.lang.NonNull;

import edu.sru.thangiah.model.Roles;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "STUDENT")

public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long studentId;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "STUDENT_AND_COURSES_TABLE", 
    joinColumns = {
    		@JoinColumn(name = "student_id", referencedColumnName = "id")
    },
    inverseJoinColumns = {
    		@JoinColumn(name = "course_id", referencedColumnName = "id")
    })
    private Set<Course> courses;
    
 // Getter and setter for courses
    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
    
    @NonNull
    @Column (name = "first_name")
    private String studentFirstName;
    
    @NonNull
    @Column (name = "last_name")
    private String studentLastName;
    
    @NonNull
    @Column (name = "email")
    private String studentEmail;
    
    @NonNull
    @Column (name = "password")
    private String studentPassword;
    
    @NonNull
    @Column (name = "username")
    private String studentUsername;
    
    @NonNull
    @Column (name = "enrolled_credits")
    private float creditsTaken;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles role;
    

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String getStudentFirstName() {
		return studentFirstName;
	}

	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	public String getStudentLastName() {
		return studentLastName;
	}

	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	public String getStudentEmail() {
		return studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}

	public String getStudentPassword() {
		return studentPassword;
	}

	public void setStudentPassword(String studentPassword) {
		this.studentPassword = studentPassword;
	}

	public String getStudentUsername() {
		return studentUsername;
	}

	public void setStudentUsername(String studentUsername) {
		this.studentUsername = studentUsername;
	}

	public float getCreditsTaken() {
		return creditsTaken;
	}

	public void setCreditsTaken(float creditsTaken) {
		this.creditsTaken = creditsTaken;
	}
	
	

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	
	public Student(Long studentId, Set<Course> courses, String studentFirstName, String studentLastName,
			String studentEmail, String studentPassword, String studentUsername, float creditsTaken, Roles role) {
		super();
		this.studentId = studentId;
		this.courses = courses;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.studentEmail = studentEmail;
		this.studentPassword = studentPassword;
		this.studentUsername = studentUsername;
		this.creditsTaken = creditsTaken;
		this.role = role;
	}

	public Student() {
		
	}
	
}
