package com.ruco.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
//@EntityListeners(AuditEntityListener.class)
public abstract class BaseEntity {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "is_delete", nullable = false)
	private Boolean isDelete = Boolean.FALSE;

	@CreationTimestamp
	@Column(name = "save_date", updatable = false)
	private LocalDateTime saveDate;

	@UpdateTimestamp
	@Column(name = "modify_date")
	private LocalDateTime modifyDate;

}