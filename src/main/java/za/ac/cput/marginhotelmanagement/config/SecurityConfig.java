package za.ac.cput.marginhotelmanagement.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/room/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/room/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/room/**").hasAnyRole("ADMIN")
                        .requestMatchers("/staff/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/guest/create").hasAnyRole("USER", "RECEPTIONIST", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/guest/findByEmail/**").hasAnyRole("USER", "RECEPTIONIST", "ADMIN")
                        .requestMatchers("/guest/**").hasAnyRole("RECEPTIONIST", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/booking/create").hasAnyRole("USER", "RECEPTIONIST", "ADMIN")
                        .requestMatchers("/booking/**").hasAnyRole("RECEPTIONIST", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/invoice/create").hasAnyRole("USER", "RECEPTIONIST", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/invoice/read/**").hasAnyRole("USER", "RECEPTIONIST", "ADMIN")
                        .requestMatchers("/invoice/**").hasAnyRole("RECEPTIONIST", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/payment/checkout").hasRole("USER")
                        .requestMatchers(
                                "/payment/**").hasAnyRole("RECEPTIONIST", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationConfig, UsernamePasswordAuthenticationFilter.class)
                ;
        return http.build();
    }
}
