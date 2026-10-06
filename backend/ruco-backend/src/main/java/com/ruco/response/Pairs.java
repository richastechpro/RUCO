package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pairs<A, B, C> {

	private A v1;
	private B v2;
	private C v3;

}
