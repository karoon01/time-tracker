package com.yosypchuk.tracker.configuration.security;

import com.yosypchuk.tracker.configuration.security.jwt.AuthTokenFilter;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AuthTokenFilter jwtTokenFilter;

    private static final String[] USER_PATHS = {
            "/api/v1/activity/{id}",
            "/api/v1/activity/all",
            "/api/v1/activity/category/{name}",
            "/api/v1/activity/category/all",
            "/api/v1/request/all/user/{id}",
            "/all/user/{id}/user/{userId}/activity/{activityId}",
            "/api/v1/request/{id}",
            "/api/v1/user/{id}",
            "/api/v1/user/{userId}/activity/{activityId}/mark"
    };
    private static final String[] ADMIN_PATHS = {
            "/api/v1/activity/**",
            "/api/v1/activity/category/**",
            "/api/v1/request/all",
            "/api/v1/request/all/user/{id}",
            "/api/v1/request/{id}",
            "/api/v1/request/{id}/approve",
            "/api/v1/request/{id}/reject",
            "/api/v1/user/{id}",
            "/api/v1/user/all"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/v3/api-docs", "/swagger-ui.html", "/swagger-ui/api-docs/swagger-config", "/swagger-ui/**").permitAll()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers(USER_PATHS).hasAuthority("USER")
                .antMatchers(ADMIN_PATHS).hasAuthority("ADMIN")
                .anyRequest().authenticated();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
