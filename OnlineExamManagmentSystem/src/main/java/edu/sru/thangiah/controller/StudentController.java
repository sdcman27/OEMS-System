package edu.sru.thangiah.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.domain.Exam;
import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.model.User;
import edu.sru.thangiah.repository.CourseRepository;
import edu.sru.thangiah.repository.ExamRepository;
import edu.sru.thangiah.repository.InstructorRepository;
import edu.sru.thangiah.repository.RoleRepository;
import edu.sru.thangiah.repository.StudentRepository;
import edu.sru.thangiah.repository.UserRepository;

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
	private ExamRepository examRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private InstructorRepository instructorRepository;

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
	
	@GetMapping("/sv-account-management")
	public String accountManager(Model model){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		System.out.println(auth.getName());
		System.out.println(auth.getPrincipal());
		System.out.println(auth.getDetails());
		
		String studentUser = auth.getName();
		
		List<Student> student = studentRepository.findBystudentUsernameContaining(studentUser);
		
		//System.out.println(CurrentManager.);
		
		model.addAttribute("student", student);
					
		return "sv-account-management";
	}
	
	@GetMapping("/sv-edit-current-student/{id}")
	public String editingCurrentUser(@PathVariable("id") long id, Model model) {
    	Student student = studentRepository.findById(id)
    		      .orElseThrow(() -> new IllegalArgumentException("Invalid instructor Id:" + id));
    		    
    		    model.addAttribute("student", student);
    		    
	    return "sv-edit-current-student"; 
	}
	
	@Transactional
	@PostMapping("/sv-edit-student/{id}")
	public String saveCurrentUserEdits(@PathVariable("id") long id, @Validated Student student, 
		      BindingResult result, Model model, @RequestParam(value = "currentPassword", required = false) String currentPassword, @RequestParam("newPassword") String newStudentPassword, 
			  @RequestParam(value = "confirmPassword", required = false) String confirmStudentPassword) {
		        if (result.hasErrors()) {
		            student.setStudentId(id);
		            return "sv-edit-current-student";
		        }
		        
		        // Fetch the user (or create a new one if not found)
		        User user = userRepository.findByUsername(student.getStudentUsername())
		                .orElse(new User());  

		        boolean passwordError = false;

		        // Only process passwords if new password fields are filled
		        if (newStudentPassword != null && !newStudentPassword.isEmpty()) {
		            // Verify current password if provided
		            if (currentPassword != null && !passwordEncoder.matches(currentPassword, user.getPassword())) {
		                model.addAttribute("passwordError", "Current password is incorrect");
		                passwordError = true;
		            } 
		            // Check that both the new password and the confirm password field are the same
		            else if (!newStudentPassword.equals(confirmStudentPassword)) {
		                model.addAttribute("passwordError", "New passwords do not match");
		                passwordError = true;
		            } 
		            // Encrypt and set the new password if there's no error
		            else {
		                String encryptedPassword = passwordEncoder.encode(newStudentPassword);
		                student.setStudentPassword(encryptedPassword);
		                user.setPassword(encryptedPassword); // Update the user's password
		            }
		        }

		        // If there was a password error, re-display the form
		        if (passwordError) {
		            model.addAttribute("student", student);
		            return "sv-edit-current-student";
		        }

		        // Update the user's username and email to match the student
		        user.setUsername(student.getStudentUsername());
		        user.setEmail(student.getStudentEmail());
		        userRepository.save(user);  // Save the user to userRepository

		        // Debugging: Print the received student data
		        System.out.println("Received Student Data:");
		        System.out.println("ID: " + student.getStudentId());
		        System.out.println("First Name: " + student.getStudentFirstName());
		        System.out.println("Last Name: " + student.getStudentLastName());
		        System.out.println("Email: " + student.getStudentEmail());
		        System.out.println("Path Variable ID: " + id);
		        

//		        student.setStudentId(id);
//		        student.setRole(student.getRole());
//		        student.setStudentPassword(student.getStudentPassword());
		        studentRepository.save(student);
	    
	    return "sv-student-edit-confirmation"; 
	}
	

	@GetMapping("/sv-course-list")
	public String showStudentCourses(Model model) {
	    // retrieve the currently authenticated user's name
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String studentUser = auth.getName();

	    // find the Student entity associated with the authenticated user
	    Student student = studentRepository.findByStudentUsername(studentUser).orElse(null);

	    // get the set of courses associated with the student
	    Set<Course> studentCourses = student.getCourses();

	    // Initialize a formatter
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	    // retrieve the Course entities and their exams
	    for (Course course : studentCourses) {
	        // Fetch the exams for the current course
	        Set<Exam> examsForCourse = examRepository.findByCourse(course);

	        // Format the start time for each exam
	        for (Exam exam : examsForCourse) {
	            // Check if the exam has a startTime to avoid NullPointerException
	            if (exam.getStartTime() != null) {
	                String formattedStartTime = exam.getStartTime().format(formatter);
	                exam.setFormattedStartTime(formattedStartTime); // Assuming there's a setter for formattedStartTime in the Exam class
	            }
	        }

	        // Set the exams (with formatted start times) back to the course
	        course.setExams(examsForCourse); // Assuming you have setExams in your Course class
	    }

	    // add the list of courses (with their formatted exams) to the model for rendering in the view
	    model.addAttribute("courses", studentCourses);

	    return "sv-course-list";
	}
	
	
}
