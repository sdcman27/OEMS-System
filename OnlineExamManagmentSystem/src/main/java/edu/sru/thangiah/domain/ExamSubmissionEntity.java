package edu.sru.thangiah.domain;

import org.springframework.format.annotation.DateTimeFormat;

import edu.sru.thangiah.model.User;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

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

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submissionTime;

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

    public Date getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Date submissionTime) {
        this.submissionTime = submissionTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    // Add setter methods for userId and examId
    public void setUserId(Long userId) {
        // Set the user ID in the User entity or handle it as needed
    }

    public void setExamId(Long examId) {
        // Set the exam ID in the Exam entity or handle it as needed
    }
}
