package com.ruco.service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ruco.model.Branch;
import com.ruco.model.City;
import com.ruco.model.InventoryIn;
import com.ruco.model.State;
import com.ruco.model.Warehouse;
import com.ruco.repo.BranchRepository;
import com.ruco.repo.CityRepository;
import com.ruco.repo.CollectionItemRepository;
import com.ruco.repo.InventoryInRepository;
import com.ruco.repo.StateRepository;
import com.ruco.repo.WarehouseRepository;
import com.ruco.request.InventoryAllRequest;
import com.ruco.response.InventoryItemResponse;
import com.ruco.response.InventoryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

	private final InventoryInRepository inventoryInRepository;
	private final BranchRepository branchRepository;
	private final WarehouseRepository warehouseRepository;
	private final CollectionItemRepository collectionItemRepository;
	private final StateRepository stateRepository;
	private final CityRepository cityRepository;

	public InventoryResponse getInventoryAll(InventoryAllRequest request) {
		List<InventoryIn> inProjections = inventoryInRepository.findAllByIsDeleteFalse();
		Map<Long, Branch> branchMap = getBranchMap();
		Map<Long, State> stateMap = getStateMap();
		Map<Long, City> cityMap = getCityMap();
		Map<Long, List<InventoryIn>> map = new HashMap<>();
		List<InventoryItemResponse> response = new ArrayList<>();
		inProjections.forEach(o -> {
			Long branchId = o.getBranchId();
			if (map.containsKey(branchId)) {
				List<InventoryIn> ins = map.get(branchId);
				ins.add(o);
				map.put(branchId, ins);
			} else {
				List<InventoryIn> list = new ArrayList<>();
				list.add(o);
				map.put(branchId, list);
			}
		});

		map.forEach((k, v) -> {
			Long branchId = k;
			List<InventoryIn> ins = v;
			Branch branch = branchMap.get(branchId);
			if (branch != null) {
				String branchName = branch.getBranchName();
				Long stateId = branch.getStateId();
				Long cityId = branch.getCityId();
				Long warehouseId = branch.getWarehouseId();
				Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouseId);
				if (warehouseOptional.isPresent()) {
					Warehouse warehouse = warehouseOptional.get();
					String warehouseName = warehouse.getWarehouseName();
					InventoryItemResponse r = new InventoryItemResponse();
					Float totalQty = 0f;
					Float totalAmount = 0f;
					for (InventoryIn in : ins) {
						Float inwardQty = in.getInwardQty();
						Long collectionItemId = in.getCollectionItemId();
						Optional<Float> rateOptional = collectionItemRepository.getRate(collectionItemId);
						if (rateOptional.isPresent()) {
							totalQty += inwardQty;
							Float rate = rateOptional.get();
							Float total = inwardQty * rate;
							totalAmount += total;
						}
					}
					r.setBranchName(branchName);
					r.setWarehouse(warehouseName);
					r.setQty(totalQty);
					r.setAmount(totalAmount);
					r.setState(stateMap.getOrDefault(stateId, new State()).getStateName());
					r.setCity(cityMap.getOrDefault(cityId, new City()).getCityName());
					response.add(r);
				}
			}

		});

		InventoryResponse res = new InventoryResponse();
		Float totalQty = 0f;
		Float totalAmount = 0f;
		for (InventoryItemResponse ir : response) {
			Float qty = ir.getQty();
			Float amount = ir.getAmount();
			totalQty += qty;
			totalAmount += amount;
		}
		res.setTotalQty(totalQty);
		res.setTotalAmount(totalAmount);
		
		res.setItems(response);
		return res;
	}

	private Map<Long, Branch> getBranchMap() {
		return branchRepository.findAll().stream().collect(Collectors.toMap(Branch::getId, p -> p));
	}

	private Map<Long, State> getStateMap() {
		return stateRepository.findAll().stream().collect(Collectors.toMap(State::getId, p -> p));
	}

	private Map<Long, City> getCityMap() {
		return cityRepository.findAll().stream().collect(Collectors.toMap(City::getId, p -> p));
	}

}
