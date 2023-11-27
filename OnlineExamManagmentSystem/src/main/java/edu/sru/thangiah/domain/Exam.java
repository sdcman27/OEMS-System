package edu.sru.thangiah.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The {@code Exam} class represents an exam entity with details such as name, duration, start and end times,
 * and the associated course and questions.
 */
@Entity
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int submissionCount;
    private String examName;
    private int durationInMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
    
    @Transient  // This annotation makes sure the field is not persisted in the database
    private String formattedStartTime;
    

    public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	@ManyToMany
    private List<ExamQuestion> questions;

	 /**
     * Constructs an {@code Exam} object and initializes the start time to the current date and time.
     */
    public Exam() {
        this.startTime = LocalDateTime.now(); // Sets the startTime to the current date and time.
    }
    

    /**
     * Formats the start time into a human-readable string.
     */
    public void formatStartTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.formattedStartTime = this.startTime.format(formatter);
    }


    /**
     * Gets the starting time of the exam.
     *
     * @return The {@code LocalDateTime} representing the starting time of the exam.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    /**
     * Sets the starting time of the exam.
     *
     * @param startTime The {@code LocalDateTime} to set as the starting time of the exam.
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    /**
     * Gets the unique identifier for the exam.
     *
     * @return The ID of the exam.
     */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for the exam.
	 *
	 * @param id The ID to set for the exam.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name of the exam.
	 *
	 * @return The exam name.
	 */
	public String getExamName() {
		return examName;
	}

	/**
	 * Sets the name of the exam.
	 *
	 * @param examName The name to set for the exam.
	 */
	public void setExamName(String examName) {
		this.examName = examName;
	}

	/**
	 * Gets the duration of the exam in minutes.
	 *
	 * @return The duration of the exam.
	 */
	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	/**
	 * Sets the duration of the exam in minutes.
	 *
	 * @param durationInMinutes The duration to set for the exam.
	 */
	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}
	
	
	/**
	 * Gets the list of questions associated with the exam.
	 *
	 * @return A list of {@code ExamQuestion} objects.
	 */
	public List<ExamQuestion> getQuestions() {
		return questions;
	}
	
	/**
	 * Sets the list of questions for the exam.
	 *
	 * @param questions The list of {@code ExamQuestion} objects to associate with the exam.
	 */
	public void setQuestions(List<ExamQuestion> questions) {
		this.questions = questions;
	}

	/**
	 * Gets the course associated with the exam.
	 *
	 * @return The {@code Course} object.
	 */
	public Course getCourse() {
		return course;
	}

	/**
	 * Sets the course associated with the exam.
	 *
	 * @param course The {@code Course} to associate with the exam.
	 */
	public void setCourse(Course course) {
		this.course = course;
	}

	/**
	 * Gets the formatted start time as a string.
	 *
	 * @return The formatted start time.
	 */
	public String getFormattedStartTime() {
		return formattedStartTime;
	}

	/**
	 * Sets the formatted start time for the exam.
	 *
	 * @param formattedStartTime The formatted start time to set for the exam.
	 */
	public void setFormattedStartTime(String formattedStartTime) {
		this.formattedStartTime = formattedStartTime;
	}

	/**
	 * Gets the number of submissions for the exam.
	 *
	 * @return The number of submissions.
	 */
	public int getSubmissionCount() {
		return submissionCount;
	}

	/**
	 * Sets the number of submissions for the exam.
	 *
	 * @param submissionCount The count to set for the number of submissions.
	 */
	public void setSubmissionCount(int submissionCount) {
		this.submissionCount = submissionCount;
	}
    
}
