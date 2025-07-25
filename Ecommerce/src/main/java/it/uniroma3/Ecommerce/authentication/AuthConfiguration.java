package it.uniroma3.Ecommerce.authentication;

import static it.uniroma3.Ecommerce.model.Credentials.PROVIDER_ROLE;

import java.io.IOException;

import static it.uniroma3.Ecommerce.model.Credentials.DEFAULT_ROLE;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import it.uniroma3.Ecommerce.authentication.CustomOauth2User;

import it.uniroma3.Ecommerce.service.CustomOauth2UserService;
import it.uniroma3.Ecommerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private CustomOauth2UserService oauthUserService;
	@Autowired
	private UserService userService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
				.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().and().cors().disable().authorizeHttpRequests()
				.requestMatchers(HttpMethod.GET, "/", "/index", "/register", "/css/**", "/images/**", "favicon.ico",
						"/carrello", "/prodotto/**", "/userProfile/**", "/ricercaconfiltro", "/ricercafiltro",
						"/user/**", "/user/profile/addresses/**", "/password_dimenticata", "/reimposta_password",
						"/oauth/**")
				.permitAll()
				.requestMatchers(HttpMethod.POST, "/register", "/login", "/company/showCreateProduct", "/prodotto/**",
						"/", "/index", "/userProfile/**", "/ricercaconfiltro", "/ricercafiltro", "/ricercaHome",
						"/user/**", "/user/profile/addresses/**", "/password_dimenticata", "/reimposta_password",
						"/oauth/**")
				.permitAll().requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(PROVIDER_ROLE)
				.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(PROVIDER_ROLE).anyRequest()
				.authenticated().and().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/success", true)
				.failureUrl("/login?error=true").and().oauth2Login() // <--- questa è la parte che mancava!
				.loginPage("/login").userInfoEndpoint().userService(oauthUserService).and()
				.successHandler(new AuthenticationSuccessHandler() {

					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						CustomOauth2User oauthUser = (CustomOauth2User) authentication.getPrincipal();

						userService.processOAuthPostLogin(oauthUser);

						response.sendRedirect("/success");
					}

				}).and().logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
				.deleteCookies("JSESSIONID").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.clearAuthentication(true).permitAll();

		return httpSecurity.build();
	}

}
