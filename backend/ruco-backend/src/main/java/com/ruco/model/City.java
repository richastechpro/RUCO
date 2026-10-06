package com.ruco.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.ruco.service.audit.AuditCollection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "m_city")
public class City extends BaseEntity {

	@Column(name = "city_name", nullable = false)
	private String cityName;

	@ManyToOne
	@JoinColumn(name = "state_id")
	@AuditCollection(ignore = true)
	private State state;

	@OneToMany
	@JoinColumn(name = "city_id")
	@Where(clause = "is_delete = false")
	private Set<Area> areas;

}