package com.ruco.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.ruco.enums.TicketRequestStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "m_rate_request")
public class RateRequest extends BaseEntity {

	@Column(name = "old_price", nullable = false)
	private Float oldPrice = 0f;

	@Column(name = "new_price", nullable = false)
	private Float newPrice;

	@Column(nullable = false)
	private Long fboBranchId;

	@Column(nullable = false)
	private String fboBranchName;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String username;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TicketRequestStatus status = TicketRequestStatus.NOT_APPROVED;

}