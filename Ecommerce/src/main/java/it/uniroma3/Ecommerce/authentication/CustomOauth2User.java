package it.uniroma3.Ecommerce.authentication;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOauth2User implements OAuth2User {

	private OAuth2User oauth;
	
	public CustomOauth2User(OAuth2User oauth) {
		this.oauth = oauth;
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		return this.oauth.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.oauth.getAuthorities();
	}

	@Override
	public String getName() {
		return this.oauth.getAttribute("name");
	}
	
	public String getFullName() {
		return this.oauth.getAttribute("name");
	}
	
	public String getEmail() {
        return oauth.<String>getAttribute("email");     
    }

}
