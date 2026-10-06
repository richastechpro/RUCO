package com.ruco.config;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.ruco.exception.UserNotFoundException;
import com.ruco.model.FBOBranchLogin;
import com.ruco.model.User;
import com.ruco.repo.FboBranchLoginRepository;
import com.ruco.repo.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FboBranchLoginRepository fboBranchLoginRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String loginId) {

		if (loginId.endsWith("admin")) {
			loginId = loginId.substring(0, loginId.length() - 5);
			Optional<User> userOptional = userRepository.findByLoginIdAndIsDeleteFalse(loginId);
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				CustomUserDetails details = new CustomUserDetails(user);
				return details;
			}

			else {
				throw new UserNotFoundException("Employee not found with code :" + loginId);
			}
		}

		else if (loginId.endsWith("fbo")) {
			loginId = loginId.substring(0, loginId.length() - 3);
			Optional<FBOBranchLogin> fboBranchLoginOptional = fboBranchLoginRepository
					.findByEmailAndIsDeleteFalse(loginId);
			if (fboBranchLoginOptional.isPresent()) {
				FBOBranchLogin fboBranchLogin = fboBranchLoginOptional.get();
				CustomUserDetails details = new CustomUserDetails(fboBranchLogin);
				return details;
			}

			else {
				throw new UserNotFoundException("Employee not found with code :" + loginId);
			}
		}

		throw new UserNotFoundException("Invalid Type");

	}
}