package edu.sru.thangiah.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.repository.StudentRepository;

@Controller
public class SearchController {

    @Autowired
    private StudentRepository studentRepository;

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

        return "student-list"; 
    }
}
