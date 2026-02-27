package com.smarthire.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
        	.cors(cors -> {})
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                		"/api/users/register", 
                		"/api/users/login",
                		"/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html").permitAll()
                // Company APIs
                // Only RECRUITER can create
                	.requestMatchers(HttpMethod.POST,"/api/company/**").hasRole("RECRUITER")
                // RECRUITER or ADMIN can delete
                	.requestMatchers(HttpMethod.DELETE,"/api/company/**").hasAnyRole("RECRUITER", "ADMIN")
                // GET company - any logged in user
                	.requestMatchers(HttpMethod.GET,"/api/company/**").authenticated()
     
                    
                 // 🔹 NEW: Profile APIs
                    .requestMatchers(HttpMethod.PUT,"/api/users/update-profile/**","/api/users/change-password/**").authenticated()
                        
                 // 🔹 Job APIs
                     .requestMatchers(HttpMethod.POST,"/api/jobs/**").hasRole("RECRUITER")
                     .requestMatchers(HttpMethod.PUT,"/api/jobs/**").hasRole("RECRUITER")
                     .requestMatchers(HttpMethod.GET,"/api/jobs/**").authenticated()
                     
                  // 🔹 Application APIs
                     .requestMatchers(HttpMethod.POST,"/api/applications/apply").hasRole("JOBSEEKER")
                     .requestMatchers(HttpMethod.GET, "/api/applications/job/**").hasRole("RECRUITER")
                     .requestMatchers(HttpMethod.GET, "/api/applications/my").hasRole("JOBSEEKER")
                     .requestMatchers(HttpMethod.PUT, "/api/applications/status/**").hasRole("RECRUITER")
 
                 // Everything else
                     .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {

        org.springframework.web.cors.CorsConfiguration configuration =
                new org.springframework.web.cors.CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
