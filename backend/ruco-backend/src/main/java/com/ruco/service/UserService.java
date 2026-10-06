package com.ruco.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruco.config.CustomUserDetails;
import com.ruco.constant.SearchBuilder;
import com.ruco.constant.UserType;
import com.ruco.model.Branch;
import com.ruco.model.Collection;
import com.ruco.model.User;
import com.ruco.repo.BranchRepository;
import com.ruco.repo.CollectionRepository;
import com.ruco.repo.UserRepository;
import com.ruco.request.SearchRequest;
import com.ruco.request.UserRequest;
import com.ruco.response.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder encoder;
	private final UserRepository userRepository;
	private final BranchRepository branchRepository;
	private final CollectionRepository collectionRepository;

	public CustomUserDetails getUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			return userDetails;
		}

		else {
			log.error("No authenticated user found.");
			return null;
		}
	}

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		CustomUserDetails user = getUser();
		String loginId = user.getLoginId();
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		String searchTxt = request.getSearchTxt();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "username"));
		Page<User> page = userRepository.findAll(new SearchBuilder().userHasSearchText(searchTxt, loginId), pageable);
		return page.map(u -> mapData(u, loginId));
	}

	private UserResponse mapData(User user, String loginId) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setLoginId(user.getLoginId());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setMobileNo(user.getMobileNo());
		response.setUserType(user.getUserType());
		Long branchId = user.getBranchId();
		if (branchId != null) {
			Optional<Branch> branchOptional = branchRepository.findById(branchId);
			if (branchOptional.isPresent()) {
				Branch branch = branchOptional.get();
				response.setBranchId(branch.getId());
				response.setBranchName(branch.getBranchName());
			}
		}
		return response;
	}

	@Transactional
	public void delete(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setIsDelete(true);
			userRepository.save(user);
		}
	}

	@Transactional(readOnly = true)
	public UserResponse get(Long id) {
		return userRepository.findById(id).map(u -> mapData(u, getUser().getLoginId()))
				.orElseThrow(() -> new EntityNotFoundException("Area not found"));
	}

	@Transactional
	public void create(UserRequest request) {
		String password = request.getPassword();
		String encodedPassword = encoder.encode(password);

		User user = new User();

		user.setId(request.getId());
		user.setLoginId(request.getLoginId());
		user.setPassword(encodedPassword);
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setMobileNo(request.getMobileNo());
		user.setUserType(request.getUserType());
		user.setBranchId(request.getBranchId());
		userRepository.save(user);
	}

	@Transactional
	public void update(UserRequest request) {
		Long id = request.getId();
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setLoginId(request.getLoginId());
			user.setUsername(request.getUsername());
			user.setEmail(request.getEmail());
			user.setMobileNo(request.getMobileNo());
			user.setUserType(request.getUserType());
			user.setBranchId(request.getBranchId());
			userRepository.save(user);
		}
	}

	public List<UserResponse> getPickupBoys(Long collectionId) {
		Optional<Collection> collectionOptional = collectionRepository.findById(collectionId);
		if (collectionOptional.isPresent()) {
			Collection collection = collectionOptional.get();
			Long branchId = collection.getBranchId();
			List<User> users = userRepository.findAllByUserTypeAndBranchIdAndIsDeleteFalse(UserType.PICKUPBOY,
					branchId);
			return users.stream().map(u -> mapData(u, getUser().getLoginId())).collect(Collectors.toList());
		}
		return null;
	}

	public void updatePWD(String pwd) {
		CustomUserDetails user = getUser();
		Long id = user.getId();
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			User user2 = userOptional.get();
			user2.setPassword(encoder.encode(pwd));
			userRepository.save(user2);
		}
	}

}
