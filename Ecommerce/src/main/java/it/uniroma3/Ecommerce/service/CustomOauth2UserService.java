package it.uniroma3.Ecommerce.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import it.uniroma3.Ecommerce.authentication.CustomOauth2User;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		    OAuth2User user = super.loadUser(userRequest);
		    return new CustomOauth2User(user);
	}

	
}
