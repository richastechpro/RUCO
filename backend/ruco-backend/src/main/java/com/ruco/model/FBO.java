package com.ruco.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.ruco.service.audit.AuditCollection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "m_fbo")
public class FBO extends BaseEntity {

	@Column(name = "fbo_name", nullable = false)
	private String fboName;

	@Column(name = "bank_name", nullable = false)
	private String bankName;

	@Column(name = "account_type", nullable = false)
	private String accountType;

	@Column(name = "account_no", nullable = false, length = 20)
	private String accountNo;

	@Column(name = "account_holder_name", nullable = false)
	private String accountHolderName;

	@Column(name = "ifsc_code", nullable = false, length = 11)
	private String ifscCode;

	@Column(name = "mobile_no", nullable = false, length = 10)
	private Long mobileNo;

	@Where(clause = "is_delete = false")
	@OneToMany(mappedBy = "fbo", cascade = CascadeType.ALL, orphanRemoval = true)
	@AuditCollection
	private List<FBOBranch> fboBranches;

	@Where(clause = "is_delete = false")
	@OneToMany(mappedBy = "fbo", cascade = CascadeType.ALL, orphanRemoval = true)
	@AuditCollection
	private List<FBOBranchLogin> fboBranchLogins;

}
