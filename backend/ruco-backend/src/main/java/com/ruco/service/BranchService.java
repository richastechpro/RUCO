package com.ruco.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruco.config.CustomUserDetails;
import com.ruco.constant.FBOUserType;
import com.ruco.constant.SearchBuilder;
import com.ruco.constant.UserType;
import com.ruco.model.Area;
import com.ruco.model.Branch;
import com.ruco.model.City;
import com.ruco.model.Company;
import com.ruco.model.FBO;
import com.ruco.model.FBOBranch;
import com.ruco.model.State;
import com.ruco.model.Warehouse;
import com.ruco.repo.AreaRepository;
import com.ruco.repo.BranchRepository;
import com.ruco.repo.CityRepository;
import com.ruco.repo.CompanyRepository;
import com.ruco.repo.FboBranchRepository;
import com.ruco.repo.FboRepository;
import com.ruco.repo.InventoryInRepository;
import com.ruco.repo.StateRepository;
import com.ruco.repo.WarehouseRepository;
import com.ruco.request.BranchRequest;
import com.ruco.request.SearchRequest;
import com.ruco.response.AreaResponse;
import com.ruco.response.BranchResponse;
import com.ruco.response.CityResponse;
import com.ruco.response.StateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchService {

	private final CompanyRepository companyRepository;
	private final BranchRepository branchRepository;
	private final InventoryInRepository inventoryInRepository;
	private final WarehouseRepository warehouseRepository;
	private final StateRepository stateRepository;
	private final CityRepository cityRepository;
	private final AreaRepository areaRepository;
	private final UserService userService;
	private final FboRepository fboRepository;
	private final FboBranchRepository fboBranchRepository;

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		CustomUserDetails user = userService.getUser();
		Boolean isFboUser = user.getIsFboUser();
		String userType = user.getUserType();
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		String searchTxt = request.getSearchTxt();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "branchName"));
		if (isFboUser) {
			Long fboId = user.getFboId();
			if (userType.equals(FBOUserType.FBO_SUPERADMIN)) {
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					List<Long> branchIds = fbo.getFboBranches().stream().map(FBOBranch::getBranchId)
							.collect(Collectors.toList());
					Page<Branch> page = branchRepository
							.findAll(SearchBuilder.branchHasSearchText(searchTxt, branchIds), pageable);
					return page.map(this::mapData);
				}
			}

			else if (userType.equals(FBOUserType.FBO_STATE)) {
				Long userStateId = user.getStateId();
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					List<Long> branchIds = fbo.getFboBranches().stream().filter(
							fboBranch -> fboBranch.getStateId() != null && fboBranch.getStateId().equals(userStateId))
							.map(FBOBranch::getBranchId).collect(Collectors.toList());
					Page<Branch> page = branchRepository
							.findAll(SearchBuilder.branchHasSearchText(searchTxt, branchIds), pageable);
					return page.map(this::mapData);
				}
			}

			else if (userType.equals(FBOUserType.FBO_CITY)) {
				Long userStateId = user.getStateId();
				Long userCityId = user.getCityId();
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					List<Long> branchIds = fbo.getFboBranches().stream()
							.filter(fboBranch -> fboBranch.getStateId() != null
									&& fboBranch.getStateId().equals(userStateId) && fboBranch.getCityId() != null
									&& fboBranch.getCityId().equals(userCityId))
							.map(FBOBranch::getBranchId).collect(Collectors.toList());
					Page<Branch> page = branchRepository
							.findAll(SearchBuilder.branchHasSearchText(searchTxt, branchIds), pageable);
					return page.map(this::mapData);
				}
			}

			else if (userType.equals(FBOUserType.FBO_BRANCH)) {
				Long fboBranchId = user.getFboBranchId();
				Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
				if (fboBranchOptional.isPresent()) {
					FBOBranch fboBranch = fboBranchOptional.get();
					Long branchId = fboBranch.getBranchId();
					Page<Branch> page = branchRepository
							.findAll(SearchBuilder.branchHasSearchText(searchTxt, Arrays.asList(branchId)), pageable);
					return page.map(this::mapData);
				}
			}
		}

		else {

			if (userType.equals(UserType.MANAGER)) {
				Long branchId = user.getBranchId();
				Page<Branch> page = branchRepository
						.findAll(SearchBuilder.branchHasSearchText(searchTxt, Arrays.asList(branchId)), pageable);
				return page.map(this::mapData);
			}

			else if (userType.equals(UserType.PICKUPBOY)) {
				Long branchId = user.getBranchId();
				Page<Branch> page = branchRepository
						.findAll(SearchBuilder.branchHasSearchText(searchTxt, Arrays.asList(branchId)), pageable);
				return page.map(this::mapData);
			}

			else if (userType.equals(UserType.SUPERADMIN)) {
				Page<Branch> page = branchRepository.findAll(SearchBuilder.branchHasSearchText(searchTxt, null),
						pageable);
				return page.map(this::mapData);
			}
		}

		return null;
	}

	private BranchResponse mapData(Branch branch) {
		BranchResponse response = new BranchResponse();
		response.setId(branch.getId());
		response.setBranchName(branch.getBranchName());
		response.setBranchAddress(branch.getBranchAddress());
		response.setGstNo(branch.getGstNo());
		response.setPanNo(branch.getPanNo());
		Long stateId = branch.getStateId();
		if (stateId != null) {
			Optional<State> stateOptional = stateRepository.findById(stateId);
			if (stateOptional.isPresent()) {
				State state = stateOptional.get();
				String stateName = state.getStateName();
				response.setState(new StateResponse(stateId, stateName, null));
			}
		}
		Long cityId = branch.getCityId();
		if (cityId != null) {
			Optional<City> cityOptional = cityRepository.findById(cityId);
			if (cityOptional.isPresent()) {
				City city = cityOptional.get();
				String cityName = city.getCityName();
				response.setCity(new CityResponse(cityId, cityName, null));
			}
		}
		Long areaId = branch.getAreaId();
		if (areaId != null) {
			Optional<Area> areaOptional = areaRepository.findById(areaId);
			if (areaOptional.isPresent()) {
				Area area = areaOptional.get();
				String areaName = area.getAreaName();
				response.setArea(new AreaResponse(areaId, areaName));
			}
		}
		Long warehouseId = branch.getWarehouseId();
		if (warehouseId != null) {
			Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouseId);
			if (warehouseOptional.isPresent()) {
				Warehouse warehouse = warehouseOptional.get();
				String warehouseName = warehouse.getWarehouseName();
				String warehouseAddress = warehouse.getWarehouseAddress();
				response.setWarehouseName(warehouseName);
				response.setWarehouseAddress(warehouseAddress);
			}
		}
		return response;
	}

	public BranchResponse get(Long id) {
		Optional<Branch> branchOptional = branchRepository.findById(id);
		return branchOptional.map(this::mapData).orElse(null);
	}

	public void delete(Long id) {
		Optional<Branch> branchOptional = branchRepository.findById(id);
		if (branchOptional.isPresent()) {
			Branch branch = branchOptional.get();
			branch.setIsDelete(true);
			Long warehouseId = branch.getWarehouseId();
			if (warehouseId != null) {
				Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouseId);
				if (warehouseOptional.isPresent()) {
					Warehouse warehouse = warehouseOptional.get();
					warehouse.setIsDelete(true);
					warehouseRepository.save(warehouse);
				}
			}
			branchRepository.save(branch);
		}
	}

	public void update(BranchRequest request) {
		Long id = request.getId();
		Optional<Branch> branchOptional = branchRepository.findById(id);
		if (branchOptional.isPresent()) {
			Branch branch = branchOptional.get();
			branch.setBranchName(request.getBranchName());
			branch.setBranchAddress(request.getBranchAddress());
			branch.setStateId(request.getStateId());
			branch.setCityId(request.getCityId());
			branch.setAreaId(request.getAreaId());
			branch.setGstNo(request.getGstNo());
			branch.setPanNo(request.getPanNo());
			Long warehouseId = branch.getWarehouseId();
			Warehouse warehouse = null;
			if (warehouseId != null) {
				warehouse = warehouseRepository.findById(warehouseId).orElse(new Warehouse());
			} else {
				warehouse = new Warehouse();
			}
			warehouse.setWarehouseName(request.getWarehouseName());
			warehouse.setWarehouseAddress(request.getWarehouseAddress());
			warehouse = warehouseRepository.save(warehouse);
			branch.setWarehouseId(warehouse.getId());
			branchRepository.save(branch);
		}
	}

	public List<BranchResponse> getValid() {
		List<Branch> branchs = branchRepository.findAllByIsDeleteFalse();
		List<BranchResponse> response = branchs.stream().map(b -> {
			BranchResponse br = new BranchResponse();
			br.setId(b.getId());
			br.setBranchName(b.getBranchName());
			Long warehouseId = b.getWarehouseId();
			Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouseId);
			if (warehouseOptional.isPresent()) {
				Warehouse warehouse = warehouseOptional.get();
				String warehouseName = warehouse.getWarehouseName();
				br.setWarehouseName(warehouseName);
			}
			return br;
		}).collect(Collectors.toList());
		return response;
	}

	public void create(BranchRequest request) {
		Long companyId = 1l;
		Optional<Company> companyOptional = companyRepository.findById(companyId);
		if (companyOptional.isPresent()) {
			Company company = companyOptional.get();

			Branch branch = new Branch();
			branch.setBranchName(request.getBranchName());
			branch.setBranchAddress(request.getBranchAddress());
			branch.setStateId(request.getStateId());
			branch.setCityId(request.getCityId());
			branch.setAreaId(request.getAreaId());
			branch.setGstNo(request.getGstNo());
			branch.setPanNo(request.getPanNo());
			branch.setCompany(company);

			Warehouse warehouse = new Warehouse();
			warehouse.setWarehouseName(request.getWarehouseName());
			warehouse.setWarehouseAddress(request.getWarehouseAddress());
			warehouse = warehouseRepository.save(warehouse);
			branch.setWarehouseId(warehouse.getId());
			branchRepository.save(branch);
		}
	}

	public Float getQty(Long id) {
		return inventoryInRepository.getQty(id).orElse(0f);
	}

	public List<BranchResponse> getValidByStateId(Long stateId) {
		List<Branch> branchs = branchRepository.findAllByIsDeleteFalseAndStateId(stateId);
		return branchs.stream().map(this::mapData).collect(Collectors.toList());
	}

}
