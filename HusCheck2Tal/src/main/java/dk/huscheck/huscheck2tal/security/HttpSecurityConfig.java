package dk.huscheck.huscheck2tal.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler("/");
		
		// @formatter:off
		http
			.authorizeRequests(a -> a
				.antMatchers("/", "/error", "/webjars/**", "/css/**").permitAll()
				.anyRequest().authenticated()
			)
			.logout(l -> l
		            .logoutSuccessUrl("/").permitAll()
		    )
			.csrf(c -> c
		            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	        )
			.exceptionHandling(e -> e
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
			)
			.oauth2Login()
	        .userInfoEndpoint(u -> u
	            .userService(oauth2UserService()));

		// @formatter:on
	}


	  private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
		    DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
		    oAuth2UserService.setRequestEntityConverter(new OAuth2UserRequestDropboxEntityConverter());
		    return oAuth2UserService;
		  }
}

