package za.ac.cput.marginhotelmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
   Just get a working Spring Security filter chain in place.
   */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationConfig jwtAuthenticationConfig;

    public SecurityConfig(JwtAuthenticationConfig jwtAuthenticationConfig) {
        this.jwtAuthenticationConfig = jwtAuthenticationConfig;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/payment/**").permitAll() // TEMP: remove before committing
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/room/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/room/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/room/**").hasAnyRole("ADMIN")
                        .requestMatchers("/staff/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/booking/create").authenticated()
                        .requestMatchers(
                                "/booking/**",
                                "/guest/**",
                                "/invoice/**").hasAnyRole("RECEPTIONIST", "ADMIN") //Payment is missing.
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationConfig, UsernamePasswordAuthenticationFilter.class)
                ;
        return http.build();
    }
}

