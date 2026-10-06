package com.ruco.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ruco.service.audit.AuditCollection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "m_company")
public class Company extends BaseEntity {

	@Column(name = "company_name", nullable = false, unique = true)
	private String companyName;

	@Column(name = "company_address", nullable = false, columnDefinition = "TEXT")
	private String companyAddress;

	@Column(name = "state_id", nullable = false)
	private Long stateId;

	@Column(name = "city_id")
	private Long cityId;

	@Column(name = "area_id")
	private Long areaId;

	@Column(name = "contact_person", nullable = false)
	private String contactPerson;

	@Column(name = "mobile_no", nullable = false, length = 10)
	private Long mobileNo;

	@Column(name = "email_id", nullable = false)
	private String emailId;

	@Column(name = "pan_no", nullable = false, length = 10)
	private String panNo;

	@Column(name = "gst_no", nullable = false, length = 15)
	private String gstNo;

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	@AuditCollection
	private List<Branch> branches = new ArrayList<>();

}
