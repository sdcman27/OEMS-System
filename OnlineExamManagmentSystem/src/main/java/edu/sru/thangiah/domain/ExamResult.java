package edu.sru.thangiah.domain;

import java.util.List;
import java.util.Map;

/**
 * The {@code ExamResult} class represents the result of an examination attempt.
 * It includes the score and lists of correct and incorrect answers,
 * as well as a map of incorrect answers with their corrections.
 */
public class ExamResult {
	
	private int score;
    private List<String> correctAnswers;
    private List<String> incorrectAnswers;
    private Map<String, String> incorrectAnswersWithCorrections;

    /**
     * Retrieves the score of the exam.
     *
     * @return The score as an integer.
     */
	public int getScore() {
		return score;
	}
	
	 /**
     * Sets the score for the exam.
     *
     * @param score The score to set.
     */
	public void setScore(int score) {
		this.score = score;
	}
	/**
     * Retrieves a list of correct answers.
     *
     * @return A list of correct answers.
     */
	public List<String> getCorrectAnswers() {
		return correctAnswers;
	}
	
    /**
     * Sets the list of correct answers.
     *
     * @param correctAnswers The list of correct answers to set.
     */
	public void setCorrectAnswers(List<String> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}
	
	 /**
     * Retrieves a list of incorrect answers.
     *
     * @return A list of incorrect answers.
     */
	public List<String> getIncorrectAnswers() {
		return incorrectAnswers;
	}
	
	/**
     * Sets the list of incorrect answers.
     *
     * @param incorrectAnswers The list of incorrect answers to set.
     */
	public void setIncorrectAnswers(List<String> incorrectAnswers) {
		this.incorrectAnswers = incorrectAnswers;
	}
	
	 /**
     * Retrieves a map of incorrect answers with their corrections.
     *
     * @return A map where keys are incorrect answers and values are the corrections.
     */
	public Map<String, String> getIncorrectAnswersWithCorrections() {
        return incorrectAnswersWithCorrections;
    }

    /**
     * Sets the map of incorrect answers with their corrections.
     *
     * @param incorrectAnswersWithCorrections The map to set, where keys are incorrect answers and values are the corrections.
     */
    public void setIncorrectAnswersWithCorrections(Map<String, String> incorrectAnswersWithCorrections) {
        this.incorrectAnswersWithCorrections = incorrectAnswersWithCorrections;
    }

    
}
