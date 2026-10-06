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
@Table(name = "m_warehouse")
public class Warehouse extends BaseEntity {

	@Column(name = "warehouse_name", nullable = false)
	private String warehouseName;

	@Column(name = "warehouse_address", nullable = false, columnDefinition = "TEXT")
	private String warehouseAddress;

}