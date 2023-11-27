package edu.sru.thangiah.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sru.thangiah.domain.Exam;
import edu.sru.thangiah.domain.ExamQuestion;
import edu.sru.thangiah.domain.ExamResult;
import edu.sru.thangiah.domain.Question;
import edu.sru.thangiah.repository.ExamQuestionRepository;
import edu.sru.thangiah.repository.ExamRepository;
import edu.sru.thangiah.repository.ExamSubmissionRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/*
 *____  __    __        _ _ 
 / __ \/ /__ / /__ ___ (_|_)
/ /_/ / / -_)  '_/(_-</ / / 
\____/_/\__/_/\_\/___/_/_/  
                        
 */

/**
 * Service class for managing exams and their related data such as questions and results.
 * <p>
 * This service handles operations for creating, updating, and deleting exams,
 * evaluating exam answers, and generating various reports and question sets.
 * </p>
 *
 * The ASCII art in the class comment section is a stylistic representation and serves no functional purpose.
 *
 * 
 */
@Service
public class ExamService {
	
    private ExamResult storedExamResult;
    
    @Autowired
    private ExamRepository examRepository;
    
    @Autowired
    private ExamQuestionRepository examQuestionRepository;
    
    @Autowired
    private ExamSubmissionRepository examSubmissionRepository;

    private List<Question> allQuestions = new ArrayList<>();

    
    /**
     * Retrieves a list of all available questions.
     *
     * @return A list of {@link Question} entities.
     */
    public List<Question> getAllQuestions() {
        return allQuestions;
    }
    
    /**
     * Retrieves a specific exam by its identifier.
     *
     * @param id The identifier of the exam to retrieve.
     * @return The {@link Exam} entity if found, or null otherwise.
     */
    public Exam getExamById(Long id) {
        return examRepository.findById(id).orElse(null);
    }
    
    /**
     * Evaluates the answers submitted by a user and calculates the exam result.
     *
     * @param userAnswers A map containing user answers with the question index as the key.
     * @return The {@link ExamResult} with the user's score and answer details.
     */

    public ExamResult evaluateAnswers(Map<Integer, String> userAnswers) {
        int score = 0;
        Map<String, String> correctAnswers = new HashMap<>();
        Map<String, String> incorrectAnswersWithCorrections = new HashMap<>();

        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            Integer questionIndex = entry.getKey();
            String userAnswer = entry.getValue();

            if (questionIndex < 0 || questionIndex >= allQuestions.size()) {
                continue;
            }

            Question question = allQuestions.get(questionIndex);
            if (question.getCorrectAnswer().equalsIgnoreCase(userAnswer)) {
                score++;
                correctAnswers.put(question.getQuestionText(), userAnswer);
            } else {
                incorrectAnswersWithCorrections.put(question.getQuestionText(), question.getCorrectAnswer());
            }
        }

        ExamResult result = new ExamResult();
        result.setScore(score);
        result.setCorrectAnswers(new ArrayList<>(correctAnswers.keySet())); // or store the entire map if needed
        result.setIncorrectAnswersWithCorrections(incorrectAnswersWithCorrections);

