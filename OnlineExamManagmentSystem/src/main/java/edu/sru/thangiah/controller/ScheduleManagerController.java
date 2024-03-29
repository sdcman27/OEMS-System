package edu.sru.thangiah.controller;
 

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.sru.thangiah.domain.Course;
import edu.sru.thangiah.domain.Instructor;
import edu.sru.thangiah.domain.ScheduleManager;
import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.model.Roles;
import edu.sru.thangiah.model.User;
import edu.sru.thangiah.repository.CourseRepository;
import edu.sru.thangiah.repository.InstructorRepository;
import edu.sru.thangiah.repository.RoleRepository;
import edu.sru.thangiah.repository.ScheduleManagerRepository;
import edu.sru.thangiah.repository.StudentRepository;
import edu.sru.thangiah.repository.UserRepository;
import edu.sru.thangiah.service.ExcelExportService;
import edu.sru.thangiah.web.dto.InstructorDTO;
import edu.sru.thangiah.web.dto.StudentDTO;


/**
 * Controller responsible for handling schedule manager-related web requests.
 * Provides methods for displaying schedule manager pages, handling user and course data.
 */
@Controller
@RequestMapping("/schedule-manager")
public class ScheduleManagerController {
	
	
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ScheduleManagerRepository scheduleManagerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ExcelExportService excelExportService;
    
    
    /**
	 * Displays the homepage for schedule managers.
	 *
	 * @return The name of the HTML template for the schedule manager homepage.
	 */
	@RequestMapping("/schedule_manager_homepage")
	public String showScheduleManagerHomepage() {
		
		return "schedule_manager_homepage";
	}
	
	/**
	 * Displays the password reset page for schedule managers.
	 *
	 * @param model The {@link Model} object to pass attributes to the view.
	 * @return The name of the HTML template for password reset.
	 */
	@GetMapping("/smv-account-management")
	public String passwordReset(Model model){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		System.out.println(auth.getName());
		System.out.println(auth.getPrincipal());
		System.out.println(auth.getDetails());
		
		String managerUser = auth.getName();
		
		List<ScheduleManager> manager = scheduleManagerRepository.findByManagerUsernameContaining(managerUser);
		
		//System.out.println(CurrentManager.);
		
		model.addAttribute("manager", manager);
					
		return "smv-password-reset";
	}
	
	/**
	 * Displays the form for editing the current schedule manager's information.
	 *
	 * @param id The ID of the schedule manager to edit.
	 * @param model The {@link Model} object to pass attributes to the view.
	 * @return The name of the HTML template for editing the schedule manager.
	 */
	@GetMapping("/smv-edit-current-manager/{id}")
	public String editingCurrentUser(@PathVariable("id") long id, Model model) {
	    ScheduleManager manager = scheduleManagerRepository.findById(id)
	      .orElseThrow(() -> new IllegalArgumentException("Invalid Schedule Manager Id:" + id));
	    
	    model.addAttribute("manager", manager);
	    return "smv-edit-current-manager"; 
	}
	
	/**
	 * Handles the submission of the form for editing the current schedule manager's information.
	 *
	 * @param id The ID of the schedule manager being edited.
	 * @param manager The updated {@link ScheduleManager} object.
	 * @param result The {@link BindingResult} object to hold validation results.
	 * @param model The {@link Model} object to pass attributes to the view.
	 * @return A string indicating the view to render next.
	 */
	@Transactional
	@PostMapping("/smv-edit-current-user/{id}")
	public String saveCurrentUserEdits(@PathVariable("id") long id, @Validated ScheduleManager manager, 
	  BindingResult result, Model model, @RequestParam(value = "currentPassword", required = false) String currentPassword, @RequestParam(value = "newPassword", required = false) String newManagerPassword, 
	  @RequestParam(value="confirmPassword", required=false) String confirmManagerPassword) {
	    if (result.hasErrors()) {
	    	manager.setManagerId(id); 
	        return "smv-edit-current-manager";
	    }
	    
	    // Fetch the user (or create a new one if not found)
	    User user = userRepository.findByUsername(manager.getManagerUsername())
	            .orElse(new User());
	    
	    boolean passwordError = false;

	    // Only process passwords if new password fields are filled
	    if (newManagerPassword != null && !newManagerPassword.isEmpty()) {
	        // Verify current password
	        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
	            model.addAttribute("passwordError", "Current password is incorrect");
	            passwordError = true;
	        } 
	        // Check if new passwords match
	        else if (!newManagerPassword.equals(confirmManagerPassword)) {
	            model.addAttribute("passwordError", "New passwords do not match");
	            passwordError = true;
	        } 
	        // Process password update if there's no error
	        else {
	            String encryptedPassword = passwordEncoder.encode(newManagerPassword);
	            manager.setManagerPassword(encryptedPassword);
	            user.setPassword(encryptedPassword);
	            userRepository.save(user);  // Save the user to userRepository
	        }
	    }

	    // Save the manager regardless of password change
	    scheduleManagerRepository.save(manager);  // Save the manager

	    // If there was a password error, re-display the form
	    if (passwordError) {
	        model.addAttribute("manager", manager);
	        return "smv-edit-current-manager";
	    }

	    // If everything is fine, redirect to the confirmation page
	    return "smv-user-edit-confirmation"; 
	}

	

	/**
	 * Displays the course addition form for schedule managers.
	 *
	 * @return The name of the HTML template for adding a new course.
	 */
    @GetMapping("/add-course")
    public String showCreateCourseForm() {
        return "add-course"; 
    }

    /**
     * Displays the form for adding a new course by a schedule manager.
     *
     * @return The name of the HTML template for adding a new course.
     */
    @GetMapping("/add-courseSM")
    public String showCreateCourseFormSMV() {
        return "smv-add-course"; 
    }
    
    /**
     * Shows a list of all instructors.
     *
     * @param model The model to which the list of instructors will be added.
     * @return The name of the HTML template for displaying the instructor list.
     */
    @GetMapping("/instructor-list")
    public String showInstructorList(Model model) {
        List<Instructor> instructors = instructorRepository.findAll();
        model.addAttribute("instructors", instructors);
        return "instructor-list";
    }

    
