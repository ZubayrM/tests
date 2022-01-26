package com.zubayr.tests.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter jwtTokenFilter;
    private final JwtTokenConfig jwtTokenConfig;

    @Autowired
    public WebSecurityConfig(JwtTokenFilter jwtTokenFilter, JwtTokenConfig jwtTokenConfig) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.jwtTokenConfig = jwtTokenConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/regis").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(jwtTokenConfig)
                .and()
                .logout()
                .logoutRequestMatcher(new RegexRequestMatcher("/logout","GET"))
                .deleteCookies("Authorization").logoutSuccessUrl("/")

        ;
                //.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);



    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