        return result;
    }

    /**
     * Temporarily stores the result of an exam for a user. 
     * This is a placeholder and should ideally be persisted in a database.
     *
     * @param result The {@link ExamResult} to be stored.
     */

    public void storeExamResultForUser(ExamResult result) {
        this.storedExamResult = result;
    }

    /**
     * Retrieves the stored exam result for a user.
     *
     * @return The {@link ExamResult} that was stored previously.
     */
    public ExamResult getStoredExamResultForUser() {
        return this.storedExamResult;
    }
    /**
     * Randomly selects a subset of questions to form an exam.
     *
     * @return A list of randomly selected {@link Question} entities.
     */
    public List<Question> getRandomQuestions() {
        List<Question> selectedQuestions = new ArrayList<>();
        Collections.shuffle(allQuestions);
        int toIndex = Math.min(10, allQuestions.size());
        selectedQuestions.addAll(allQuestions.subList(0, toIndex));
        return selectedQuestions;
    }
    
    /**
     * Reads true/false questions from an Excel file located at the given resource path.
     *
     * @param resourcePath The classpath resource location of the Excel file to be read.
     * @return A list of {@link Question} entities containing the true/false questions.
     */
    private List<Question> readTrueFalseQuestions(String resourcePath) {
        List<Question> questions = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream(resourcePath);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null) {
                    continue;
                }
                Question question = new Question();
                question.setQuestionText(getCellValue(row.getCell(0)));
                Map<String, String> options = new HashMap<>();
                options.put("A", "True");
                options.put("B", "False");
                question.setOptions(options);
                String answer = getCellValue(row.getCell(1));
                if (answer.startsWith("Ans: ")) {
                    question.setCorrectAnswer(answer.substring(5).trim());
                }
                questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return ""; // Handle the case where the cell is null
        }
        
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        }
        return "";
    }


    public List<Question> generateExam(int chapterOrExamType, int numberOfQuestions) {
        List<Question> examQuestions = new ArrayList<>();
        // Based on the chapter, we decide which files to read.
        if (chapterOrExamType <= 4) {
            // For individual chapters, we load specific files.
            examQuestions.addAll(readQuestionsFromExcel("/static/chapter-" + chapterOrExamType + ".xlsx", true)); // true indicates we want to read true/false questions as well.
        } else if (chapterOrExamType == 5) { // Mid-Term
            examQuestions.addAll(readQuestionsFromExcel("/static/chapter-1.xlsx", false));
            examQuestions.addAll(readQuestionsFromExcel("/static/chapter-2.xlsx", false));
        } else if (chapterOrExamType == 6) { // Final Exam
            for (int i = 1; i <= 4; i++) {
                examQuestions.addAll(readQuestionsFromExcel("/static/chapter-" + i + ".xlsx", false));
            }
        }
        Collections.shuffle(examQuestions);
        return examQuestions.stream().limit(numberOfQuestions).collect(Collectors.toList());
    }


    private List<Question> readQuestionsFromExcel(String resourcePath, boolean includeTrueFalse) {
        List<Question> questions = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream(resourcePath);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); ) {
                Row questionRow = sheet.getRow(i);
                if (questionRow == null || questionRow.getCell(0) == null) {
                    continue;
                }
                Question question = new Question();
                question.setQuestionText(getCellValue(questionRow.getCell(0)));
                Map<String, String> options = new HashMap<>();
                for (int j = 1; j <= 4; j++) {
                    Row optionRow = sheet.getRow(i + j);
                    if (optionRow == null || optionRow.getCell(0) == null || optionRow.getCell(1) == null) {
                        continue;
                    }
                    options.put(getCellValue(optionRow.getCell(0)), getCellValue(optionRow.getCell(1)));
                }
                question.setOptions(options);
                i += 5;
                Row answerRow = sheet.getRow(i);
                if (answerRow != null && answerRow.getCell(0) != null) {
                    String answer = getCellValue(answerRow.getCell(0));
                    if (answer.startsWith("Ans: ")) {
                        question.setCorrectAnswer(answer.substring(5).trim());
                    }
                }
                questions.add(question);
                i++;
            }
            // If we need to include true/false questions, we read them from a designated file.
            if (includeTrueFalse) {
                questions.addAll(readTrueFalseQuestions(resourcePath.replace(".xlsx", "_TF.xlsx"))); // Assuming a naming convention for true/false files.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }
    
    /**
     * Creates an Excel file for the given list of questions.
     *
     * @param questions The list of {@link Question} entities to include in the Excel file.
     * @return A byte array containing the contents of the Excel file.
     */
    
    public byte[] createExcelFile(List<Question> questions) {

        // This method calls the ExcelFileExporter utility class to create the Excel content.
        ByteArrayInputStream in = ExcelGeneratorService.createExcelFile(questions);
        try {
            return IOUtils.toByteArray(in); // Convert the InputStream to a byte array
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert input stream to byte array", e);
        }
    }
    /**
     * Fetches distinct chapter numbers from the repository.
     *
     * @return A list of distinct chapter numbers.
     */
    public List<Integer> getAllChapters() {
        // This method should interact with the repository to fetch all distinct chapters available.
        // Assuming you have a method in your repository class that fetches all unique chapter numbers.
        return examQuestionRepository.findAllDistinctChapters();
    }
    /**
     * Generates a list of questions for a specific chapter.
     *
     * @param chapter The chapter number to fetch questions for.
     * @return A list of {@link ExamQuestion} entities for the given chapter.
     */
    public List<ExamQuestion> generateQuestionsForChapter(int chapter) {
        // This method fetches questions from a specific chapter.
        // Assumes you have a method in your repository to find questions by chapter.
        return examQuestionRepository.findQuestionsByChapter(chapter);
    }
    /**
     * Deletes an exam if it has no submissions.
     *
     * @param examId The identifier of the exam to delete.
     * @return True if the exam was deleted, false otherwise.
     */
    public boolean deleteExam(Long examId) {
        Optional<Exam> examOptional = examRepository.findById(examId);
        if (examOptional.isPresent()) {
            Exam exam = examOptional.get();

            // Check if there are any submissions for this exam
            if (examSubmissionRepository.countByExam(exam) > 0) {
                System.out.println("Cannot delete exam with ID: " + examId + " as it has submissions.");
                return false; // Indicate that the exam cannot be deleted
            }

            // If no submissions, delete the exam
            examRepository.delete(exam);
            System.out.println("Deleted exam with ID: " + examId);
            return true; // Indicate successful deletion
        } else {
            System.out.println("Exam with ID " + examId + " not found.");
            return false;
        }
    }



    /**
     * Retrieves all questions from the repository.
     *
     * @return A list of all {@link ExamQuestion} entities.
     */
    public List<ExamQuestion> getAllExamQuestions() {
        return examQuestionRepository.findAll();
    }

    /**
     * Updates the questions for an existing exam.
     *
     * @param examId       The identifier of the exam to update.
     * @param questionIds  The list of question identifiers to associate with the exam.
     */
    public void updateExamQuestions(Long examId, List<Long> questionIds) {
        Optional<Exam> examOptional = examRepository.findById(examId);
        if (!examOptional.isPresent()) {
            throw new RuntimeException("Exam not found"); // Handle this case as per your application's requirement
        }

        Exam exam = examOptional.get();
        List<ExamQuestion> updatedQuestions = questionIds.stream()
                                                         .map(examQuestionRepository::findById)
                                                         .filter(Optional::isPresent)
                                                         .map(Optional::get)
                                                         .collect(Collectors.toList());
        exam.setQuestions(updatedQuestions);
        examRepository.save(exam);
    }
    
    /**
     * Retrieves all exams with their submission counts.
     *
     * @return A list of {@link Exam} entities with updated submission counts.
     */
    public List<Exam> getAllExamsWithSubmissionCount() {
        List<Exam> exams = examRepository.findAll();
        for (Exam exam : exams) {
            int count = (int) examSubmissionRepository.countByExam(exam);
            exam.setSubmissionCount(count);
        }
        return exams;
    }




    

    
    

}
    
    

