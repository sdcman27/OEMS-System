package edu.sru.thangiah.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import edu.sru.thangiah.domain.ExamSubmissionEntity;
import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.model.User;
import edu.sru.thangiah.repository.CourseRepository;
import edu.sru.thangiah.repository.ExamRepository;
import edu.sru.thangiah.repository.ExamSubmissionRepository;
import edu.sru.thangiah.repository.InstructorRepository;
import edu.sru.thangiah.repository.RoleRepository;
import edu.sru.thangiah.repository.StudentRepository;
import edu.sru.thangiah.repository.UserRepository;
import edu.sru.thangiah.web.dto.CourseGradeDTO;
import edu.sru.thangiah.web.dto.ExamGradeDTO;

/**
 * Controller responsible for handling student-related web requests.
 * Provides methods for displaying student pages, handling student data, and interacting with the course repository.
 */
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
    private ExamSubmissionRepository examSubmissionRepository;
    
    @Autowired
    private InstructorRepository instructorRepository;

	public StudentController(StudentRepository studentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        super();
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }
	
	/**
    * Displays the student homepage.
    * 
    * @return The name of the HTML template for the student homepage.
    */
	@RequestMapping("/student_homepage")
	public String showStudentHomepage() {
		return "student_homepage";
	}

	/**
     * Shows a list of all students.
     *
     * @param model The model object to which the student list is added.
     * @return The name of the HTML template that displays the list of students.
     */
    @GetMapping("/students")
    public String showStudentList(Model model) {
        // Retrieve the list of students from the repository
        List<Student> students = studentRepository.findAll();

        // Add the list of students to the model for rendering in the HTML template
        model.addAttribute("students", students);

        // Return the name of the HTML template to be displayed
        return "student-list";
    }
    
    /**
     * Saves a student entity along with its associated course.
     *
     * @param student The student entity to be saved.
     * @return The saved student entity.
     */
	@PostMapping
	public Student saveStudentWithCourse(@RequestBody Student student) {
		return studentRepository.save(student);
	}
	
	 /**
     * Retrieves a student by their ID.
     *
     * @param studentId The ID of the student to retrieve.
     * @return The {@link Student} object if found, otherwise null.
     */
	@GetMapping("/{studentId}")
	public Student findStudent(@PathVariable Long studentId) {
		return studentRepository.findById(studentId).orElse(null);
	}
	
	 /**
     * Finds students whose first name contains the given string.
     *
     * @param name The string to search for within student first names.
     * @return A list of {@link Student} objects that meet the search criteria.
     */
	@GetMapping("/find/{name}")
	public List<Student> findStudentsContainingByStudentFirstName(@PathVariable String name){
		return studentRepository.findBystudentFirstNameContaining(name);
	}
	
	/**
     * Finds courses by an ID containing the given number.
     *
     * @param id The ID or part of the ID to search for within course IDs.
     * @return A list of {@link Course} objects that meet the search criteria.
     */
	@GetMapping("/search")
	public List<Course> findByIdContaining(@PathVariable Long id){
		return courseRepository.findByIdContaining(id);
	}
	
	/**
     * Displays the account management page for the student.
     *
     * @param model The {@link Model} object to pass attributes to the view.
     * @return The name of the HTML template for account management.
     */
	@GetMapping("/math-quiz")
    public String mathQuizPage() {
        // displays the math quiz
        return "math-quiz"; // the name of the HTML template for the quiz page
    }
	
	/**
     * Displays the form for editing the current student's information.
     *
     * @param id The ID of the student to edit.
     * @param model The {@link Model} object to pass attributes to the view.
     * @return The name of the HTML template for editing the student.
     */
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
	
	/**
     * Handles the submission of the form for editing the current student's information.
     *
     * @param id The ID of the student being edited.
     * @param student The updated {@link Student} object.
     * @param result The {@link BindingResult} object to hold validation results.
     * @param model The {@link Model} object to pass attributes to the view.
     * @return A string indicating the view to render next.
     */
	@GetMapping("/sv-edit-current-student/{id}")
	public String editingCurrentUser(@PathVariable("id") long id, Model model) {
    	Student student = studentRepository.findById(id)
    		      .orElseThrow(() -> new IllegalArgumentException("Invalid instructor Id:" + id));
    		    
    		    model.addAttribute("student", student);
    		    
	    return "sv-edit-current-student"; 
	}
	
	  /**
     * Handles the submission of the form for editing the current student's information.
     *
     * @param id The ID of the student being edited.
     * @param student The updated {@link Student} object.
     * @param result The {@link BindingResult} object to hold validation results.
     * @param model The {@link Model} object to pass attributes to the view.
     * @return A string indicating the view to render next.
     */
	@Transactional
	@PostMapping("/sv-edit-student/{id}")
	public String saveCurrentUserEdits(@PathVariable("id") long id, @Validated Student student, 
		      BindingResult result, Model model, @RequestParam(value = "currentPassword", required = false) String currentPassword, @RequestParam("newPassword") String newStudentPassword, 
			  @RequestParam(value = "confirmPassword", required = false) String confirmStudentPassword) {
		        if (result.hasErrors()) {
		            student.setStudentId(id);
		            return "sv-edit-current-student";
		        }
		        
		        Student Updatestudent = studentRepository.findByStudentUsername(student.getStudentUsername()).orElse(null);
		        
		        student.setStudentPassword(Updatestudent.getStudentPassword());
		        student.setUser(Updatestudent.getUser());
		        student.setCourses(Updatestudent.getCourses());
		        
		        Updatestudent = student;
		        
		        // Fetch the user (or create a new one if not found)
		        User user = userRepository.findByUsername(Updatestudent.getStudentUsername())
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
		                Updatestudent.setStudentPassword(encryptedPassword);
		                user.setPassword(encryptedPassword); // Update the user's password
		            }
		        }

		        // If there was a password error, re-display the form
		        if (passwordError) {
		            model.addAttribute("student", student);
		            return "sv-edit-current-student";
		        }

		        // Update the user's username and email to match the student
		        user.setUsername(Updatestudent.getStudentUsername());
		        user.setEmail(Updatestudent.getStudentEmail());
		        userRepository.save(user);  // Save the user to userRepository

		        // Debugging: Print the received student data
		        System.out.println("Received Student Data:");
		        System.out.println("ID: " + student.getStudentId());
		        System.out.println("First Name: " + student.getStudentFirstName());
		        System.out.println("Last Name: " + student.getStudentLastName());
		        System.out.println("Email: " + student.getStudentEmail());
		        System.out.println("Path Variable ID: " + id);
		        
		        studentRepository.save(Updatestudent);
	    
	    return "sv-student-edit-confirmation"; 
	}
	
	/**
     * Displays a list of courses that the student is enrolled in.
     *
     * @param model The {@link Model} object to pass attributes to the view.
     * @return The name of the HTML template for the student course list.
     */
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
	    
        Map<Long, Boolean> examTakenMap = new HashMap<>();


	    // retrieve the Course entities and their exams
	    for (Course course : studentCourses) {
	        // Fetch the exams for the current course
	        Set<Exam> examsForCourse = examRepository.findByCourse(course);

	        // Format the start time for each exam
	        for (Exam exam : examsForCourse) {
	        	
	         // If a submission exists, mark the exam as taken
	            boolean hasTaken = examSubmissionRepository.existsByUser_IdAndExam_Id(student.getUser().getId(), exam.getId());
	            examTakenMap.put(exam.getId(), hasTaken);

	            if (exam.getStartTime() != null && !hasTaken) {
	                String formattedStartTime = exam.getStartTime().format(formatter);
	                exam.setFormattedStartTime(formattedStartTime); // Assuming there's a setter for formattedStartTime in the Exam class
	            }
	        }

	        // Set the exams (with formatted start times) back to the course
	        course.setExams(examsForCourse); // Assuming you have setExams in your Course class

	    }

	    // add the list of courses (with their formatted exams) to the model for rendering in the view
	    model.addAttribute("courses", studentCourses);
        model.addAttribute("examTakenMap", examTakenMap);


	    return "sv-course-list";
	}
	
	/**
     * Displays a list of grades for the student across all courses.
     *
     * @param model The {@link Model} object to pass attributes to the view.
     * @return The name of the HTML template for the student grade list.
     */
	@Transactional
	@GetMapping("/sv-grade-list")
	public String showStudentGrades(Model model) {
	    // Retrieve the currently authenticated user's name
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String studentUsername = auth.getName();
	    System.out.println("Authenticated username: " + studentUsername);

	    
	    // Find the User entity associated with the authenticated username
	    User user = userRepository.findByUsername(studentUsername).orElse(null);
	    System.out.println("Found user: " + user);


	    // Create a data structure to hold the courses along with the exams and scores
	    List<CourseGradeDTO> courseGrades = new ArrayList<>();

	    // Check if the user is not null
	    if (user != null) {
	        // Find the Student entity associated with the User entity
	    	Optional<Student> studentOpt = studentRepository.findByUserId(user.getId());
	        System.out.println("Found student: " + studentOpt);


	     // Check if the student is present
	        if (studentOpt.isPresent()) {
	            Student student = studentOpt.get();
	            // Get the set of courses associated with the student
	            Set<Course> studentCourses = student.getCourses();
	            System.out.println("Number of courses found for student: " + studentCourses.size());


	            // Iterate over the courses and compile the grades
	            for (Course course : studentCourses) {
	                CourseGradeDTO courseGrade = new CourseGradeDTO();
	                courseGrade.setCourseName(course.getCourseName());
	                System.out.println("Processing course: " + course.getCourseName());


	                // Retrieve the exams for this course
	                Set<Exam> exams = examRepository.findByCourse(course);
	                System.out.println("Number of exams found for course: " + exams.size());


	                // Compile exam grades for the student
	                for (Exam exam : exams) {
	                	 ExamSubmissionEntity submission = examSubmissionRepository.findByUser_IdAndExam_Id(user.getId(), exam.getId());
	                     Integer score = submission != null ? submission.getScore() : null;
	                     // Here, retrieve the total number of questions for the exam
	                     Integer totalQuestions = exam.getQuestions() != null ? exam.getQuestions().size() : 0;
	                     System.out.println("Exam: " + exam.getExamName() + ", Score: " + score + ", Total Questions: " + totalQuestions);
	                     // Create a new ExamGradeDTO with the total number of questions
	                     ExamGradeDTO examGradeDTO = new ExamGradeDTO(exam.getExamName(), score, totalQuestions);
	                     // Add the ExamGradeDTO to the courseGrade
	                     courseGrade.addExamGrade(examGradeDTO);
	                }
	                
	                courseGrade.calculateTotalScoreAndPercentage();
	                courseGrades.add(courseGrade);
	            }
	        }
	    } 
	    else 
	    	{
	        System.out.println("User not found for username: " + studentUsername);
	    }

	    // Log the size of the courseGrades list after processing
	    System.out.println("Number of course grades: " + courseGrades.size());

	    // Add the list of CourseGradeDTOs to the model for rendering in the view
	    model.addAttribute("courseGrades", courseGrades);

	    return "sv-grade-list";
	}



	
}
