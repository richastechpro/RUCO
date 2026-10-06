package com.ruco.constant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.ruco.model.Area;
import com.ruco.model.Branch;
import com.ruco.model.City;
import com.ruco.model.Collection;
import com.ruco.model.Customer;
import com.ruco.model.FBO;
import com.ruco.model.State;
import com.ruco.model.Transporter;
import com.ruco.model.User;

public class SearchBuilder {

	public static Specification<Branch> branchHasSearchText(String search, List<Long> branchIds) {
		return (root, query, cb) -> {
			Predicate notDeleted = cb.isFalse(root.get("isDelete"));
			Predicate searchPredicate = cb.conjunction();
			if (search != null && !search.trim().isEmpty()) {
				String pattern = "%" + search.toLowerCase() + "%";
				Predicate name = cb.like(cb.lower(root.get("branchName")), pattern);
				Predicate address = cb.like(cb.lower(root.get("branchAddress")), pattern);
				Predicate gst = cb.like(cb.lower(root.get("gstNo")), pattern);
				Predicate pan = cb.like(cb.lower(root.get("panNo")), pattern);
				searchPredicate = cb.or(name, address, gst, pan);
			}
			Predicate branchIdPredicate = cb.conjunction();
			if (branchIds != null && !branchIds.isEmpty()) {
				branchIdPredicate = root.get("id").in(branchIds);
			}
			return cb.and(notDeleted, branchIdPredicate, searchPredicate);
		};
	}

	public Specification<User> userHasSearchText(String search, String loginId) {
		
		return (root, query, cb) -> {
			if (search == null || search.trim().isEmpty()) {
				return cb.isFalse(root.get("isDelete"));
			}
			String pattern = "%" + search.toLowerCase() + "%";
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.like(cb.lower(root.get("loginId")), pattern));
			predicates.add(cb.like(cb.lower(root.get("password")), pattern));
			predicates.add(cb.like(cb.lower(root.get("username")), pattern));
			predicates.add(cb.like(cb.lower(root.get("email")), pattern));
			predicates.add(cb.like(cb.concat(root.get("mobileNo").as(String.class), ""), pattern));
			predicates.add(cb.like(cb.lower(root.get("userType")), pattern));
			predicates.add(cb.notEqual(cb.lower(root.get("loginId")), loginId));
			return cb.and(cb.isFalse(root.get("isDelete")), cb.or(predicates.toArray(new Predicate[0])));
		};
	}

	public static Specification<Customer> customerHasSearchText(String search) {
		return (root, query, cb) -> {
			if (search == null || search.trim().isEmpty()) {
				return cb.isFalse(root.get("isDelete"));
			}
			String pattern = "%" + search.toLowerCase() + "%";
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.like(cb.lower(root.get("customerName")), pattern));
			predicates.add(cb.like(cb.lower(root.get("customerAddress")), pattern));
			predicates.add(cb.like(cb.lower(root.get("customerType")), pattern));
			predicates.add(cb.like(cb.lower(root.get("email")), pattern));
			predicates.add(cb.like(cb.concat(root.get("mobileNo").as(String.class), ""), pattern));
			return cb.and(cb.isFalse(root.get("isDelete")), cb.or(predicates.toArray(new Predicate[0])));
		};
	}

	public static Specification<Transporter> transporterHasSearchText(String search, Long branchId) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			Predicate notDeleted = cb.isFalse(root.get("isDelete"));
			predicates.add(notDeleted);
			if (branchId != null) {
				predicates.add(cb.equal(root.get("branchId"), branchId));
			}
			if (search != null && !search.trim().isEmpty()) {
				String pattern = "%" + search.toLowerCase() + "%";
				List<Predicate> searchPredicates = new ArrayList<>();
				searchPredicates.add(cb.like(cb.lower(root.get("contactPerson")), pattern));
				searchPredicates.add(cb.like(cb.lower(root.get("transporterName")), pattern));
				searchPredicates.add(cb.like(cb.lower(root.get("transporterAddress")), pattern));
				searchPredicates.add(cb.like(cb.concat(root.get("mobileNo").as(String.class), ""), pattern));
				searchPredicates.add(cb.like(cb.lower(root.get("vehicleNo")), pattern));
				searchPredicates.add(cb.like(cb.lower(root.get("licenseNo")), pattern));
				searchPredicates.add(cb.like(cb.lower(root.get("gstNo")), pattern));
				searchPredicates.add(cb.like(cb.lower(root.get("panNo")), pattern));
				searchPredicates.add(cb.like(cb.lower(root.get("contractDate")), pattern));
				Predicate searchCombined = cb.or(searchPredicates.toArray(new Predicate[0]));
				predicates.add(searchCombined);
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<State> stateHasSearchText(String search) {
		return (root, query, cb) -> {
			if (search == null || search.trim().isEmpty()) {
				return cb.isFalse(root.get("isDelete"));
			}
			String pattern = "%" + search.toLowerCase() + "%";
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.like(cb.lower(root.get("stateName")), pattern));
			return cb.and(cb.isFalse(root.get("isDelete")), cb.or(predicates.toArray(new Predicate[0])));
		};
	}

	public static Specification<City> cityHasSearchText(String search) {
		return (root, query, cb) -> {
			if (search == null || search.trim().isEmpty()) {
				return cb.isFalse(root.get("isDelete"));
			}
			String pattern = "%" + search.toLowerCase() + "%";
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.like(cb.lower(root.get("cityName")), pattern));
			return cb.and(cb.isFalse(root.get("isDelete")), cb.or(predicates.toArray(new Predicate[0])));
		};
	}

	public static Specification<Area> areaHasSearchText(String search) {
		return (root, query, cb) -> {
			if (search == null || search.trim().isEmpty()) {
				return cb.isFalse(root.get("isDelete"));
			}
			String pattern = "%" + search.toLowerCase() + "%";
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.like(cb.lower(root.get("areaName")), pattern));
			return cb.and(cb.isFalse(root.get("isDelete")), cb.or(predicates.toArray(new Predicate[0])));
		};
	}

	public static Specification<FBO> fboHasSearchText(String search, List<Long> fboIds) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.isFalse(root.get("isDelete")));
			if (fboIds != null && !fboIds.isEmpty()) {
				predicates.add(root.get("id").in(fboIds));
			}
			if (search != null && !search.trim().isEmpty()) {
				String pattern = "%" + search.toLowerCase() + "%";
				predicates.add(cb.like(cb.lower(root.get("fboName")), pattern));
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<Collection> collectionHasSearchText(String search, List<Long> fboBranchIds,
			Long branchId, Long userId) {

		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.isFalse(root.get("isDelete")));
			if (fboBranchIds != null && !fboBranchIds.isEmpty()) {
				Join<Object, Object> itemsJoin = root.join("items", JoinType.INNER);
				predicates.add(itemsJoin.get("fboBranchId").in(fboBranchIds));
				query.distinct(true);
			}
			if (branchId != null) {
				predicates.add(cb.equal(root.get("branchId"), branchId));
			}
			if (userId != null) {
				Predicate matchPickupBoy1 = cb.equal(root.get("pickupboy1"), userId);
				Predicate matchPickupBoy2 = cb.equal(root.get("pickupboy2"), userId);
				predicates.add(cb.or(matchPickupBoy1, matchPickupBoy2));
			}
			if (search != null && !search.trim().isEmpty()) {
				String pattern = "%" + search.toLowerCase() + "%";
				List<Predicate> searchPredicates = new ArrayList<>();
				searchPredicates.add(cb.like(cb.lower(root.get("collectionName")), pattern));
				predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

}
