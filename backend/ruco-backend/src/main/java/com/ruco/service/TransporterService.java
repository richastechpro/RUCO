package com.ruco.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruco.config.CustomUserDetails;
import com.ruco.constant.SearchBuilder;
import com.ruco.constant.UserType;
import com.ruco.model.Branch;
import com.ruco.model.Collection;
import com.ruco.model.Transporter;
import com.ruco.repo.BranchRepository;
import com.ruco.repo.CollectionRepository;
import com.ruco.repo.TransporterRepository;
import com.ruco.request.SearchRequest;
import com.ruco.response.TransporterResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransporterService {

	private final TransporterRepository transporterRepository;
	private final BranchRepository branchRepository;
	private final UserService userService;
	private final CollectionRepository collectionRepository;

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		CustomUserDetails user = userService.getUser();
		String userType = user.getUserType();
		Long branchId = user.getBranchId();
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		String searchTxt = request.getSearchTxt();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "transporterName"));
		if (userType.equals(UserType.SUPERADMIN)) {
			Page<Transporter> page = transporterRepository
					.findAll(SearchBuilder.transporterHasSearchText(searchTxt, null), pageable);
			return page.map(this::mapData);
		}

		else {
			Page<Transporter> page = transporterRepository
					.findAll(SearchBuilder.transporterHasSearchText(searchTxt, branchId), pageable);
			return page.map(this::mapData);
		}

	}

	private TransporterResponse mapData(Transporter transporter) {
		TransporterResponse response = new TransporterResponse();
		response.setId(transporter.getId());
		response.setContactPerson(transporter.getContactPerson());
		response.setTransporterName(transporter.getTransporterName());
		response.setTransporterAddress(transporter.getTransporterAddress());
		response.setStateId(transporter.getStateId());
		response.setCityId(transporter.getCityId());
		response.setAreaId(transporter.getAreaId());
		response.setMobileNo(transporter.getMobileNo());
		response.setVehicleNo(transporter.getVehicleNo());
		response.setLicenseNo(transporter.getLicenseNo());
		response.setGstNo(transporter.getGstNo());
		response.setPanNo(transporter.getPanNo());
		response.setContractDate(transporter.getContractDate());
		response.setRatePerKg(transporter.getRatePerKg());
		response.setCollectedQty(transporter.getCollectedQty());
		Long branchId = transporter.getBranchId();
		if (branchId != null) {
			Optional<Branch> branchOptional = branchRepository.findById(branchId);
			if (branchOptional.isPresent()) {
				Branch branch = branchOptional.get();
				String branchName = branch.getBranchName();
				response.setBranchId(branchId);
				response.setBranchName(branchName);
			}
		}
		return response;
	}

	public void delete(Long id) {
		Optional<Transporter> transporterOptional = transporterRepository.findById(id);
		if (transporterOptional.isPresent()) {
			Transporter transporter = transporterOptional.get();
			transporter.setIsDelete(true);
			transporterRepository.save(transporter);
		}
	}

	public Transporter get(Long id) {
		return transporterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area not found"));
	}

	public void update(Transporter transporter) {
		Long stateId = transporter.getStateId();
		Long cityId = transporter.getCityId();
		Long areaId = transporter.getAreaId();
		Long branchId = transporter.getBranchId();

		transporter.setStateId(stateId);
		transporter.setCityId(cityId);
		transporter.setAreaId(areaId);
		transporter.setBranchId(branchId);

		transporterRepository.save(transporter);
	}

	public void create(Transporter transporter) {
		transporterRepository.save(transporter);
	}

	public List<Transporter> getValid(Long collectionId) {

		Optional<Collection> collectionOptional = collectionRepository.findById(collectionId);
		if (collectionOptional.isPresent()) {
			Collection collection = collectionOptional.get();
			Long branchId = collection.getBranchId();
			return transporterRepository.findAllByBranchIdAndIsDeleteFalse(branchId);
		}

		return null;

	}

}
