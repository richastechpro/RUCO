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
@Table(name = "m_area")
public class Area extends BaseEntity {

	@Column(name = "area_name", nullable = false)
	private String areaName;

	@ManyToOne
	@JoinColumn(name = "city_id")
	@AuditCollection(ignore = true)
	private City city;

}