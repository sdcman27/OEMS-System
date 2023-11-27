package edu.sru.thangiah.controller;

import edu.sru.thangiah.domain.Administrator;
import edu.sru.thangiah.domain.Instructor;
import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.model.Roles;
import edu.sru.thangiah.model.User;
import edu.sru.thangiah.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The {@code MenuController} class manages various navigation-related functionalities in the application.
 * It is responsible for displaying the main navigation bar, the sidebar, and handling the exit functionality.
 * The class interacts with various repositories to access and manage user and role data.
 * <p>
 * It uses the {@code UserRepository}, {@code RoleRepository}, {@code AdministratorRepository},
 * {@code StudentRepository}, {@code InstructorRepository}, and a {@code PasswordEncoder} for handling user data
 * and passwords, currently not in use but it would be nice to organize the methods.
 */

@Controller
public class MenuController {
	


	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private RoleRepository roleRepository;

	    @Autowired
	    private AdministratorRepository administratorRepository;

	    @Autowired
	    private StudentRepository studentRepository;

	    @Autowired
	    private InstructorRepository instructorRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;


		/*
		 * @GetMapping("/register") public String register(Model model) {
		 * model.addAttribute("user", new User()); return "register"; }
		 * 
		 * 
		 * @PostMapping("/register") public String saveUser(User user, @RequestParam
		 * String role) { user.setPassword(passwordEncoder.encode(user.getPassword()));
		 * Roles userRole = roleRepository.findByName(role) .orElseThrow(() -> new
		 * RuntimeException("Error: Role is not found.")); user.setRole(userRole);
		 * userRepository.save(user);
		 * 
		 * switch (role) { case "ADMINISTRATOR": Administrator admin = new
		 * Administrator(); admin.setUser(user); administratorRepository.save(admin);
		 * break; case "STUDENT": Student student = new Student();
		 * student.setUser(user); studentRepository.save(student); break; case
		 * "INSTRUCTOR": Instructor instructor = new Instructor();
		 * instructor.setUser(user); instructorRepository.save(instructor); break; //
		 * Add Scedule manager when merged }
		 * 
		 * return "redirect:/index"; // Redirect to login page after successful
		 * registration }
		 */
	

	    /**
	     * Displays the main navigation bar of the application.
	     * 
	     * @return The view name of the navigation bar template.
	     */
	@RequestMapping("/navbar")
    public String showMainScreen() {
        return "navbar"; 
    }
	
	
	/**
	 * Displays the sidebar of the application. Logs a console message upon receiving the sidebar request.
	 * 
	 * @return The view name of the sidebar template.
	 */
	@RequestMapping("/sidebar")
    public String showSidebar() {
	    System.out.println("Sidebar request received");
        return "sidebar"; 
    }
	
	/**
	 * Handles the exit functionality of the application (/logout is now used over this method).
	 * 
	 * @return Redirects to the index (home) page of the application.
	 */
	@RequestMapping("/exit")
    public String exitProgram() {
        return "index"; 
    }


    
}