/**
 * Displays the form for creating a new instructor.
 *
 * @return The name of the HTML template for creating a new instructor.
 */
    @GetMapping("/create-instructor")
    public String showCreateInstructorForm() {
        return "create-instructor"; 
    }
    
    
	@GetMapping("/associateSM")
	public String associateStudentWithCourseFormSMV(Model model) {
		// Retrieve the list of students and courses from the repository
		List<Student> students = studentRepository.findAll();
		List<Course> courses = courseRepository.findAll();

		// Add the lists of students and courses to the model for rendering in the HTML
		// template
		model.addAttribute("students", students);
		model.addAttribute("courses", courses);

		// Return the name of the HTML template for the form
		return "smv-associate-students";
	}
	
	/**
	 * Displays the form for associating a student with a course.
	 *
	 * @param model The model to which the list of students and courses will be added.
	 * @return The name of the HTML template for the association form.
	 */
	@PostMapping("/associateSM")
    public String handleAssociateStudentWithCourse(@RequestParam("studentId") Long studentId, @RequestParam("courseId") Long courseId) {
        System.out.println(studentId);
        if(studentId == null || courseId == null) {
            // Redirect back to the form with an error message
            return "redirect:/associateSM?error=Please select both a student and a course.";
        }

        
         Student student = studentRepository.findById(studentId).orElse(null);

         Course course = courseRepository.findById(courseId).orElse(null);;
        

        
         if(student != null && course != null) {
        	// Associate the student with the course

            student.getCourses().add(course);
            studentRepository.save(student);
            return "smv-student-association-confirmation";
        } 
        else {
			return "smv-student-association-fail";
		}

    }
	
	@GetMapping("/eligible_students")
	@ResponseBody
	public List<StudentDTO> getEligibleStudents(@RequestParam("courseId") Long courseId) {
	    Course course = courseRepository.findById(courseId).orElse(null);
	    if (course != null) {
	        return studentRepository.findAll().stream()
	                .filter(student -> !student.getCourses().contains(course))
	                .map(student -> new StudentDTO(student.getStudentId(), student.getStudentFirstName(), student.getStudentLastName()))
	                .collect(Collectors.toList());
	    }
	    return new ArrayList<>();
	}


	/**
	 * Handles the submission of the form for associating a student with a course.
	 *
	 * @param studentId The ID of the student to associate.
	 * @param courseId The ID of the course to associate.
	 * @return A redirection to the appropriate confirmation or error page.
	 */
	@GetMapping("/associate-instructorSM")
	public String associateInstructorWithCourseFormSMV(Model model) {
		// Retrieve the list of instructors and courses from the repository
		List<Instructor> instructors = instructorRepository.findAll();
		List<Course> courses = courseRepository.findAll();

		// Add the lists of instructors and courses to the model for rendering in the
		// HTML template
		model.addAttribute("instructors", instructors);
		model.addAttribute("courses", courses);

		// Return the name of the HTML template for the form
		return "smv-associate-instructor";
	}
	
	@GetMapping("/eligible_instructors")
	@ResponseBody
	public List<InstructorDTO> getEligibleInstructors(@RequestParam("courseId") Long courseId) {
	    Course course = courseRepository.findById(courseId).orElse(null);
	    if (course != null) {
	        return instructorRepository.findAll().stream()
	                .filter(instructor -> !instructor.getCourses().contains(course))
	                .map(instructor -> new InstructorDTO(instructor.getInstructorId(), instructor.getInstructorFirstName(), instructor.getInstructorLastName()))
	                .collect(Collectors.toList());
	    }
	    return new ArrayList<>();
	}

	
	/**
	 * Displays the form for associating an instructor with a course.
	 *
	 * @param model The model to which the list of instructors and courses will be added.
	 * @return The name of the HTML template for the association form.
	 */
	@PostMapping("/associate-instructorSM")
	public String handleAssociateInstructorWithCourse(@RequestParam("instructorId") Long instructorId, @RequestParam("courseId") Long courseId) {
		System.out.println(instructorId);
		 if(instructorId == null || courseId == null) {
		        // Redirect back to the form with an error message
		        return "redirect:/associate-instructorSM?error=Please select both an instructor and a course.";
		    }

        
        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);

        Course course = courseRepository.findById(courseId).orElse(null);;

       
        if(instructor != null && course != null) {

    		// Associate instructor with the course
    		//instructor = instructor.get();
    		course.setInstructor(instructor);
    		courseRepository.save(course);
           return "smv-instructor-association-confirmation";
       } 
       else {
			return "smv-instructor-association-fail";
		}
	}
    

	/**
	 * Handles the submission of the form for associating an instructor with a course.
	 *
	 * @param instructorId The ID of the instructor to associate.
	 * @param courseId The ID of the course to associate.
	 * @return A redirection to the appropriate confirmation or error page.
	 */
    @PreAuthorize("hasRole('SCHEDULE_MANAGER')")
    @GetMapping("/create-instructors")
    public String showCreateInstructorFormSMV() {
        return "smv-create-instructor"; 
    }
    
    /**
     * Shows a list of all students for a schedule manager with appropriate permissions.
     *
     * @param model The model to which the list of students will be added.
     * @return The name of the HTML template to be displayed.
     */
	@GetMapping("/list-students")
	@PreAuthorize("hasRole('SCHEDULE_MANAGER')")
	public String showStudentsListSMV(Model model) {
		// Retrieve the list of students from the repository
		List<Student> students = (List<Student>) studentRepository.findAll();

		// Add the list of students to the model for rendering in the HTML template
		model.addAttribute("students", students);

		// Return the name of the HTML template to be displayed
		return "smv-student-list";
	}
	
	@GetMapping("/smv-class-student-list")
	public String showClassStudentListSMV(Model model) {
	    // Retrieve the list of all courses from the repository
	    List<Course> courses = (List<Course>) courseRepository.findAll();

	    // Create a map to hold the course IDs and their respective student counts
	    Map<Long, Long> courseStudentCountMap = new HashMap<>();
	    for (Course course : courses) {
	        Long studentCount = Long.valueOf(course.getStudents().size()); // Get the size of the student set
	        courseStudentCountMap.put(course.getId(), studentCount); // Use course ID as key
	    }

	    // Add the courses and their counts to the model
	    model.addAttribute("courses", courses);
	    model.addAttribute("courseCounts", courseStudentCountMap);

	    // Return the name of the HTML template to be displayed
	    return "smv-class-student-list";
	}
    

	/**
	 * Shows a list of all schedule managers for a schedule manager with appropriate permissions.
	 *
	 * @param model The model to which the list of schedule managers will be added.
	 * @return The name of the HTML template to be displayed.
	 */
	@GetMapping("/smv-student-list-by-class/{id}")
	public String showStudentsListbyClassSMV(@PathVariable("id") long id, Model model) {
		
		Optional<Course> course = courseRepository.findById(id);
		
		if (course.isPresent()) {
		    Course foundCourse = course.get();
		    
		    // Retrieve the list of students based on the course ID
		    List<Student> students = studentRepository.findByCoursesId(foundCourse.getId());
		    
		    // Add the list of students to the model for rendering in the HTML template
		    model.addAttribute("students", students);
		}

		// Return the name of the HTML template to be displayed
		return "smv-student-list-by-class";
	}
    

    @GetMapping("/instructors")
    @ResponseBody
    public List<Instructor> getInstructors() {
        return instructorRepository.findAll();
    }
    
    /**
     * Shows a list of all schedule managers for a schedule manager with appropriate permissions.
     *
     * @param model The model to which the list of schedule managers will be added.
     * @return The name of the HTML template to be displayed.
     */
    @GetMapping("/all-sm")
    public String showSMSMV(Model model) {
        List<ScheduleManager> ScheduleManager = scheduleManagerRepository.findAll();
        model.addAttribute("ScheduleManager", ScheduleManager);
        return "smv-schedule-manager-list";
    }
    
    /**
     * Shows a list of all schedule managers for a schedule manager with appropriate permissions.
     *
     * @param model The model to which the list of schedule managers will be added.
     * @return The name of the HTML template to be displayed.
     */
    @GetMapping("/all")
    public String showSM(Model model) {
        List<ScheduleManager> ScheduleManager = scheduleManagerRepository.findAll();
        model.addAttribute("ScheduleManager", ScheduleManager);
        return "schedule-manager-list";
    }

    
    /**
     * Provides a form for updating a student's information.
     *
     * @param id    The ID of the student to update.
     * @param model The model to which the student object will be added.
     * @return The name of the HTML template for editing the student.
     */

    @GetMapping("/iv-edit-student/{id}")
    public String showUpdateFormIV(@PathVariable("id") long id, Model model) {
		Student student = studentRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        
        model.addAttribute("student", student);
        return "iv-edit-student";
    }

    
    /**
     * Loads the main page of the manager interface with a list of instructors.
     *
     * @param model The model to which the list of instructors will be added.
     * @return The name of the HTML template for the manager page.
     */
    @GetMapping("/page")
    public String loadManagerPage(Model model) {
        List<Instructor> instructors = instructorRepository.findAll();
        model.addAttribute("instructors", instructors);
        return "manager"; 
    }
    
   

	@GetMapping("/smv-instructor-success")
	public String showInstructorSuccessFormSMV() {
		return "smv-instructor-success";
	}
    


