package edu.sru.thangiah;

import edu.sru.thangiah.domain.Exam;
import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.domain.ExamQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ExamTest {

    private Exam exam;

    @BeforeEach
    void setUp() {
        exam = new Exam();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        exam.setId(id);
        assertEquals(id, exam.getId());
    }

    @Test
    void testSetAndGetExamName() {
        String name = "Sample Exam";
        exam.setExamName(name);
        assertEquals(name, exam.getExamName());
    }

    @Test
    void testSetAndGetDuration() {
        int duration = 60;
        exam.setDurationInMinutes(duration);
        assertEquals(duration, exam.getDurationInMinutes());
    }

    @Test
    void testSetAndGetStartTime() {
        LocalDateTime startTime = LocalDateTime.now();
        exam.setStartTime(startTime);
        assertEquals(startTime, exam.getStartTime());
    }

    @Test
    void testSetAndGetEndTime() {
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        exam.setEndTime(endTime);
        assertEquals(endTime, exam.getEndTime());
    }

    @Test
    void testSetAndGetCourse() {
        Course course = new Course();
        exam.setCourse(course);
        assertEquals(course, exam.getCourse());
    }

    @Test
    void testSetAndGetQuestions() {
        List<ExamQuestion> questions = new ArrayList<>();
        questions.add(new ExamQuestion());
        exam.setQuestions(questions);
        assertEquals(questions, exam.getQuestions());
    }

    @Test
    void testSetAndGetSubmissionCount() {
        int count = 5;
        exam.setSubmissionCount(count);
        assertEquals(count, exam.getSubmissionCount());
    }

    @Test
    void testFormatStartTime() {
        LocalDateTime startTime = LocalDateTime.now();
        exam.setStartTime(startTime);
        exam.formatStartTime();
        assertNotNull(exam.getFormattedStartTime());
    }
}
