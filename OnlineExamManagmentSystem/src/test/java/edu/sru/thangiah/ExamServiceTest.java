package edu.sru.thangiah;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.sru.thangiah.repository.ExamQuestionRepository;
import edu.sru.thangiah.repository.ExamRepository;
import edu.sru.thangiah.repository.ExamSubmissionRepository;
import edu.sru.thangiah.service.ExamService;
import edu.sru.thangiah.domain.Exam;
import edu.sru.thangiah.domain.Question;
import edu.sru.thangiah.domain.ExamResult;
import edu.sru.thangiah.domain.ExamSubmission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ExamQuestionRepository examQuestionRepository;

    @Mock
    private ExamSubmissionRepository examSubmissionRepository;

    @InjectMocks
    private ExamService examService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExamById() {
        Long examId = 1L;
        Exam mockExam = new Exam();
        mockExam.setId(examId);
        when(examRepository.findById(examId)).thenReturn(Optional.of(mockExam));

        Exam result = examService.getExamById(examId);

        assertEquals(mockExam, result);
    }

    @Test
    void testUpdateExamName() {
        Long examId = 1L;
        String newName = "Updated Name";
        Exam mockExam = new Exam();
        mockExam.setId(examId);
        when(examRepository.findById(examId)).thenReturn(Optional.of(mockExam));
        when(examRepository.save(any(Exam.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = examService.updateExamName(examId, newName);

        assertTrue(result);
        assertEquals(newName, mockExam.getExamName());
    }

    @Test
    void testUpdateDuration() {
        Long examId = 1L;
        int newDuration = 120;
        Exam mockExam = new Exam();
        mockExam.setId(examId);
        when(examRepository.findById(examId)).thenReturn(Optional.of(mockExam));
        when(examRepository.save(any(Exam.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = examService.updateDuration(examId, newDuration);

        assertTrue(result);
        assertEquals(newDuration, mockExam.getDurationInMinutes());
    }

    @Test
    void testUpdateStartTime() {
        Long examId = 1L;
        LocalDateTime newStartTime = LocalDateTime.now();
        Exam mockExam = new Exam();
        mockExam.setId(examId);
        when(examRepository.findById(examId)).thenReturn(Optional.of(mockExam));
        when(examRepository.save(any(Exam.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = examService.updateStartTime(examId, newStartTime);

        assertTrue(result);
        assertEquals(newStartTime, mockExam.getStartTime());
    }

    @Test
    void testUpdateEndTime() {
        Long examId = 1L;
        LocalDateTime newEndTime = LocalDateTime.now();
        Exam mockExam = new Exam();
        mockExam.setId(examId);
        when(examRepository.findById(examId)).thenReturn(Optional.of(mockExam));
        when(examRepository.save(any(Exam.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = examService.updateEndTime(examId, newEndTime);

        assertTrue(result);
        assertEquals(newEndTime, mockExam.getEndTime());
    }

    @Test
    void testAnswers() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setCorrectAnswer("A");
        questions.add(q1);

        examService.allQuestions = questions;

        Map<Integer, String> userAnswers = new HashMap<>();
        userAnswers.put(0, "A");

        ExamResult result = examService.evaluateAnswers(userAnswers);

        assertEquals(1, result.getScore());
        assertTrue(result.getCorrectAnswers().contains(q1.getQuestionText()));
    }
    
    @Test
    void testUpdateExamDetails() {
        // Test updating details for an existing exam
        Exam existingExam = new Exam();
        existingExam.setId(1L);
        existingExam.setExamName("Old Name");
        when(examRepository.findById(1L)).thenReturn(Optional.of(existingExam));

        Exam updatedExam = new Exam();
        updatedExam.setId(1L);
        updatedExam.setExamName("New Name");
        updatedExam.setDurationInMinutes(60);
        updatedExam.setStartTime(LocalDateTime.now());
        updatedExam.setEndTime(LocalDateTime.now().plusHours(1));

        boolean result = examService.updateExamDetails(updatedExam);
        assertTrue(result);

        assertEquals("New Name", existingExam.getExamName());
        assertEquals(60, existingExam.getDurationInMinutes());

        // Test updating details for a non-existing exam
        when(examRepository.findById(2L)).thenReturn(Optional.empty());
        System.out.println("Mock result: " + examRepository.findById(2L));

    }

    @Test
    void testEvaluateAnswers() {
        Map<Integer, String> userAnswers = Map.of(
                0, "True",
                1, "False",
                2, "True"
        );

        ExamResult result = examService.evaluateAnswers(userAnswers);

        assertEquals(2, result.getScore());
        assertEquals(2, result.getCorrectAnswers().size());
        assertTrue(result.getCorrectAnswers().contains("Question 1"));
        assertTrue(result.getCorrectAnswers().contains("Question 3"));

        assertEquals(1, result.getIncorrectAnswersWithCorrections().size());
        assertTrue(result.getIncorrectAnswersWithCorrections().containsKey("Question 2"));
        assertEquals("True", result.getIncorrectAnswersWithCorrections().get("Question 2"));
    }


    private List<Question> createSampleQuestions() {
        List<Question> questions = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Question question = new Question();
            question.setQuestionText("Question " + i);
            question.setCorrectAnswer(i % 2 == 0 ? "True" : "False");
            questions.add(question);
        }
        return questions;
    }


}
