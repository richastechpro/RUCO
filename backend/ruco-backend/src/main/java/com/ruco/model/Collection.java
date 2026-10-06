package com.ruco.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.ruco.constant.CollectionType;
import com.ruco.service.audit.AuditCollection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "t_collection")
public class Collection extends BaseEntity {

	@Column(name = "collection_name", nullable = false)
	private String collectionName;

	@Column(name = "type", nullable = false)
	private String collectionType = CollectionType.SINGLE;

	@Column(name = "added_by")
	private String addedBy;

	@Column(name = "branch_id", nullable = false)
	private Long branchId;

	@Column(name = "warehouse_id")
	private Long warehouseId;

	@Column(name = "is_assigned")
	private Boolean isAssigned = Boolean.FALSE;

	@Column(name = "assigned_by")
	private String assignedBy;

	@Column(name = "assigned_on")
	private LocalDateTime assignedOn;

	@Column(name = "pickupboy_id1")
	private Long pickupboy1;

	@Column(name = "pickupboy_id2")
	private Long pickupboy2;

	@Column(name = "transporter_id")
	private Long transporter;

	@Column(name = "fbo_challan_created")
	private Boolean fboChallanCreated = Boolean.FALSE;

	@Column(name = "fbo_payment_status")
	private String fboPaymentStatus;

	@Column(name = "transporter_challan_created")
	private Boolean transporterChallanCreated = Boolean.FALSE;

	@Column(name = "labour_challan_created")
	private Boolean labourChallanCreated = Boolean.FALSE;

	@Column(name = "certificate_no")
	private String certificateNo;

	@Where(clause = "is_delete = false")
	@OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
	@AuditCollection
	private List<CollectionItem> items = new ArrayList<>();

}
