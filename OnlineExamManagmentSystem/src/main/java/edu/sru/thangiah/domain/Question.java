package edu.sru.thangiah.domain;

import java.util.Map;


/**
 * The Question class represents a question with a set of possible answers
 * and a correct answer within an examination or quiz context.
 */
public class Question {
    private String questionText;
    private Map<String, String> options;
    private String correctAnswer; 


    /**
     * Gets the text of the question.
     *
     * @return The text of the question.
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Sets the text of the question.
     *
     * @param questionText The text to be set for the question.
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Gets the options for the question.
     *
     * @return A map of option identifiers to their corresponding text.
     */
    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * Sets the options for the question.
     *
     * @param options A map of option identifiers to their corresponding text.
     */
    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    /**
     * Gets the correct answer for the question.
     *
     * @return The correct answer identifier.
     */
	public String getCorrectAnswer() {
		return correctAnswer;
	}

	/**
     * Sets the correct answer for the question.
     *
     * @param correctAnswer The correct answer identifier to be set.
     */
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
    
    
}
