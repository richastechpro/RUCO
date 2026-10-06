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
@Table(name = "m_branch")
public class Branch extends BaseEntity {

	@Column(name = "branch_name", nullable = false)
	private String branchName;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String branchAddress;

	@Column(name = "state_id", nullable = false)
	private Long stateId;

	@Column(name = "city_id", nullable = false)
	private Long cityId;

	@Column(name = "area_id")
	private Long areaId;

	@Column(name = "gst_no", nullable = false, length = 15)
	private String gstNo;

	@Column(name = "pan_no", nullable = false, length = 10)
	private String panNo;

	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false)
	@AuditCollection(ignore = true)
	private Company company;

	@Column(name = "warehouse_id")
	private Long warehouseId;

}