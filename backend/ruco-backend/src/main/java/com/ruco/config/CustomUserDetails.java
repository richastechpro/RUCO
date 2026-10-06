package com.ruco.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ruco.constant.UserPermission;
import com.ruco.constant.UserType;
import com.ruco.model.FBO;
import com.ruco.model.FBOBranchLogin;
import com.ruco.model.User;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String loginId;
	private String password;
	private String username;
	private String email;
	private Long mobileNo;
	private String userType;
	private Boolean isDelete;
	private Long fboId;
	private Long fboBranchId;
	private List<String> permissions;
	private Boolean isFboUser = false;
	private Long stateId;
	private Long cityId;
	private Long branchId;

	public CustomUserDetails(User user) {
		this.id = user.getId();
		this.loginId = user.getLoginId();
		this.password = user.getPassword();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.mobileNo = user.getMobileNo();
		this.userType = user.getUserType();
		this.isDelete = user.getIsDelete();
		this.branchId = user.getBranchId();

		switch (this.userType) {
		case UserType.SUPERADMIN:
			this.permissions = Arrays.asList(UserPermission.ADD, UserPermission.VIEW, UserPermission.UPDATE,
					UserPermission.DELETE);
			break;

		case UserType.MANAGER:
			this.permissions = Arrays.asList(UserPermission.ADD, UserPermission.VIEW, UserPermission.UPDATE);
			break;

		case UserType.PICKUPBOY:
			this.permissions = Arrays.asList(UserPermission.VIEW, UserPermission.UPDATE);
			break;

		default:
			break;
		}
	}

	public CustomUserDetails(FBOBranchLogin user) {
		this.id = user.getId();
		this.loginId = user.getEmail();
		this.password = user.getPassword();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.mobileNo = user.getMobileNo();
		this.userType = user.getType();
		this.isDelete = user.getIsDelete();
		FBO fbo = user.getFbo();
		if (fbo != null) {
			Long id = fbo.getId();
			this.fboId = id;
		}
		Long fboBranchId = user.getFboBranchId();
		this.fboBranchId = fboBranchId;
		this.isFboUser = true;
		this.stateId = user.getStateId();
		this.cityId = user.getCityId();
	}

	public String getLoginId() {
		return this.loginId;
	}

	public String getEmail() {
		return this.email;
	}

	public Long getMobileNo() {
		return this.mobileNo;
	}

	public String getUserType() {
		return this.userType;
	}

	public List<String> getPermissions() {
		return this.permissions;
	}

	public Long getFboId() {
		return fboId;
	}

	public Long getFboBranchId() {
		return fboBranchId;
	}

	public Boolean getIsFboUser() {
		return isFboUser;
	}

	public Long getStateId() {
		return stateId;
	}

	public Long getCityId() {
		return cityId;
	}

	public Long getBranchId() {
		return branchId;
	}

	public Long getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !this.isDelete;
	}

}