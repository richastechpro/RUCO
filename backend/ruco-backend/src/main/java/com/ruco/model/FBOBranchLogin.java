package com.ruco.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ruco.service.audit.AuditCollection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "m_fbo_branch_login")
public class FBOBranchLogin extends BaseEntity {

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "pwd", nullable = false)
	private String pwd;

	@Column(name = "user_name", nullable = false)
	private String username;

	@Column(name = "mobile_no", nullable = false, length = 10)
	private Long mobileNo;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "state_id")
	private Long stateId;

	@Column(name = "city_id")
	private Long cityId;

	@Column(name = "fbo_branch_id")
	private Long fboBranchId;

	@Column
	private Long branchId;

	@ManyToOne
	@JoinColumn(name = "fbo_id", nullable = false)
	@AuditCollection(ignore = true)
	private FBO fbo;

}