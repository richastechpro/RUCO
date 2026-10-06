package com.ruco.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "m_chat")
public class ChatMessage extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id", nullable = false)
	@AuditCollection(ignore = true)
	private Ticket ticket;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String username;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String message;

}
