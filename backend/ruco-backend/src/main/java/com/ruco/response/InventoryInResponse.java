package com.ruco.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryInResponse {

	private String branchName;
	private String warehouse;
	private Float qty;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime date;
	private Float rate;
	private Float total;
}
