package edu.sru.thangiah.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.domain.Exam;

/**
 * JPA repository for {@link Exam} entities. This interface extends JpaRepository to provide
 * standard CRUD operations for Exam data and includes additional querying capabilities specific to Exams.
 */
public interface ExamRepository extends JpaRepository<Exam, Long> {

	/**
     * Retrieves a set of Exam entities associated with a specific Course.
     *
     * @param course The {@link Course} entity for which to find associated exams.
     * @return A set of {@link Exam} entities related to the given course.
     */
	Set<Exam> findByCourse(Course course);
	}
