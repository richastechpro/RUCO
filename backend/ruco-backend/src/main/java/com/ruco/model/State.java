package com.ruco.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "m_state")
public class State extends BaseEntity {

	@Column(name = "state_name", nullable = false)
	private String stateName;

	@OneToMany
	@JoinColumn(name = "state_id")
	@Where(clause = "is_delete = false")
	@AuditCollection
	private Set<City> cities;

}