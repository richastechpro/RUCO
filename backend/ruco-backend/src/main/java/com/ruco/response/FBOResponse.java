package com.ruco.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FBOResponse {

	private Long id;
	private String fboName;
	private String bankName;
	private String accountType;
	private String accountNo;
	private String accountHolderName;
	private String ifscCode;
	private Long mobileNo;
	private List<FboBranchResponse> fboBranches;
	private Integer fboBranchesCount;
	private Integer fboUsersCount;

}
