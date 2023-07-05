package com.example.javaecommerce.security;


import com.example.javaecommerce.security.jwt.AuthEntryPointJwt;
import com.example.javaecommerce.security.jwt.AuthTokenFilter;
import com.example.javaecommerce.security.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor

public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/api/test/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/api/v1/products/**").permitAll()
                .antMatchers("/api/auth/verifyRegistration/**").permitAll()
                .requestMatchers(publicEndpoints()).permitAll()
                .requestMatchers(privateEndpoints()).authenticated()
                .anyRequest().authenticated();

        http.logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private RequestMatcher publicEndpoints() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/v1/products/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/category/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/category/**", HttpMethod.DELETE.name()),
                new AntPathRequestMatcher("/api/v1/category/**", HttpMethod.PUT.name())
        );
    }

    private RequestMatcher privateEndpoints() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/v1/users/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/category/**", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/api/v1/orders/**", HttpMethod.POST.name())
        );
    }
}
