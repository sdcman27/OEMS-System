package edu.sru.thangiah.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code ExamQuestionDisplay} class is a data transfer object (DTO)
 * that encapsulates the information needed to display a question and its associated user answer
 * along with the correct answer text for review purposes after an exam has been taken.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionDisplay {
    private Long id;
    private String questionText;
    private String userAnswer;
    private String correctAnswerText;
    
    /**
     * Retrieves the text of the correct answer.
     *
     * @return A string representing the correct answer.
     */
    public String getCorrectAnswerText() {
        return correctAnswerText;
    }

    /**
     * Sets the text of the correct answer.
     *
     * @param correctAnswerText The text of the correct answer.
     */
    public void setCorrectAnswerText(String correctAnswerText) {
        this.correctAnswerText = correctAnswerText;
    }
}
