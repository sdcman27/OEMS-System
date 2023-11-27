package edu.sru.thangiah.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sru.thangiah.domain.Exam;
import edu.sru.thangiah.domain.ExamSubmissionEntity;
import jakarta.transaction.Transactional;

/**
 * JPA repository for {@link ExamSubmissionEntity} entities. It facilitates the creation,
 * retrieval, and deletion of exam submission records, as well as provides various utility methods
 * related to exam submissions.
 */
public interface ExamSubmissionRepository extends JpaRepository<ExamSubmissionEntity, Long> {
	
	/**
     * Retrieves an exam submission by the user's ID and the exam's ID.
     *
     * @param userId The ID of the user.
     * @param examId The ID of the exam.
     * @return The {@link ExamSubmissionEntity} if found, or null otherwise.
     */
	ExamSubmissionEntity findByUser_IdAndExam_Id(Long userId, Long examId);
    

    /**
     * Deletes all submissions associated with a given exam.
     * 
     * @param exam The {@link Exam} entity whose submissions are to be deleted.
     */
	@Transactional
	void deleteByExam(Exam exam);
    
    /**
     * Counts the number of submissions for a given exam.
     *
     * @param exam The {@link Exam} entity to count submissions for.
     * @return The number of submissions for the exam.
     */
	long countByExam(Exam exam);

	 /**
     * Checks if an exam submission exists for a given user and exam.
     *
     * @param userId The ID of the user.
     * @param examId The ID of the exam.
     * @return true if a submission exists, false otherwise.
     */
	boolean existsByUser_IdAndExam_Id(Long userId, Long examId);

}
