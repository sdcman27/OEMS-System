package edu.sru.thangiah.config;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import edu.sru.thangiah.model.Roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Custom authentication success handler for managing redirection after successful authentication
 * based on user roles and certain conditions like default passwords.
 * <p>
 * This class extends {@link SimpleUrlAuthenticationSuccessHandler} to override the
 * {@code onAuthenticationSuccess} method. It determines the role of the authenticated user
 * and performs redirection to different URLs based on their role and whether they are using
 * a default password.
 * </p>
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	/**
     * Handles the redirection logic after successful authentication.
     * <p>
     * If the user has a default password, they are redirected to change it. Otherwise, they are
     * redirected to the homepage corresponding to their role. It uses the {@code HttpSession} to pass
     * messages between redirects.
     * </p>
     *
     * @param request The request during which the authentication attempt occurred.
     * @param response The response.
     * @param authentication The authentication object which was created during the authentication process.
     * @throws IOException On input/output exceptions during redirection.
     */
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        
        // Get the entered password directly from the request
        String enteredPassword = request.getParameter("password");

        // Get the role of the logged in user
        String userRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        // Create HttpSession object
        HttpSession session = request.getSession();

        // Check for default passwords and redirect accordingly
        if ("instructor".equals(enteredPassword) && "INSTRUCTOR".equals(userRole)) {
        	 System.out.println("Redirecting instructor with default password"); // Logging for debugging
             session.setAttribute("changePassword", "Please change your password from a default password before continuing.");
             response.sendRedirect("/instructor/iv-account-management");
            return;
        } else if ("student".equals(enteredPassword) && "STUDENT".equals(userRole)) {
        	 System.out.println("Redirecting student with default password"); // Logging for debugging
             session.setAttribute("changePassword", "Please change your password from a default password before continuing.");
             response.sendRedirect("/student/course/sv-account-management");
        }

        // Redirect user based on their role
        switch (userRole) {
            case "STUDENT":
                this.setDefaultTargetUrl("/student/course/student_homepage");
                break;
            case "INSTRUCTOR":
                this.setDefaultTargetUrl("/instructor/instructor_homepage");
                break;
            case "ADMINISTRATOR":
                this.setDefaultTargetUrl("/admin_homepage");
                break;
            case "SCHEDULE_MANAGER":
                this.setDefaultTargetUrl("/schedule-manager/schedule_manager_homepage");
                break;
            default:
                this.setDefaultTargetUrl("/index"); 
        }

        try {
			super.onAuthenticationSuccess(request, response, authentication);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
    }
}
