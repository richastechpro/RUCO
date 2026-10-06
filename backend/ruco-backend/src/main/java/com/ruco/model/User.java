package com.ruco.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "m_user")
public class User extends BaseEntity {

	@Column(name = "login_id", unique = true, nullable = false)
	private String loginId;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "user_name", nullable = false)
	private String username;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "mobile_no", nullable = false, length = 10)
	private Long mobileNo;

	@Column(name = "user_type", nullable = false)
	private String userType;

	@Column(name = "branch_id")
	private Long branchId;

}
