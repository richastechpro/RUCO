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
@Table(name = "m_transporter")
public class Transporter extends BaseEntity {

	@Column(name = "contact_person", nullable = false)
	private String contactPerson;

	@Column(name = "transporter_name", nullable = false)
	private String transporterName;

	@Column(name = "transporter_address", nullable = false, columnDefinition = "TEXT")
	private String transporterAddress;

	@Column(name = "state_id", nullable = false)
	private Long stateId;

	@Column(name = "city_id", nullable = false)
	private Long cityId;

	@Column(name = "area_id")
	private Long areaId;

	@Column(name = "mobile_no", nullable = false, length = 10)
	private Long mobileNo;

	@Column(name = "vehicle_no", nullable = false)
	private String vehicleNo;

	@Column(name = "license_no", nullable = false)
	private String licenseNo;

	@Column(name = "gst_no", length = 15, nullable = false)
	private String gstNo;

	@Column(name = "pan_no", length = 10, nullable = false)
	private String panNo;

	@Column(name = "contract_date", nullable = false)
	private String contractDate;

	@Column(name = "rate_per_kg", precision = 12, scale = 2, nullable = false)
	private Float ratePerKg = 0f;

	@Column(name = "collected_qty", precision = 12, scale = 2, nullable = false)
	private Float collectedQty = 0f;

	@Column(name = "branch_id", nullable = false)
	private Long branchId;

}