package edu.sru.thangiah.domain;

import java.util.Set;

import javax.persistence.JoinColumn;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a course entity in an educational institution's domain.
 * This class maps to a database table named 'COURSE' and includes
 * information about the course such as its name, the instructor teaching it,
 * the students enrolled, and the exams associated with it.
 * <p>
 * The {@code Course} class is a JPA entity that supports standard ORM
 * (Object-Relational Mapping) and is linked to other entities like {@code Instructor},
 * {@code Student}, and {@code Exam} through various JPA relationships.
 * </p>
 * <p>
 * Each course is uniquely identified by its ID and can be associated with multiple
 * students and exams. The instructor is mapped as a many-to-one relationship indicating
 * that each course is taught by a single instructor but an instructor can teach multiple courses.
 * </p>
 *
 * @see Instructor
 * @see Student
 * @see Exam
 */
@Entity
@Table(name = "COURSE")

public class Course {

	@Id
	private Long id;

    private String courseName;
    
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
    
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Exam> exams;


    /**
     * Gets the instructor associated with this course.
     *
     * @return The {@code Instructor} object.
     */
    public Instructor getInstructor() {
        return instructor;
    }

    /**
     * Sets the instructor associated with this course.
     *
     * @param instructor The {@code Instructor} object to associate.
     */
    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    /**
     * Gets the unique identifier for the course.
     *
     * @return The ID of the course.
     */
	public Long getId() {
		return id;
	}

	 /**
     * Sets the unique identifier for the course.
     *
     * @param id The ID to set for the course.
     */
	public void setId(Long id) {
		this.id = id;
	}

	/**
     * Gets the name of the course.
     *
     * @return The course name.
     */
	public String getCourseName() {
		return courseName;
	}
	

	//public void setCourseName(String className) {
	//	this.courseName = className;
	//}
	
	/**
     * Gets the name of the course.
     *
     * @return The course name.
     */
	public void setCourseName(String courseName) {
	    this.courseName = courseName;
	}
	

	/**
     * Gets the set of students enrolled in this course.
     *
     * @return A set of {@code Student} objects.
     */
	public Set<Student> getStudents() {
		return students;
	}

	/**
     * Sets the students enrolled in this course.
     *
     * @param students A set of {@code Student} objects to enroll.
     */
	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	 /**
     * Gets the set of exams associated with this course.
     *
     * @return A set of {@code Exam} objects.
     */
	public Set<Exam> getExams() {
		return exams;
	}

	 /**
     * Sets the exams associated with this course.
     *
     * @param exams A set of {@code Exam} objects to associate.
     */
	public void setExams(Set<Exam> exams) {
		this.exams = exams;
	}
	
    
    
}