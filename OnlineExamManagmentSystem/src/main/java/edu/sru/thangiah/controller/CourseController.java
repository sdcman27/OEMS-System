package edu.sru.thangiah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.repository.CourseRepository;

/**
 * Controller class for handling course-related actions.
 * This class is responsible for managing the course data and interactions within the application.
 */
@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;
    
    
    /**
     * Handles the request to add a new course to the system.
     * Only administrators are allowed to add courses, which is enforced by the PreAuthorize annotation.
     *
     * @param course The course object populated from the form submission.
     * @param model  The UI Model to pass attributes to the view template.
     * @return The name of the view to be rendered, depending on the outcome of the add operation.
     */
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/add-course")
    public String addCourse(@ModelAttribute Course course, Model model) {
        System.out.println("Inside addCourse method");  // Debugging line
        try {
            // Save the course to the database
            Course savedCourse = courseRepository.save(course);
            if (savedCourse != null) {
                model.addAttribute("message", "Course added successfully.");
                return "redirect:/course-success-page"; // Redirect to a success page
            } else {
                model.addAttribute("message", "Error adding course. Controller");
                return "add-course"; // Stay on the same page and display the error
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error adding course. Controller Error");
            return "add-course"; // Stay on the same page and display the error
        }
    }
}