package com.ruco.service;

import com.ruco.model.Area;
import com.ruco.model.Branch;
import com.ruco.model.City;
import com.ruco.model.Collection;
import com.ruco.model.Company;
import com.ruco.model.Customer;
import com.ruco.model.FBO;
import com.ruco.model.State;
import com.ruco.model.Transporter;
import com.ruco.model.User;

public class UtilService {

	public static Class<?> resolveEntityClass(String entity) {
		switch (entity) {
		case "Company":
			return Company.class;
		case "Branch":
			return Branch.class;
		case "User":
			return User.class;
		case "Customer":
			return Customer.class;
		case "Transporter":
			return Transporter.class;
		case "State":
			return State.class;
		case "City":
			return City.class;
		case "Area":
			return Area.class;
		case "FBO":
			return FBO.class;
		case "Collection":
			return Collection.class;
		default:
			return null;
		}
	}

}
