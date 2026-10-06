package com.ruco.service;

import javax.security.auth.login.LoginException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ruco.config.CustomUserDetails;
import com.ruco.config.JwtService;
import com.ruco.request.LoginRequest;
import com.ruco.request.UpdatePwdRequest;
import com.ruco.response.LoginResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final UserService userService;

	public LoginResponse login(LoginRequest loginRequest) throws LoginException {

		String email = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		boolean authenticated = authenticate.isAuthenticated();
		if (authenticated) {
			String token = jwtService.generate(email);
			CustomUserDetails detail = (CustomUserDetails) authenticate.getPrincipal();
			LoginResponse response = new LoginResponse(detail.getUsername(), detail.getUserType(), token,
					detail.getPermissions());
			return response;
		}

		else {
			throw new LoginException("Invalid Credentials");
		}

	}

	public void updatePWD(UpdatePwdRequest request) {
		userService.updatePWD(request.getPwd());
	}

}
