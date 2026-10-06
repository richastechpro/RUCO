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
@Table(name = "t_image")
public class Image extends BaseEntity{

	@Column(name = "file_name",nullable = false)
	private String fileName;

	@Column(name = "collection_item_id",nullable = false)
	private Long requestId;

}
