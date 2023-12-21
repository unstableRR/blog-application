package com.blogapplication.config;

import com.blogapplication.security.CustomUserDetailService;
import com.blogapplication.security.JwtAuthenticationEntryPoint;
import com.blogapplication.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig { //extends WebSecurityConfigurerAdapter {

    public static final String[] PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/v3/api-docs",
            "/v2/api-docs",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Autowired
    private CustomUserDetailService cuds;

    @Autowired
    private JwtAuthenticationEntryPoint jaep;

    @Autowired
    private JwtAuthenticationFilter jaf;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
    	 http
         .csrf()
         .disable()
         .authorizeHttpRequests()
                 .antMatchers(PUBLIC_URLS).permitAll()
                 .antMatchers(HttpMethod.GET).permitAll()
         .anyRequest()
         .authenticated()
         .and()
                 .exceptionHandling().authenticationEntryPoint(this.jaep)
         .and()
                 .sessionManagement()
                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

         http.addFilterBefore(this.jaf, UsernamePasswordAuthenticationFilter.class);

         http.authenticationProvider(daoAuthenticationprovider());
         DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
         
         return defaultSecurityFilterChain;
    	
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//                        http
//                        .csrf()
//                        .disable()
//                        .authorizeHttpRequests()
//                                .antMatchers(PUBLIC_URLS).permitAll()
//                                .antMatchers(HttpMethod.GET).permitAll()
//                        .anyRequest()
//                        .authenticated()
//                        .and()
//                                .exceptionHandling().authenticationEntryPoint(this.jaep)
//                        .and()
//                                .sessionManagement()
//                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//                        http.addFilterBefore(this.jaf, UsernamePasswordAuthenticationFilter.class);
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//
//        auth.userDetailsService(cuds).passwordEncoder(passwordEncoder());
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception{
//        return super.authenticationManagerBean();
//    }
    
    @Bean
    public DaoAuthenticationProvider daoAuthenticationprovider() {
    	
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(cuds);
        provider.setPasswordEncoder(passwordEncoder());
        
        return provider;
    	
    }
    
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
