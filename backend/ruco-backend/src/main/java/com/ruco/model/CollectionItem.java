package com.ruco.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "t_collection_item")
public class CollectionItem extends BaseEntity {

	@Column(name = "fbo_branch_id", nullable = false)
	private Long fboBranchId;

	@Column(name = "request_date", nullable = false)
	private String requestDate;

	@Column(name = "qty", nullable = false)
	private Float qty;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "invoice_no")
	private String invoiceNo;

	@Column(name = "collected_qty")
	private Float collectedQty;

	@Column(name = "collected_by")
	private String collectedBy;

	@Column(name = "collected_on")
	private String collectedOn;

	@Column(name = "is_collected")
	private Boolean isCollected = Boolean.FALSE;

	@Column(name = "rate_id", nullable = false)
	private Long rateId;

	@ManyToOne
	@JoinColumn(name = "collection_id", nullable = false)
	@AuditCollection(ignore = true)
	private Collection collection;

	@OneToMany
	@AuditCollection
	private List<Image> images;
	
	@OneToOne
	@Where(clause = "is_delete = false")
	@AuditCollection
	private InventoryIn inventoryIn;

}