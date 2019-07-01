package org.tim.configurations.securityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.tim.utils.Mapping.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
//                    .authenticationEntryPoint(restAuthenticationEntryPoint)
//                .and()
//                    .exceptionHandling()
//                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                    .cors()
                .and()
                    .headers()
                        .frameOptions().disable()
                .and()
                    .csrf().disable()
                    .authorizeRequests()
                        .antMatchers("/", "/login/**", "/error/**", "/api/v1/exportCI/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .failureHandler(customAuthenticationFailureHandler())
                    .successHandler(customAuthenticationSuccessHandler())
                .and()
                    .logout()
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true);
    }


    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager);
    }
}
