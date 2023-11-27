package edu.sru.thangiah.domain;

import java.util.List;


/**
 * The {@code ExamDetails} class encapsulates the details of an exam,
 * including the selected questions, exam name, duration, and associated chapter.
 */
public class ExamDetails {
    private List<Long> selectedExamQuestionIds;
    private String examName;
    private int durationInMinutes; // Duration in minutes
    private int chapter;
    
    /**
     * Retrieves the IDs of the selected questions for the exam.
     *
     * @return A list of question IDs.
     */
	public List<Long> getSelectedExamQuestionIds() {
		return selectedExamQuestionIds;
	}
	
	/**
     * Sets the IDs of the selected questions for the exam.
     *
     * @param selectedExamQuestionIds A list of selected question IDs.
     */
	public void setSelectedExamQuestionIds(List<Long> selectedExamQuestionIds) {
		this.selectedExamQuestionIds = selectedExamQuestionIds;
	}
	
	/**
     * Retrieves the name of the exam.
     *
     * @return The exam name as a string.
     */
	public String getExamName() {
		return examName;
	}
	
	/**
     * Sets the name of the exam.
     *
     * @param examName The name of the exam.
     */
	public void setExamName(String examName) {
		this.examName = examName;
	}
	
	 /**
     * Retrieves the duration of the exam in minutes.
     *
     * @return The duration of the exam in minutes.
     */
	public int getDurationInMinutes() {
		return durationInMinutes;
	}
	
	/**
     * Sets the duration of the exam in minutes.
     *
     * @param examDuration The duration of the exam.
     */
	public void setDurationInMinutes(int examDuration) {
		this.durationInMinutes = examDuration;
	}
	
	/**
     * Retrieves the chapter associated with the exam.
     *
     * @return The chapter number.
     */
	public int getChapter() {
		return chapter;
	}
	
	/**
     * Sets the chapter associated with the exam.
     *
     * @param chapter The chapter number.
     */
	public void setChapter(int chapter) {
		this.chapter = chapter;
	}
	

    
    
}