/**
 * Redirects to the success page after creating an instructor.
 *
 * @return The name of the success page template.
 */
    @Transactional
    @PostMapping("/add")
    public String addInstructorSMV(@ModelAttribute Instructor instructor, RedirectAttributes redirectAttributes) {
        System.out.println("Inside instructor-addSMV method");
        try {
            // Check if the instructor with the given username already exists
            if (instructorRepository.findByInstructorUsername(instructor.getInstructorUsername()).isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Instructor with given username already exists.");
                return "redirect:/schedule-manager/smv-create-instructor";
            }

            // Fetch the role with ID 3 and set it to the instructor
            Roles roles = roleRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Role with ID 3 not found"));
            List<Roles> rolesList = new ArrayList<>();
            rolesList.add(roles);
            instructor.setRoles(rolesList);

            // Save the new instructor
            instructorRepository.save(instructor);

            // Create and save the corresponding user
            User newUser = new User();
            newUser.setId(instructor.getInstructorId());
            newUser.setUsername(instructor.getInstructorUsername());
            String hashedPassword = passwordEncoder.encode(instructor.getInstructorPassword());
    	    newUser.setPassword(hashedPassword);

    	    newUser.setRoles(rolesList); 


            // Set enabled for the user as well
            newUser.setEnabled(true);

            userRepository.save(newUser);

            redirectAttributes.addFlashAttribute("successMessage", "Instructor and corresponding user added successfully.");
            return "redirect:/schedule-manager/smv-instructor-success";
        } catch (Exception e) {
            System.out.println("Failed to add instructor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add instructor.");
            return "redirect:/fail";
        }
    }
    
    /**
     * Shows the course addition success page.
     *
     * @return The name of the course success page template.
     */
    @GetMapping("smv-course-success-page")
    public String showSuccessPageSMV() {
        return "smv-course-success-page";  // This should be the name of your Thymeleaf template (without .html)
    }

    /**
     * Handles the addition of a new course.
     *
     * @param course The course object to be added.
     * @param model  The model to pass messages to the view.
     * @return A redirection to either the success page or back to the form with an error.
     */
    @PreAuthorize("hasRole('SCHEDULE_MANAGER')")
    @PostMapping("/add-courseSM")
    public String addCourseSM(@ModelAttribute Course course, Model model) {
        System.out.println("Inside addCourseSM method");  // Debugging line
        try {
            // Save the course to the database
            Course savedCourse = courseRepository.save(course);
            if (savedCourse != null) {
                model.addAttribute("message", "Course added successfully.");
                return "redirect:/schedule-manager/smv-course-success-page"; // Redirect to a success page
            } else {
                model.addAttribute("message", "Error adding course. Controller");
                return "add-courseSM"; // Stay on the same page and display the error
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error adding course. Controller Error");
            return "add-courseSM"; // Stay on the same page and display the error
        }
    }

    

    /**
     * Handles the addition of a new course.
     *
     * @param course The course object to be added.
     * @param model  The model to pass messages to the view.
     * @return A redirection to either the success page or back to the form with an error.
     */
    @PostMapping("/course/add")
    public ResponseEntity<?> addCourse(@ModelAttribute Course course){
        try {
            Course savedCourse = courseRepository.save(course);
            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Course added.\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Failed to add course.\"}");
        }
    }


     /**
      * Assigns an instructor to a course.
      *
      * @param courseId     The ID of the course.
      * @param instructorId The ID of the instructor.
      * @return A ResponseEntity indicating success or failure.
      */
    @PostMapping("/course/assign-instructor")
    public ResponseEntity<?> assignInstructorToCourse(
        @RequestParam Long courseId, 
        @RequestParam Long instructorId) {
        try {
            Course course = courseRepository.findById(courseId).orElse(null);
            Instructor instructor = instructorRepository.findById(instructorId).orElse(null);

            if (course == null || instructor == null) {
                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Invalid course or instructor ID.\"}");
            }

            course.setInstructor(instructor);
            courseRepository.save(course);

            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Instructor assigned to course.\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Failed to assign instructor.\"}");
        }
    }

    /**
     * Shows a form for updating an instructor's information.
     *
     * @param id    The ID of the instructor to update.
     * @param model The model to which the instructor object will be added.
     * @return The name of the HTML template for editing the instructor.
     */
    @GetMapping("/smv-edit-instructor/{id}")
    public String showInstructorUpdateFormSMV(@PathVariable("id") long id, Model model) {
    	Instructor instructor = instructorRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        
        model.addAttribute("instructor", instructor);
        return "smv-edit-instructor";
    }
    
    /**
     * Updates an instructor's information.
     *
     * @param id                    The ID of the instructor to update.
     * @param instructor            The updated instructor object.
     * @param result                Binding result for validation.
     * @param model                 The model to pass attributes to the view.
     * @param newInstructorPassword The new password for the instructor.
     * @param confirmInstructorPassword Confirmation of the new password.
     * @return A redirection to the appropriate confirmation or error page.
     */
    @PostMapping("/update/{id}")
    public String updateInstructorSMV(@PathVariable("id") long id, @Validated Instructor instructor, 
      BindingResult result, Model model, @RequestParam("newPassword") String newInstructorPassword, 
	  @RequestParam("confirmPassword") String confirmInstructorPassword) {
        if (result.hasErrors()) {
        	instructor.setInstructorId(id);
            return "smv-edit-instructor";
        }
        
     // checking the user to exist and creating it if it does not already exist
        User user = userRepository.findByUsername(instructor.getInstructorUsername())
                .orElse(new User());  

        // checking that both the password and the confirm password field are the same
        if (!newInstructorPassword.isEmpty() && !confirmInstructorPassword.isEmpty()) {
            if (!newInstructorPassword.equals(confirmInstructorPassword)) {
                model.addAttribute("passwordError", "Passwords do not match");
                return "smv-edit-instructor";
            }
            
            String encryptedPassword = passwordEncoder.encode(newInstructorPassword);
            instructor.setInstructorPassword(confirmInstructorPassword);
            
            // updating the users password
            user.setPassword(encryptedPassword);
        }

        // updating the users username and email to match the student
        user.setUsername(instructor.getInstructorUsername());
        user.setEmail(instructor.getInstructorEmail());
        userRepository.save(user);  // Save the user to userRepository
        
        
        // Debugging: Print the received student data
        System.out.println("Received Instructor Data:");
	    System.out.println("ID: " + instructor.getInstructorId());
	    System.out.println("First Name: " + instructor.getInstructorFirstName());
	    System.out.println("Last Name: " + instructor.getInstructorLastName());
	    System.out.println("Email: " + instructor.getInstructorEmail());
	    System.out.println("Path Variable ID: " + id);
        
//        student.setStudentId(id);
//        student.setRole(student.getRole());
//        student.setStudentPassword(student.getStudentPassword());
	    instructorRepository.save(instructor);
        return "smv-edit-instructor-confirmation";
    }


    /**
     * Deletes an instructor from the system.
     *
     * @param id    The ID of the instructor to delete.
     * @param model The model to pass attributes to the view.
     * @return A redirection to the confirmation page.
     */
    @Transactional
    @GetMapping("/instructor/delete/{id}")
	public String deleteInstructorSMV(@PathVariable("id") long id, Model model) {
	    Instructor instructor = instructorRepository.findById(id)
	      .orElseThrow(() -> new IllegalArgumentException("Invalid instructor Id:" + id));
	    
	    // Get all courses associated with the instructor
	    Set<Course> courses = instructor.getCourses();
	    
	    if (courses != null) {
	        // Iterate over the courses and set the instructor to null
	        for (Course course : courses) {
	            course.setInstructor(null);
	            // Save the course to update the association in the database
	            courseRepository.save(course);
	        }
	    }
  
     
        instructorRepository.delete(instructor);
     
     return "smv-edit-instructor-confirmation";
 }
    
    /**
     * Shows a form for creating a new student.
     *
     * @return The name of the HTML template for creating a student.
     */
	@GetMapping("/create-students")
	public String showCreateStudentFormSMV() {
		return "smv-create-student"; // This corresponds to the name of your HTML file
	}
    
	/**
	 * Creates a new student along with the corresponding user account.
	 *
	 * @param student              The student object to be created.
	 * @param redirectAttributes   Redirect attributes to pass messages.
	 * @return A redirection to either the success or error page.
	 */
    @Transactional
	@PostMapping("/create")
	public String createSMV(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
	    System.out.println("Inside student-create method");
	    try {
	        // Check if the student with the given username already exists
	        if (studentRepository.findByStudentUsername(student.getStudentUsername()).isPresent()) {
	            redirectAttributes.addFlashAttribute("errorMessage", "Student with given username already exists.");
	            return "redirect:/schedule-manager/create-students";
	        }
	        
	        // Check if a User with the provided ID already exists
	        if (userRepository.findById(student.getStudentId()).isPresent()) {
	            redirectAttributes.addFlashAttribute("errorMessage", "User with given ID already exists.");
	            return "redirect:/schedule-manager/create-students";
	        }
	        
	        // Create and save the corresponding User
	        User newUser = new User();
	        newUser.setId(student.getStudentId()); 
	        newUser.setUsername(student.getStudentUsername());
	        newUser.setFirstName(student.getStudentFirstName()); 
	        newUser.setLastName(student.getStudentLastName()); 
	        newUser.setEmail(student.getStudentEmail()); 
	        String hashedPassword = passwordEncoder.encode(student.getStudentPassword());
	        newUser.setPassword(hashedPassword);
	        newUser.setEnabled(true);

	        // Fetch the role with ID 2 and set it to the user
	        Roles roles = roleRepository.findById(2L)
	            .orElseThrow(() -> new RuntimeException("Role with ID 2 not found"));
	        newUser.setRoles(Collections.singletonList(roles));
            
            
	     // Save the User to the database
	        newUser = userRepository.save(newUser);
	        
	        student.setStudentPassword(hashedPassword);

	        // Assign the User to the Student and save the Student
	        student.setUser(newUser);
	        studentRepository.save(student);

	        redirectAttributes.addFlashAttribute("successMessage", "Student and corresponding user added successfully.");
	        return "smv-upload-success";
	    } catch (Exception e) {
	        System.out.println("Failed to add student: " + e.getMessage());
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to add student.");
	        return "redirect:/fail";
	    }
	}
    
    /**
     * Shows the form for editing a student's information.
     *
     * @param id    the ID of the student to edit
     * @param model the Spring MVC model
     * @return the name of the template to show
     */
	@GetMapping("/smv-edit-student/{id}")
    public String showUpdateFormSMV(@PathVariable("id") long id, Model model) {
		Student student = studentRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        
        model.addAttribute("student", student);
        return "smv-edit-student";
    }

	/**
	 * Processes the form for updating a student's information.
	 *
	 * @param id the ID of the student to update
	 * @param student the updated student information
	 * @param result the binding result for error handling
	 * @param model the Spring MVC model
	 * @param newPassword the new password for the student
	 * @param confirmPassword the confirmation of the new password
	 * @return the name of the template to show
	 */
	@PostMapping("/smv-update/{id}")
    public String updateStudentSMV(@PathVariable("id") long id, @Validated Student student, 
      BindingResult result, Model model, @RequestParam("newPassword") String newStudentPassword, 
	  @RequestParam("confirmPassword") String confirmStudentPassword) {
        if (result.hasErrors()) {
            student.setStudentId(id);
            return "smv-update-user";
        }
        
        Student Updatestudent = studentRepository.findByStudentUsername(student.getStudentUsername()).orElse(null);
        
        student.setStudentPassword(Updatestudent.getStudentPassword());
        student.setUser(Updatestudent.getUser());
        student.setCourses(Updatestudent.getCourses());
        
        Updatestudent = student;
        
     // checking the user to exist and creating it if it does not already exist
        User user = userRepository.findByUsername(Updatestudent.getStudentUsername())
                .orElse(new User());  

        // checking that both the password and the confirm password field are the same
        if (!newStudentPassword.isEmpty() && !confirmStudentPassword.isEmpty()) {
            if (!newStudentPassword.equals(confirmStudentPassword)) {
                model.addAttribute("passwordError", "Passwords do not match");
                return "smv-edit-student";
            }
            
            String encryptedPassword = passwordEncoder.encode(newStudentPassword);
            Updatestudent.setStudentPassword(encryptedPassword);
            
            // updating the users password
            user.setPassword(encryptedPassword);
        }

        // updating the users username and email to match the student
        user.setFirstName(Updatestudent.getStudentFirstName());
        user.setLastName(Updatestudent.getStudentLastName());
        user.setUsername(Updatestudent.getStudentUsername());
        user.setEmail(Updatestudent.getStudentEmail());
        userRepository.save(user);  // Save the user to userRepository

        
        // Debugging: Print the received student data
        System.out.println("Received Student Data:");
        System.out.println("ID: " + Updatestudent.getStudentId());
        System.out.println("First Name: " + Updatestudent.getStudentFirstName());
        System.out.println("Last Name: " + Updatestudent.getStudentLastName());
        System.out.println("Email: " + Updatestudent.getStudentEmail());
        System.out.println("Path Variable ID: " + Updatestudent.getStudentId());
        
        
        studentRepository.save(Updatestudent);
        return "smv-edit-confirmation";
    }
	
	/**
	 * Deletes a student.
	 *
	 * @param id    the ID of the student to delete
	 * @param model the Spring MVC model
	 * @return the name of the template to show after deletion
	 */
	@GetMapping("/student/delete/{id}")
    public String deleteStudentSMV(@PathVariable("id") long id, Model model) {
        Student student = studentRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        
        //you will need to also remove the student from their courses or its going to delete all the students associated with that course
        
       	// remove the student from the courses

           System.out.println(student.getCourses());
           student.getCourses().clear();
           studentRepository.save(student);
           //return "smv-edit-confirmation";
     
        
           studentRepository.delete(student);
        
        return "smv-edit-confirmation";
    }
	
	/**
	 * Shows the form for importing students from an Excel file.
	 *
	 * @return the name of the template to show
	 */
	@GetMapping("/exportStudentSMV")
	public ResponseEntity<String> exportStudentDataSMV() {
	    try {
	        // Fetches the list of students from the repository
	        List<Student> students = (List<Student>) studentRepository.findAll(); 

	        if (students != null && !students.isEmpty()) {
	            // Get the user's downloads folder
	            String userHome = System.getProperty("user.home");
	            String downloadsDirectory = userHome + File.separator + "Downloads";
	            
	            // Define the file path in the downloads folder
	            String filePath = downloadsDirectory + File.separator + "student_exported_data.xlsx";
	            
	            // Check if the file already exists
	            File file = new File(filePath);
	            boolean fileExists = file.exists();
	            
	            excelExportService.exportStudentData(students, filePath, fileExists);
	            
	            if (fileExists) {
	                return ResponseEntity.ok("Student data updated successfully!");
	            } else {
	                return ResponseEntity.ok("Student data exported successfully!");
	            }
	        } else {
	            return ResponseEntity.ok("No students found to export!");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to export student data!");
	    }
	}
	
	/**
	 * Shows the success page after a successful upload.
	 *
	 * @return the name of the template to show
	 */
	@GetMapping("/uploadExcelSMV")
	public String importStudentsSMV() {
		return "smv-import"; // This corresponds to the name of your HTML file
	}
	/**
	 * Shows the success page if the upload does succeed.
	 *
	 * @return the name of the template to show
	 */
	@GetMapping("/smv-upload-success")
	public String uploadSuccessSMV() {
		return "smv-upload-success"; // This corresponds to the name of your HTML file
	}
	
	/**
	 * Handles the upload of an Excel file containing student data.
	 *
	 * @param file the Excel file to upload
	 * @return the name of the template to show after processing the file
	 */
	@Transactional
	@PostMapping("/uploadExcelSMV")
	public String uploadExcelSMV(@RequestParam("file") MultipartFile file) {
	    if (file.isEmpty()) {
	        // Handle empty file error
	        return "redirect:/schedule-manager/smv-import?error=emptyfile";
	    }

	    try (InputStream inputStream = file.getInputStream()) {
	        Workbook workbook = WorkbookFactory.create(inputStream);
	        Sheet sheet = workbook.getSheetAt(0); // Assuming the data is on the first sheet

	        // Fetch the role with ID 2 once
	        Roles role = roleRepository.findById(2L)
	            .orElseThrow(() -> new RuntimeException("Role with ID 2 not found"));
	        List<Roles> rolesList = new ArrayList<>();

	        // Iterate through rows and columns to extract data
	        for (Row row : sheet) {
	            if (row.getRowNum() == 0) {
	                // Skip the header row
	                continue;
	            }

                Student student = new Student();


                // Handle each cell in the row
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                // Cell contains a string value
                                switch (i) {
                                    case 1:
                                        student.setStudentFirstName(cell.getStringCellValue());
                                        break;
                                    case 2:
                                        student.setStudentLastName(cell.getStringCellValue());
                                        break;
                                    case 3:
                                        student.setStudentEmail(cell.getStringCellValue());
                                        break;
                                    case 4:
                                        student.setStudentPassword(cell.getStringCellValue());
                                        break;
                                    case 5:
                                        student.setStudentUsername(cell.getStringCellValue());
                                        break;
                                    // Add cases for other cells as needed
                                }
                                break;
                            case NUMERIC:
                                // Cell contains a numeric value
                                double numericValue = cell.getNumericCellValue();
                                switch (i) {
                                    case 0:
                                        student.setStudentId((long) numericValue);
                                        System.out.println("Assigned Student ID: " + student.getStudentId());  // For debugging purposes

                                        break;
                                    case 6:
                                        // Assuming column 6 contains numeric value for Credits Taken
                                        student.setCreditsTaken((float) numericValue);
                                        break;
                                    // Handle numeric value for other cells if needed
                                }
                                break;
                        }
              
                    }
                }
                
                // Fetch the role with ID 2 and set it to the student
                if (student.getStudentId() == null) {
                    System.out.println("Skipped a row due to missing Student ID.");
                    continue;
                }

                student.setRoles(rolesList);

                Optional<Student> existingStudent = studentRepository.findById(student.getStudentId());
                if (!existingStudent.isPresent()) {
                    // Saving the student to the database since it doesn't exist
                    studentRepository.save(student);
                    User newUser = new User();
                    newUser.setId(student.getStudentId());
                    newUser.setUsername(student.getStudentUsername());
                    
                    // Encode the password before saving
                    String encodedPassword = passwordEncoder.encode(student.getStudentPassword());
                    newUser.setPassword(encodedPassword);
                    
                    newUser.setRoles(rolesList); 
                    newUser.setEnabled(true);
                    userRepository.save(newUser);
                } else {
                    // Handle the case when the student already exists, if needed
                }
            }

            workbook.close();  // Close the workbook
            return "redirect:/schedule-manager/smv-upload-success";

        } catch (IOException e) {
            e.printStackTrace();
            // Handle file processing error
            return "redirect:/instructor/smv-import?error=processing";
        }
    }
	
	/**
	 * Shows the failure page if the upload does not succeed.
	 *
	 * @return the name of the template to show
	 */
	@GetMapping("/smv-upload-fail")
	public String showSmvUploadFail() {
		return "smv-upload-fail"; // This corresponds to the name of your HTML file
	}
	
	/**
	 * Shows the form for importing class data from an Excel file.
	 *
	 * @return the name of the template to show
	 */
	@GetMapping("/smv-class-import")
	public String showSmvClassImport() {
		return "smv-class-import"; // This corresponds to the name of your HTML file
	}
	
	/**
	 * Handles the upload of an Excel file containing class and instructor data.
	 *
	 * @param file the Excel file to upload
	 * @return the name of the template to show after processing the file
	 */
	@PostMapping("/smv-upload")
	public String upload(@RequestParam("file") MultipartFile file) throws IOException {

		boolean instructorCreated = false;

		if (file.isEmpty()) {

			return "redirect:/schedule-manager/smv-import?error=emptyfile";
		}

		InputStream is = file.getInputStream();
		Workbook workbook = WorkbookFactory.create(is);

		Sheet sheet = workbook.getSheetAt(0);
		String courseNameFull = sheet.getRow(4).getCell(0).getStringCellValue();
		String instructorNameFull = sheet.getRow(2).getCell(0).getStringCellValue();
		String instructorEmailFull = sheet.getRow(1).getCell(0).getStringCellValue();
		String instructorIdString = sheet.getRow(3).getCell(0).getStringCellValue();
		String courseIdString = sheet.getRow(5).getCell(0).getStringCellValue();

		System.out.println(instructorIdString);
		System.out.println(instructorNameFull);
		System.out.println(courseNameFull);
		System.out.println(courseIdString);

		// converting courseId
		String courseIdShort = removeBeforeColon(courseIdString);
		long courseId = Long.parseLong(courseIdShort);

		System.out.println(courseId);
		String courseName = removeBeforeColon(courseNameFull);
		System.out.println(courseName);
		
		
		
		Course course = new Course();
		course.setId(courseId);
		course.setCourseName(courseName);
		courseRepository.save(course);

		
		
		String parsedInstructorName = removeBeforeColon(instructorNameFull);
		String[] instructorsNames = extractNames(parsedInstructorName);
		String InsFirstName = instructorsNames[1];
		String InsLastName = instructorsNames[0];

		// converting instructorId
		String instructorIdshort = removeBeforeColon(instructorIdString);
		long instructorId = Long.parseLong(instructorIdshort);

		// getting email for instructor
		String instructorEmail = removeBeforeColon(instructorEmailFull);
		String instructorUsername = parseEmail(instructorEmail);

		System.out.println(instructorId);
		System.out.println(InsFirstName);
		System.out.println(InsLastName);

		Instructor instructor = new Instructor();

		Optional<Instructor> existingInstructor = instructorRepository.findByInstructorUsername(instructorUsername);
		if (!existingInstructor.isPresent()) {

			instructor.setInstructorId(instructorId);
			instructor.setInstructorFirstName(InsFirstName);
			instructor.setInstructorLastName(InsLastName);
			instructor.setInstructorEmail(instructorEmail);
			instructor.setInstructorUsername(instructorUsername);
			String hashedPassword = passwordEncoder.encode("instructor");
			instructor.setInstructorPassword(hashedPassword);

			instructorRepository.save(instructor);

			instructorCreated = true;

			Roles roles = roleRepository.findById(3L)
					.orElseThrow(() -> new RuntimeException("Role with ID 4 not found"));
			List<Roles> rolesList = new ArrayList<>();
			rolesList.add(roles);
			instructor.setRoles(rolesList);

			Optional<User> existingUser = userRepository.findByUsername(instructor.getInstructorUsername());
			if (!existingUser.isPresent()) {
				User user = new User();
				user.setEmail(instructor.getInstructorEmail());
				user.setFirstName(instructor.getInstructorFirstName());
				user.setLastName(instructor.getInstructorLastName());
				user.setUsername(instructor.getInstructorUsername());
				user.setPassword(hashedPassword);
				user.setEnabled(true);
				rolesList.add(roles);
				user.setRoles(rolesList);
				userRepository.save(user);
			} else {
				System.out.println("Console LOG: User Id is already present in the database");
			}

			course.setInstructor(instructor);
			courseRepository.save(course);

		} else {
			// Associate instructor with the course
			instructor = existingInstructor.get();
			course.setInstructor(instructor);
			courseRepository.save(course);
		}

		// this section adds the students id's, names, emails, generates usernames and
		// default password

		try {
			for (int i = 8; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) { // null check
					System.out.println("Encountered null row. Stopping processing.");
					break; // This will exit the loop when a null row is encountered.
				}

				Student student = new Student();
				String StuFirstName;
				String StuLastName;
				long StudentID;
				String studentEmail;

				if (row.getCell(0) != null) {
					StudentID = (long) row.getCell(0).getNumericCellValue();
					System.out.println(StudentID);
					student.setStudentId(StudentID);
				}

				if (row.getCell(1) != null) {
					String StudentName = row.getCell(1).getStringCellValue();
					String[] StudentNames = extractNames(StudentName);
					StuFirstName = StudentNames[1];
					StuLastName = StudentNames[0];
					student.setStudentFirstName(StuFirstName);
					student.setStudentLastName(StuLastName);
				}
				if (row.getCell(2) != null) {
					studentEmail = row.getCell(2).getStringCellValue();
					System.out.println(studentEmail);
					student.setStudentEmail(studentEmail);
					String username = parseEmail(studentEmail);
					student.setStudentUsername(username);
					System.out.println(username);
				}

				Roles roles = roleRepository.findById(2L)
						.orElseThrow(() -> new RuntimeException("Role with ID 4 not found"));
				List<Roles> rolesList = new ArrayList<>();
				rolesList.add(roles);
				student.setRoles(rolesList);

				student.setStudentPassword("student" + student.getStudentId());
				String hashedPassword = passwordEncoder.encode(student.getStudentPassword());
				student.setStudentPassword(hashedPassword);

				// Check if the student already exists in the database
				Optional<Student> existingStudent = studentRepository.findByStudentUsername(student.getStudentUsername());
				if (!existingStudent.isPresent()) {
					// Student does not exist, save it
					studentRepository.save(student);

					// Associate the student with the course
					student.getCourses().add(course);
					studentRepository.save(student);
				} else if(existingStudent.isPresent()){
					// create an ArrayList to store course IDs
				    List<Long> courseIds = new ArrayList<>();
				    
				    Student studentUpdate = studentRepository.findByStudentUsername(student.getStudentUsername()).orElse(null);
				    
					Set<Course> studentCourses = studentUpdate.getCourses();
				    for (Course courseCheck : studentCourses) {
				        Long courseIdCheck = courseCheck.getId(); 
				        courseIds.add(courseIdCheck);
				    }

				    // retrieve the Course entities based on the extracted course IDs
				    //List<Course> courses = courseRepository.findAllById(courseIds);
				    
				    System.out.println(courseIds);
				    
				    for(int j = 0; j < courseIds.size(); j++) {
				    	if (!courseIds.contains(course.getId())) {
				          
				    		studentUpdate.getCourses().add(course);
							studentRepository.save(studentUpdate);
				        }
				    }
				    
					
					System.out.println("Console LOG: Student Id is already present in the database");
				}

				Optional<User> existingUser = userRepository.findByUsername(student.getStudentUsername());
				if (!existingUser.isPresent()) {
					User user = new User();
					user.setEmail(student.getStudentEmail());
					user.setFirstName(student.getStudentFirstName());
					user.setLastName(student.getStudentLastName());
					user.setUsername(student.getStudentUsername());
					user.setPassword(hashedPassword);
					user.setEnabled(true);
					rolesList.add(roles);
					user.setRoles(rolesList);
					userRepository.save(user);
				    student.setUser(user);  // setting the association of the student to user 
				    studentRepository.save(student); // saving that association 
				} else {
					System.out.println("Console LOG: User Id is already present in the database");
				}

			}

			if (instructorCreated != true) {
				return "redirect:/schedule-manager/smv-upload-success";
			} else {
				return "redirect:/schedule-manager/smv-upload-fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/error";
		}
	}
	    
	    public static String removeBeforeColon(String input) {
	        int colonIndex = input.indexOf(":");
	        if (colonIndex != -1) {
	            return input.substring(colonIndex + 1).trim();
	        } else {
	            // Return the original string if there is no colon in it.
	            return input;
	        }
	    }
	    
	    public static String[] extractNames(String fullName) {
	        // Split the full name using a comma and space as the delimiter
	        String[] names = fullName.split(",\\s+");
	        if (names.length == 2) {
	            return names; // Assuming the input format is correct (Last Name, First Name)
	        } else {
	            // Handle incorrect input format or provide default values as needed
	            return new String[]{"null", "null"};
	        }
	    }
	    
	    public static String parseEmail(String email) {
	        int atIndex = email.indexOf("@");
	        if (atIndex != -1) {
	            return email.substring(0, atIndex).trim();
	        } else {
	            // If there is no "@" symbol, return the original email.
	            return email;
	        }
	    }
	    
	    /**
	     * Deletes a class.
	     *
	     * @param id    the ID of the class to delete
	     * @param model the Spring MVC model
	     * @return the name of the template to show after deletion
	     */
		@GetMapping("/delete/{id}")
		public String deleteClassSMV(@PathVariable("id") long id, Model model) {
		    Course course = courseRepository.findById(id)
		      .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

		    Set<Student> students = course.getStudents();
		    
		    for (Student student : students) {
		        student.getCourses().remove(course); 
		    }

		    course.getStudents().clear();

		    courseRepository.delete(course);
		    return "smv-edit-class-confirmation";
		}
		
		/**
		 * Lists all classes in the system.
		 *
		 * @param model the Spring MVC model
		 * @return the name of the template to show
		 */
		@GetMapping("/smv-class-list")
		public String showClassListSMV(Model model) {
		    // Retrieve the list of all courses from the repository
		    List<Course> courses = (List<Course>) courseRepository.findAll();

		    // Create a map to hold the course IDs and their respective student counts
		    Map<Long, Long> courseStudentCountMap = new HashMap<>();
		    for (Course course : courses) {
		        Long studentCount = Long.valueOf(course.getStudents().size()); // Get the size of the student set
		        courseStudentCountMap.put(course.getId(), studentCount); // Use course ID as key
		    }

		    // Add the courses and their counts to the model
		    model.addAttribute("courses", courses);
		    model.addAttribute("courseCounts", courseStudentCountMap);

		    // Return the name of the HTML template to be displayed
		    return "smv-class-list";
		}		
		
		@GetMapping("/smv-instructor-courses/{instructorId}")
		public String showInstructorCourses(@PathVariable Long instructorId, Model model) {
		    Instructor instructor = instructorRepository.findById(instructorId).orElse(null);
		    Set<Course> courses = new HashSet<>();

		    if (instructor != null) {
		        courses = instructor.getCourses();
		    }
		    // Create a map to hold the course IDs and their respective student counts
		    Map<Long, Long> courseStudentCountMap = new HashMap<>();
		    for (Course course : courses) {
		        Long studentCount = Long.valueOf(course.getStudents().size()); // Get the size of the student set
		        courseStudentCountMap.put(course.getId(), studentCount); // Use course ID as key
		    }

		    model.addAttribute("instructor", instructor);
		    model.addAttribute("courses", courses);
		    model.addAttribute("courseCounts", courseStudentCountMap);


		    return "smv-instructor-courses";
		}

		
		/**
		 * Shows the form for editing class information.
		 *
		 * @param id    the ID of the class to edit
		 * @param model the Spring MVC model
		 * @return the name of the template to show
		 */
		@GetMapping("/smv-edit-class/{id}")
		public String showUpdateClassSMV(@PathVariable("id") long id, Model model) {
		    Course course = courseRepository.findById(id)
		      .orElseThrow(() -> new IllegalArgumentException("Invalid Course Id:" + id));
		    
		    model.addAttribute("course", course);
		    return "smv-edit-class"; 
		}
		
		/**
		 * Processes the form for updating a class's information.
		 *
		 * @param id     the ID of the class to update
		 * @param course the updated course information
		 * @param result the binding result for error handling
		 * @param model  the Spring MVC model
		 * @return the name of the template to show after update
		 */
		@PostMapping("/smv-edit-class/{id}")
	    public String updateClassSMV(@PathVariable("id") long id, @Validated Course updatedCourse, 
	      BindingResult result, Model model) {
	        if (result.hasErrors()) {
	        	updatedCourse.setId(id);
	            return "smv-edit-class";
	        }
	        
	        Course existingCourse = courseRepository.findById(id)
	        	      .orElseThrow(() -> new IllegalArgumentException("Invalid Course Id:" + id));

	        	    // Maintain the existing instructor
	        	    if (existingCourse.getInstructor() != null) {
	        	        updatedCourse.setInstructor(existingCourse.getInstructor());
	        	    }
	        	    
	        // Debugging: Print the received student data
	        System.out.println("Received Class Data:");
	        System.out.println("ID: " + updatedCourse.getId());
	        System.out.println("Course Name: " + updatedCourse.getCourseName());
	        System.out.println("Path Variable ID: " + id);
	        
	        courseRepository.save(updatedCourse);
	        return "smv-edit-class-confirmation";
	    }
		

}
