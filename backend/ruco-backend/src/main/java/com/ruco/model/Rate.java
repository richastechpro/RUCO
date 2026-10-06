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
@Table(name = "m_rate")
public class Rate extends BaseEntity {

	@Column(name = "price", nullable = false)
	private Float price = 0f;

	@Column(nullable = false)
	private Long fboBranchId;

	public Rate(Float price, Long fboBranchId) {
		super();
		this.price = price;
		this.fboBranchId = fboBranchId;
	}

	public Rate(Float price) {
		super();
		this.price = price;
	}

}