package edu.sru.thangiah.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * Security configuration class for Spring Security integration.
 * <p>
 * This class uses annotations to define beans and configure the security context for the application.
 * It extends WebSecurityConfigurerAdapter to provide custom security configurations.
 * By annotating with {@code @EnableWebSecurity}, it gains the ability to contribute to the web security
 * configuration using {@code HttpSecurity} instances.
 * </p>
 * <p>
 * The configuration specifies custom user details service for authentication,
 * password encoding scheme, and success handler for post-authentication actions.
 * It also configures CSRF protection settings and the authorization requests for different URL patterns.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    /**
     * Configures the {@link PasswordEncoder} bean that will be used to encode passwords.
     * The method creates a new instance of {@link BCryptPasswordEncoder}.
     *
     * @return an instance of {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the {@link HttpSecurity} instance to specify the security features.
     * <p>
     * It sets up CSRF protection, configures authorization requests for specific URL patterns,
     * form-based authentication and logout behavior.
     * </p>
     *
     * @param http the {@link HttpSecurity} to modify
     * @return the configured {@link DefaultSecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    protected DefaultSecurityFilterChain configure(HttpSecurity http) throws Exception {
        System.out.println("SecurityConfig is loaded");

        http
                //Disabling CSRF is not recommended for production but it is making the whole program very angry.
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                				.requestMatchers("/student_homepage/**").hasAuthority("STUDENT")
                                .anyRequest().authenticated()
                )
                .formLogin(login -> login
                                .successHandler(successHandler) // the second parameter ensures always redirecting to "/navbar" after login
                                .permitAll()
                )
                .logout(logout -> logout // Configure logout
                                .logoutUrl("/logout") // Set the URL for logout
                                .logoutSuccessUrl("/login?logout") // Redirect to this URL after successful logout
                );
        return http.build();
    }


    /**
     * Configures the global authentication manager with user details and password encoder.
     * This configuration is necessary to ensure the authentication manager can authenticate users based on
     * the custom {@code UserDetailsService} and the specified {@code PasswordEncoder}.
     *
     * @param auth the {@link AuthenticationManagerBuilder} to use
     * @throws Exception if an error occurs while configuring the authentication manager
     */
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}