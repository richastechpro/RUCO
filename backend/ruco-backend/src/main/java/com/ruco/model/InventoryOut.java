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
@Table(name = "t_inventory_out")
public class InventoryOut extends BaseEntity {

	@Column(name = "added_by", nullable = false)
	private String addedBy;

	@Column(name = "branch_id", nullable = false)
	private Long branchId;

	@Column(name = "cutomer_id", nullable = false)
	private Long customerId;

	@Column(name = "qty", nullable = false)
	private Float qty;

	@Column(name = "comment", columnDefinition = "TEXT")
	private String comment;

	@Column(name = "vehicle_no")
	private String vehicleNo;

	@Column(name = "transporter")
	private String transporter;

	@Column(nullable = false)
	private String ids;

}