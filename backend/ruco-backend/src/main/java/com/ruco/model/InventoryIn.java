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
@Table(name = "t_inventory_in")
public class InventoryIn extends BaseEntity {

	@Column(nullable = false)
	private Long collectionItemId;

	@Column(name = "branch_id", nullable = false)
	private Long branchId;

	@Column(name = "inward_qty", nullable = false)
	private Float inwardQty;

}