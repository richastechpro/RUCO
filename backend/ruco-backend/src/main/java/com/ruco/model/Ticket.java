package com.ruco.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ruco.service.audit.AuditCollection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "m_ticket")
public class Ticket extends BaseEntity {

	@Column(name = "ticket_id", unique = true, nullable = false, length = 50)
	private String ticketId;

	@Column(nullable = false)
	private String subject;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String status = "OPEN";

	@OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
	@OrderBy("saveDate ASC")
	@AuditCollection
	private List<ChatMessage> chats = new ArrayList<>();

	public Ticket(String ticketId, String subject, String description, Long userId) {
		this.ticketId = ticketId;
		this.subject = subject;
		this.description = description;
		this.userId = userId;
	}

}