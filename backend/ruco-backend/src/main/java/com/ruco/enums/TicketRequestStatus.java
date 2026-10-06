package com.ruco.enums;

public enum TicketRequestStatus {

	NOT_APPROVED("Not Approved"),
	APPROVED("Approved"),
	REJECTED("Rejected");

	private final String label;

	TicketRequestStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
