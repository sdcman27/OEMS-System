package edu.sru.thangiah.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.sru.thangiah.domain.ExamQuestion;

/**
 * JPA repository for {@link ExamQuestion} entities. It extends JpaRepository to provide
 * standard CRUD operations for ExamQuestion data and includes additional querying capabilities.
 */
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
	
	/**
     * Retrieves an ExamQuestion by its question text.
     *
     * @param questionText The text of the question to find.
     * @return The {@link ExamQuestion} entity if found.
     */
	ExamQuestion findByQuestionText(String questionText);
	
	/**
     * Finds all questions associated with a specific chapter.
     *
     * @param chapter The chapter number to search for.
     * @return A list of {@link ExamQuestion} entities for the specified chapter.
     */
    List<ExamQuestion> findByChapter(int chapter);
    
    /**
     * Saves a list of AI-generated ExamQuestion entities.
     *
     * @param aiQuestions The list of {@link ExamQuestion} entities to save.
     */
    @Query("SELECT DISTINCT eq.chapter FROM ExamQuestion eq")
    List<Integer> findAllDistinctChapters();
    
    /**
     * Finds all questions associated with a specific chapter.
     *
     * @param chapter The chapter number to search for.
     * @return A list of {@link ExamQuestion} entities for the specified chapter.
     */
    List<ExamQuestion> findQuestionsByChapter(int chapter);
    
    /**
     * Saves a list of AI-generated ExamQuestion entities.
     *
     * @param aiQuestions The list of {@link ExamQuestion} entities to save.
     */
	void save(List<ExamQuestion> aiQuestions);
	/**
     * Finds all ExamQuestion entities where the question text contains the given search text, case-insensitively.
     *
     * @param searchText The text to search for within the question text.
     * @return A list of {@link ExamQuestion} entities where the question text contains the given search text.
     */
    List<ExamQuestion> findByQuestionTextContainingIgnoreCase(String searchText);
    
    /**
     * Retrieves an ExamQuestion by its ID.
     *
     * @param id The ID of the exam question.
     * @return An {@link Optional} containing the ExamQuestion if found, or empty otherwise.
     */
    Optional<ExamQuestion> findById(Long id);
    
    /**
     * Finds a list of random ExamQuestion entities of a specified question type.
     *
     * @param type The type of questions to find.
     * @param pageable The {@link Pageable} object to specify the number and order of results.
     * @return A list of randomly selected {@link ExamQuestion} entities of the specified type.
     */
    @Query("SELECT q FROM ExamQuestion q WHERE q.questionType = ?1 ORDER BY FUNCTION('RAND')")
    List<ExamQuestion> findRandomQuestionsByType(ExamQuestion.QuestionType type, Pageable pageable);

}
