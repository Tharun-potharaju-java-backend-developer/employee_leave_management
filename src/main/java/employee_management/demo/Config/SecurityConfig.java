package employee_management.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import employee_management.demo.Security.JwtAuthenticationFilter;

/*
 * @Configuration tells Spring that this class contains
 * application configuration (in this case, Spring Security configuration).
 */
@Configuration
public class SecurityConfig {

    /*
     * @Bean tells Spring to create and manage this object
     * in the Spring IoC Container.
     *
     * SecurityFilterChain defines the security rules
     * that every incoming HTTP request must follow.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http

                /*
                 * CSRF (Cross Site Request Forgery) protection is enabled by default.
                 *
                 * For REST APIs using JWT or Token Authentication,
                 * CSRF protection is usually disabled because the API
                 * does not rely on browser sessions.
                 */
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())

                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny()))

                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)

                /*
                 * Configure authorization rules.
                 *
                 * Authorization decides:
                 * "Which users can access which URLs?"
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * requestMatchers() specifies URL patterns.
                         *
                         * The following URLs can be accessed
                         * without authentication.
                         */
                        .requestMatchers(

                                // Responsive frontend assets
                                "/", "/index.html", "/app.css", "/app.js", "/favicon.ico",

                                // Swagger API documentation
                                "/v3/api-docs/**",

                                // Swagger UI resources
                                "/swagger-ui/**",

                                // Old Swagger UI URL
                                "/swagger-ui.html",

                                // All endpoints starting with /users/
                                // Example:
                                // /users/register
                                // /users/login
                                // /users/forgot-password
                                "/api/v1/auth/**",
                                "/api/v1/users",
                                "/api/v1/employee/**")

                        /*
                         * permitAll() means
                         * anyone can access the above URLs,
                         * even if they are not logged in.
                         */
                        .permitAll()

                        /*
                         * anyRequest() matches every remaining URL.
                         *
                         * authenticated() means the user must
                         * be authenticated (logged in)
                         * before accessing those endpoints.
                         *
                         * Example:
                         * /employees
                         * /attendance
                         * /salary
                         * /admin
                         *
                         * These URLs require authentication.
                         */
                        .anyRequest().authenticated());

        /*
         * build() creates the SecurityFilterChain object
         * using all the rules configured above.
         *
         * Spring Security uses this filter chain
         * to secure every incoming request.
         */
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
