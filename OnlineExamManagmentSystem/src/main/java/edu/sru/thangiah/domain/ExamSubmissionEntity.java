package edu.sru.thangiah.domain;

import org.springframework.format.annotation.DateTimeFormat;

import edu.sru.thangiah.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Represents an exam submission entity which includes user details, exam details, answers, and submission time.
 */
@Entity
@Table(name = "exam_submission")
public class ExamSubmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ElementCollection
    private List<String> userAnswers; // Change the field type to List<String>


    private LocalDateTime submissionTime;

    /**
     * Gets the submission time for the exam.
     * 
     * @return The submission time as a {@link LocalDateTime}.
     */
    public LocalDateTime getSubmissionTime() {
		return submissionTime;
	}

    /**
     * Sets the submission time for the exam.
     * 
     * @param submissionTime The submission time to set as a {@link LocalDateTime}.
     */
	public void setSubmissionTime(LocalDateTime submissionTime) {
		this.submissionTime = submissionTime;
	}

	private int score;

    // Constructors, getters, and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<String> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<String> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    /**
     * Sets the user ID associated with this submission.
     * 
     * @param userId The ID of the user.
     */
    public void setUserId(Long userId) {
    }

    /**
     * Sets the exam ID associated with this submission.
     * 
     * @param examId The ID of the exam.
     */
    public void setExamId(Long examId) {
    }
}
