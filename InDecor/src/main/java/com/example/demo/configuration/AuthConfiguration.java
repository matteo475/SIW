package com.example.demo.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.example.demo.model.Credentials.ADMIN_ROLE;
import static com.example.demo.model.Credentials.USER_ROLE;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {
  
	@Autowired private DataSource dataSource;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		 auth.jdbcAuthentication().dataSource(dataSource)
		 .authoritiesByUsernameQuery("SELECT username,ruolo from credentials WHERE username=?")
		 .usersByUsernameQuery("SELECT username,password, 1 as enabled from credentials WHERE username=?");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	protected SecurityFilterChain configure(final HttpSecurity HttpSecurity) throws Exception{
		HttpSecurity.csrf().and().cors().disable().authorizeHttpRequests()
		.requestMatchers(HttpMethod.GET, "/","/index","register","/css/**","/images/**","favicon.ico").permitAll()
		.requestMatchers(HttpMethod.POST, "/register","/login").permitAll()
		.requestMatchers(HttpMethod.GET,"/admin/**").hasAnyAuthority(ADMIN_ROLE)
		.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
		.anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
		.defaultSuccessUrl("/success",true)
		.failureUrl("/login?error=true").and().logout().logoutSuccessUrl("/").invalidateHttpSession(true)
		.deleteCookies("JSESSIONID").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.clearAuthentication(true).permitAll();
		return HttpSecurity.build();
	}
}
