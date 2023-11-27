package edu.sru.thangiah.service;

import java.io.IOException;
//ExamQuestionService.java
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.sru.thangiah.domain.ExamQuestion;

/**
 * Interface for services related to exam question management within the Online Exam Management System.
 * Defines the contract for service implementations that handle the retrieval, persistence, and utility operations for exam questions.
 */

public interface ExamQuestionService {
	/**
     * Retrieves all exam questions.
     *
     * @return A list of {@link ExamQuestion} entities.
     */
    List<ExamQuestion> getAllExamQuestions();
    
    /**
     * Retrieves a single exam question by its identifier.
     *
     * @param id The identifier of the exam question.
     * @return The {@link ExamQuestion} entity if found, otherwise null.
     */
    ExamQuestion getExamQuestionById(Long id);
    
    /**
     * Persists a given exam question to the database.
     *
     * @param examQuestion The {@link ExamQuestion} entity to be saved.
     */
    void saveExamQuestion(ExamQuestion examQuestion);
    
    /**
     * Deletes an exam question from the database by its identifier.
     *
     * @param id The identifier of the exam question to be deleted.
     */
    void deleteExamQuestion(Long id);
    
    /**
     * Reads exam questions from a file and saves them to the database.
     * 
     * @throws IOException If an I/O error occurs during file processing.
     */
    void readExamQuestionsFromFile() throws IOException;
    
    /**
     * Retrieves exam questions by chapter.
     *
     * @param chapter The chapter number to retrieve questions for.
     * @return A list of {@link ExamQuestion} entities for the specified chapter.
     */
    List<ExamQuestion> getQuestionsByChapter(int chapter);
    
    /**
     * Generates a list of questions for a specific chapter.
     *
     * @param chapter The chapter number to generate questions for.
     * @return A list of {@link ExamQuestion} entities for the chapter.
     */
    public List<ExamQuestion> generateQuestionsForChapter(int chapter);
    
    /**
     * Retrieves all distinct chapter numbers from the database.
     *
     * @return A list of unique chapter numbers.
     */
    public List<Integer> getAllChapters();
    
    /**
     * Reads fill-in-the-blank questions from a file.
     *
     * @return A list of fill-in-the-blank {@link ExamQuestion} entities.
     * @throws IOException If an I/O error occurs during file processing.
     */
    List<ExamQuestion> readBlanksFromFile() throws IOException;
    
    /**
     * Generates a list of fill-in-the-blank questions.
     *
     * @param numBlanks The number of fill-in-the-blank questions to generate.
     * @return A list of fill-in-the-blank {@link ExamQuestion} entities.
     * @throws IOException If an I/O error occurs during the generation process.
     */
	List<ExamQuestion> generateFillInTheBlanksQuestions(int numBlanks) throws IOException;
	
	/**
     * Reads true/false questions from a file.
     *
     * @return A list of true/false {@link ExamQuestion} entities.
     * @throws IOException If an I/O error occurs during file processing.
     */
	List<ExamQuestion> readTrueFalseFromFile() throws IOException;
	
	/**
     * Reads AI-generated questions from a file.
     *
     * @param file The multipart file containing AI-generated questions.
     * @return A list of AI-generated {@link ExamQuestion} entities.
     * @throws IOException If an I/O error occurs during file processing.
     */
    List<ExamQuestion> readAIQuestionsFromFile(MultipartFile file) throws IOException;
    
    /**
     * Finds questions containing a specific text.
     *
     * @param searchText The text to search for within the questions.
     * @return A list of {@link ExamQuestion} entities that contain the specified text.
     */
	List<ExamQuestion> findQuestionsContainingText(String searchText);
	
	/**
     * Retrieves a random selection of true/false questions.
     *
     * @param numTrueFalse The number of true/false questions to retrieve.
     * @return A list of true/false {@link ExamQuestion} entities.
     */
	List<ExamQuestion> getRandomTrueFalseQuestions(int numTrueFalse);
	
	 /**
     * Retrieves a random selection of fill-in-the-blank questions.
     *
     * @param numBlanks The number of fill-in-the-blank questions to retrieve.
     * @return A list of fill-in-the-blank {@link ExamQuestion} entities.
     */
	List<ExamQuestion> getRandomFillInTheBlanksQuestions(int numBlanks);
}

