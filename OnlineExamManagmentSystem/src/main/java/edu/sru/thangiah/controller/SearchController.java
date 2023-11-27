package edu.sru.thangiah.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sru.thangiah.domain.ExamQuestion;
import edu.sru.thangiah.domain.Instructor;
import edu.sru.thangiah.domain.ScheduleManager;
import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.repository.InstructorRepository;
import edu.sru.thangiah.repository.ScheduleManagerRepository;
import edu.sru.thangiah.repository.StudentRepository;
import edu.sru.thangiah.service.ExamQuestionService;


/**
 * The {@code SearchController} class is responsible for handling search-related requests in the application.
 * It provides methods to search for entities such as instructors, schedule managers, students, and exam questions
 * based on different criteria like ID, name, or username. This controller makes use of repository classes to
 * fetch data from the database and uses service classes to perform business logic operations.
 * <p>
 * The searches are performed through GET requests with parameters specifying the type of search and the search
 * terms. The results are then added to the model and the appropriate view is returned to display the results.
 * <p>
 * Autowired components:
 * <ul>
 *     <li>{@code StudentRepository} to access student data</li>
 *     <li>{@code ExamQuestionService} to access exam question operations</li>
 *     <li>{@code InstructorRepository} to access instructor data</li>
 *     <li>{@code ScheduleManagerRepository} to access schedule manager data</li>
 * </ul>
 */
@Controller
public class SearchController {
	
	/*
	 *____  __    __        _ _ 
	 / __ \/ /__ / /__ ___ (_|_)
	/ /_/ / / -_)  '_/(_-</ / / 
	\____/_/\__/_/\_\/___/_/_/  

	                        
	 */

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private ExamQuestionService examQuestionService;
    
    @Autowired
    private InstructorRepository instructorRepository;
    
    @Autowired
    private ScheduleManagerRepository scheduleManagerRepository;

    
    /**
     * Searches for instructors based on a type of search and a parameter.
     * 
     * @param searchType The type of search (e.g., "id", "name", "username").
     * @param searchParam The parameter to search for.
     * @param model The model to add attributes to.
     * @return The name of the view to render.
     */
    @GetMapping("/instructor/search")
    public String searchInstructors(
        @RequestParam("searchType") String searchType,
        @RequestParam("searchParam") String searchParam,
        Model model
    ) {
        // Perform the instructor search based on searchType and searchParam
        if ("id".equalsIgnoreCase(searchType)) {
            try {
                Long instructorId = Long.parseLong(searchParam);
                Instructor instructor = instructorRepository.findById(instructorId).orElse(null);
                if (instructor != null) {
                    model.addAttribute("instructors", List.of(instructor));
                } else {
                    model.addAttribute("instructors", List.of());
                }
            } catch (NumberFormatException e) {
                model.addAttribute("instructors", List.of());
            }
        } else if ("name".equalsIgnoreCase(searchType)) {
            List<Instructor> instructors = instructorRepository.findByInstructorFirstNameContaining(searchParam);
            model.addAttribute("instructors", instructors);
        } else if ("username".equalsIgnoreCase(searchType)) {
            List<Instructor> instructors = instructorRepository.findByInstructorUsernameContaining(searchParam);
            model.addAttribute("instructors", instructors);
        } else {
            model.addAttribute("instructors", List.of());
        }

        return "instructor-list";
    }
    
    /**
     * Searches for schedule managers based on a type of search and a parameter.
     * 
     * @param searchType The type of search (e.g., "id", "name", "username").
     * @param searchParam The parameter to search for.
     * @param model The model to add attributes to.
     * @return The name of the view to render.
     */
    @GetMapping("/schedule-manager/search")
    public String searchScheduleManagers(
        @RequestParam("searchType") String searchType,
        @RequestParam("searchParam") String searchParam,
        Model model
    ) {
        // Perform the schedule manager search based on searchType and searchParam
        if ("id".equalsIgnoreCase(searchType)) {
            try {
                Long managerId = Long.parseLong(searchParam);
                ScheduleManager manager = scheduleManagerRepository.findById(managerId).orElse(null);
                if (manager != null) {
                    model.addAttribute("scheduleManagers", List.of(manager));
                } else {
                    model.addAttribute("scheduleManagers", List.of());
                }
            } catch (NumberFormatException e) {
                model.addAttribute("scheduleManagers", List.of());
            }
        } else if ("name".equalsIgnoreCase(searchType)) {
            List<ScheduleManager> managers = scheduleManagerRepository.findByManagerFirstNameContaining(searchParam);
            model.addAttribute("scheduleManagers", managers);
        } else if ("username".equalsIgnoreCase(searchType)) {
            List<ScheduleManager> managers = scheduleManagerRepository.findByManagerUsernameContaining(searchParam);
            model.addAttribute("scheduleManagers", managers);
        } else {
            model.addAttribute("scheduleManagers", List.of());
        }

        return "schedule-manager-list";
    }
    
    

    /**
     * Searches for students based on a type of search and a parameter.
     * 
     * @param searchType The type of search (e.g., "id", "name", "username").
     * @param searchParam The parameter to search for.
     * @param model The model to add attributes to.
     * @return The name of the view to render.
     */
    @GetMapping("/student/search")
    public String searchStudents(
        @RequestParam("searchType") String searchType,
        @RequestParam("searchParam") String searchParam,
        Model model
    ) {
        // Perform the student search based on searchType and searchParam
        if ("id".equalsIgnoreCase(searchType)) {
            try {
                Long studentId = Long.parseLong(searchParam);
                Student student = studentRepository.findById(studentId).orElse(null);
                if (student != null) {
                    model.addAttribute("students", List.of(student));
                } else {
                    model.addAttribute("students", List.of()); // No matching students
                }
            } catch (NumberFormatException e) {
                model.addAttribute("students", List.of()); // Invalid input
            }
        } else if ("name".equalsIgnoreCase(searchType)) {
            List<Student> students = studentRepository.findBystudentFirstNameContaining(searchParam);
            model.addAttribute("students", students);
        } else if ("username".equalsIgnoreCase(searchType)) {
            List<Student> students = studentRepository.findBystudentUsernameContaining(searchParam);
            model.addAttribute("students", students);
        } else {
            model.addAttribute("students", List.of()); 
        }

        return "iv-student-list"; 
    }
    
    /**
     * Searches for exam questions based on a type of search and a parameter.
     * 
     * @param searchType The type of search (e.g., "id", "text").
     * @param searchParam The parameter to search for.
     * @param model The model to add attributes to.
     * @return The name of the view to render.
     */
    @GetMapping("/exam-question/search")
    public String searchExamQuestions(
        @RequestParam("searchType") String searchType,
        @RequestParam("searchParam") String searchParam,
        Model model
    ) {
        List<ExamQuestion> questions = new ArrayList<>();
        if ("id".equalsIgnoreCase(searchType)) {
            try {
                Long id = Long.parseLong(searchParam);
                ExamQuestion question = examQuestionService.getExamQuestionById(id);
                if (question != null) {
                    questions.add(question);
                }
            } catch (NumberFormatException e) {
                // Handle invalid ID format (e.g., non-numeric)
            }
        } else if ("text".equalsIgnoreCase(searchType)) {
            questions = examQuestionService.findQuestionsContainingText(searchParam);
        }
        model.addAttribute("examQuestions", questions);
        return "listExamQuestions"; 
    }



}
