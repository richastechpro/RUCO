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
@Table(name = "m_customer")
public class Customer extends BaseEntity {

	@Column(name = "customer_name", nullable = false)
	private String customerName;

	@Column(name = "customer_address", columnDefinition = "TEXT", nullable = false)
	private String customerAddress;

	@Column(name = "customer_type", nullable = false)
	private String customerType;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "mobile_no", nullable = false)
	private Long mobileNo;

}