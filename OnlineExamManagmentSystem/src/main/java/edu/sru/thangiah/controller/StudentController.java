package edu.sru.thangiah.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.model.Roles;
import edu.sru.thangiah.model.User;
import edu.sru.thangiah.repository.CourseRepository;
import edu.sru.thangiah.repository.RoleRepository;
import edu.sru.thangiah.repository.StudentRepository;
import edu.sru.thangiah.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequestMapping("/student/course")
public class StudentController 
{
    @Autowired
	private StudentRepository studentRepository;
    @Autowired
	private CourseRepository courseRepository;
    @Autowired
	private UserRepository userRepository;
    @Autowired
	private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

	public StudentController(StudentRepository studentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        super();
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }
	
	@RequestMapping("/student_homepage")
	public String showStudentHomepage() {
		return "student_homepage";
	}

    @GetMapping("/students")
    public String showStudentList(Model model) {
        // Retrieve the list of students from the repository
        List<Student> students = studentRepository.findAll();

        // Add the list of students to the model for rendering in the HTML template
        model.addAttribute("students", students);

        // Return the name of the HTML template to be displayed
        return "student-list";
    }
    
	@PostMapping
	public Student saveStudentWithCourse(@RequestBody Student student) {
		return studentRepository.save(student);
	}
	
	
	@GetMapping("/{studentId}")
	public Student findStudent(@PathVariable Long studentId) {
		return studentRepository.findById(studentId).orElse(null);
	}
	
	@GetMapping("/find/{name}")
	public List<Student> findStudentsContainingByStudentFirstName(@PathVariable String name){
		return studentRepository.findBystudentFirstNameContaining(name);
	}
	
	@GetMapping("/search")
	public List<Course> findByIdContaining(@PathVariable Long id){
		return courseRepository.findByIdContaining(id);
	}
	
	@GetMapping("/math-quiz")
    public String mathQuizPage() {
        // displays the math quiz
        return "math-quiz"; // the name of the HTML template for the quiz page
    }

}
