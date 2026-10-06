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
import com.ruco.model.FBO;
import com.ruco.model.FBOBranch;
import com.ruco.model.FBOBranchLogin;
import com.ruco.model.Rate;
import com.ruco.model.State;
import com.ruco.repo.AreaRepository;
import com.ruco.repo.BranchRepository;
import com.ruco.repo.CityRepository;
import com.ruco.repo.FboBranchRepository;
import com.ruco.repo.FboRepository;
import com.ruco.repo.RateRepository;
import com.ruco.repo.StateRepository;
import com.ruco.request.FBORequest;
import com.ruco.request.SearchRequest;
import com.ruco.response.BranchResponse;
import com.ruco.response.CityResponse;
import com.ruco.response.FBOResponse;
import com.ruco.response.FboBranchLoginResponse;
import com.ruco.response.FboBranchResponse;
import com.ruco.response.StateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FboService {

	private final StateRepository stateRepository;
	private final CityRepository cityRepository;
	private final AreaRepository areaRepository;
	private final FboRepository fboRepository;
	private final FboBranchRepository fboBranchRepository;
	private final BranchRepository branchRepository;
	private final UserService userService;
	private final RateRepository rateRepository;

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		CustomUserDetails user = userService.getUser();
		Boolean isFboUser = user.getIsFboUser();
		String userType = user.getUserType();
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		String searchTxt = request.getSearchTxt();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "fboName"));

		if (isFboUser) {
			if (userType.equals(FBOUserType.FBO_SUPERADMIN)) {
				Long fboId = user.getFboId();
				Page<FBO> page = fboRepository.findAll(SearchBuilder.fboHasSearchText(searchTxt, Arrays.asList(fboId)),
						pageable);
				return page.map(this::mapData);
			}

			else if (userType.equals(FBOUserType.FBO_STATE)) {
				Long fboId = user.getFboId();
				Long stateId = user.getStateId();
				Page<FBO> page = fboRepository.findAll(SearchBuilder.fboHasSearchText(searchTxt, Arrays.asList(fboId)),
						pageable);
				return page.map(p -> {
					FBOResponse response = mapData(p);
					List<FBOBranch> filteredBranches = p.getFboBranches().stream().filter(
							fboBranch -> fboBranch.getStateId() != null && fboBranch.getStateId().equals(stateId))
							.collect(Collectors.toList());
					response.setFboBranchesCount(filteredBranches.size());
					return response;
				});
			}

			else if (userType.equals(FBOUserType.FBO_CITY)) {
				Long fboId = user.getFboId();
				Long stateId = user.getStateId();
				Long cityId = user.getCityId();
				Page<FBO> page = fboRepository.findAll(SearchBuilder.fboHasSearchText(searchTxt, Arrays.asList(fboId)),
						pageable);
				return page.map(p -> {
					FBOResponse response = mapData(p);
					List<FBOBranch> filteredBranches = p.getFboBranches().stream()
							.filter(fboBranch -> fboBranch.getStateId() != null
									&& fboBranch.getStateId().equals(stateId) && fboBranch.getCityId() != null
									&& fboBranch.getCityId().equals(cityId))
							.collect(Collectors.toList());
					response.setFboBranchesCount(filteredBranches.size());
					return response;
				});
			}

			else if (userType.equals(FBOUserType.FBO_BRANCH)) {
				Long fboId = user.getFboId();
				Long fboBranchId = user.getFboBranchId();
				Page<FBO> page = fboRepository.findAll(SearchBuilder.fboHasSearchText(searchTxt, Arrays.asList(fboId)),
						pageable);
				return page.map(p -> {
					FBOResponse response = mapData(p);
					response.setFboBranchesCount(fboBranchRepository.findById(fboBranchId).isPresent() ? 1 : 0);
					return response;
				});
			}
		}

		else {
			if (userType.equals(UserType.MANAGER)) {
				Long branchId = user.getBranchId();
				List<Long> fboBranchIds = fboBranchRepository.getFboBranchIdsByBranchId(branchId);
				Page<FBO> page = fboRepository.findAll(SearchBuilder.fboHasSearchText(searchTxt, fboBranchIds),
						pageable);
				return page.map(p -> {
					FBOResponse response = mapData(p);
					List<FBOBranch> fboBranches = p.getFboBranches();
					List<FBOBranch> filteredBranches = fboBranches.stream()
							.filter(b -> fboBranchIds.contains(b.getId())).collect(Collectors.toList());
					response.setFboBranchesCount(filteredBranches.size());
					return response;
				});
			}

			else if (userType.equals(UserType.SUPERADMIN)) {
				Page<FBO> page = fboRepository.findAll(SearchBuilder.fboHasSearchText(searchTxt, null), pageable);
				return page.map(this::mapData);
			}
		}
		return null;
	}

	private FBOResponse mapData(FBO fbo) {
		FBOResponse response = new FBOResponse();
		response.setId(fbo.getId());
		response.setFboName(fbo.getFboName());
		response.setBankName(fbo.getBankName());
		response.setAccountType(fbo.getAccountType());
		response.setAccountNo(fbo.getAccountNo());
		response.setAccountHolderName(fbo.getAccountHolderName());
		response.setIfscCode(fbo.getIfscCode());
		response.setMobileNo(fbo.getMobileNo());
		response.setFboBranchesCount(fbo.getFboBranches().size());
		response.setFboUsersCount(fbo.getFboBranchLogins().size());
		return response;
	}

	public void delete(Long id) {
		Optional<FBO> fboOptional = fboRepository.findById(id);
		if (fboOptional.isPresent()) {
			FBO fbo = fboOptional.get();
			fbo.setIsDelete(true);
			List<FBOBranch> branches = fbo.getFboBranches();
			branches.forEach(branch -> branch.setIsDelete(true));
			branches = fboBranchRepository.saveAll(branches);
			fbo.setFboBranches(branches);
			fboRepository.save(fbo);
		}
	}

	public FBOResponse get(Long fboId) {
		Optional<FBO> fboOptional = fboRepository.findById(fboId);
		return fboOptional.map(fbo -> mapData(fboId, fbo)).orElse(null);
	}

	private FBOResponse mapData(Long fboId, FBO fbo) {
		String fboName = fbo.getFboName();
		FBOResponse response = new FBOResponse();
		response.setId(fboId);
		response.setFboName(fboName);
		response.setBankName(fbo.getBankName());
		response.setAccountType(fbo.getAccountType());
		response.setAccountNo(fbo.getAccountNo());
		response.setAccountHolderName(fbo.getAccountHolderName());
		response.setIfscCode(fbo.getIfscCode());
		response.setMobileNo(fbo.getMobileNo());

		CustomUserDetails user = userService.getUser();
		Boolean isFboUser = user.getIsFboUser();
		String userType = user.getUserType();

		if (isFboUser) {
			if (userType.equals(FBOUserType.FBO_SUPERADMIN)) {
				List<FBOBranch> fboBranches = fbo.getFboBranches();
				List<FboBranchResponse> fboBranchesResponse = mapData(fboBranches);
				response.setFboBranches(fboBranchesResponse);
			}

			else if (userType.equals(FBOUserType.FBO_STATE)) {
				Long stateId = user.getStateId();
				List<FBOBranch> fboBranches = fbo.getFboBranches().stream()
						.filter(fboBranch -> fboBranch.getStateId() != null && fboBranch.getStateId().equals(stateId))
						.collect(Collectors.toList());
				List<FboBranchResponse> fboBranchesResponse = mapData(fboBranches);
				response.setFboBranches(fboBranchesResponse);
			}

			else if (userType.equals(FBOUserType.FBO_CITY)) {
				Long stateId = user.getStateId();
				Long cityId = user.getCityId();
				List<FBOBranch> fboBranches = fbo.getFboBranches().stream()
						.filter(fboBranch -> fboBranch.getStateId() != null && fboBranch.getStateId().equals(stateId)
								&& fboBranch.getCityId() != null && fboBranch.getCityId().equals(cityId))
						.collect(Collectors.toList());
				List<FboBranchResponse> fboBranchesResponse = mapData(fboBranches);
				response.setFboBranches(fboBranchesResponse);
			}

			else if (userType.equals(FBOUserType.FBO_BRANCH)) {
				Long fboBranchId = user.getFboBranchId();
				Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
				if (fboBranchOptional.isPresent()) {
					FBOBranch fboBranch = fboBranchOptional.get();
					List<FBOBranch> fboBranches = Arrays.asList(fboBranch);
					List<FboBranchResponse> fboBranchesResponse = mapData(fboBranches);
					response.setFboBranches(fboBranchesResponse);
				}
			}
		}

		else {
			if (userType.equals(UserType.MANAGER)) {
				Long branchId = user.getBranchId();
				List<FBOBranch> fboBranchs = fboBranchRepository.getFboBranchesByBranchId(branchId);
				List<FboBranchResponse> fboBranchesResponse = mapData(fboBranchs);
				response.setFboBranches(fboBranchesResponse);
			}

			else if (userType.equals(UserType.SUPERADMIN)) {
				List<FBOBranch> fboBranchs = fbo.getFboBranches();
				List<FboBranchResponse> fboBranchesResponse = mapData(fboBranchs);
				response.setFboBranches(fboBranchesResponse);
			}
		}

		return response;

	}

	private List<FboBranchResponse> mapData(List<FBOBranch> fboBranches) {
		List<FboBranchResponse> fboBranchs = fboBranches.stream().map(b -> {
			FboBranchResponse response = new FboBranchResponse();
			Long stateId = b.getStateId();
			if (stateId != null) {
				Optional<State> stateOptional = stateRepository.findById(stateId);
				if (stateOptional.isPresent()) {
					State state = stateOptional.get();
					String stateName = state.getStateName();
					response.setStateName(stateName);
					Long cityId = b.getCityId();
					if (cityId != null) {
						Optional<City> cityOptional = cityRepository.findById(cityId);
						if (cityOptional.isPresent()) {
							City city = cityOptional.get();
							String cityName = city.getCityName();
							response.setCityName(cityName);
							Long areaId = b.getAreaId();
							if (areaId != null) {
								Optional<Area> areaOptional = areaRepository.findById(areaId);
								if (areaOptional.isPresent()) {
									Area area = areaOptional.get();
									String areaName = area.getAreaName();
									response.setAreaName(areaName);
								}
							}
						}
					}
				}
				Long branchId = b.getBranchId();
				Optional<Branch> branchOptional = branchRepository.findById(branchId);
				if (branchOptional.isPresent()) {
					Branch branch = branchOptional.get();
					BranchResponse branchResponse = mapData(branch);
					response.setBranchName(branchResponse.getBranchName());
				}
			}
			response.setId(b.getId());
			response.setFboBranchName(b.getFboBranchName());
			response.setFboBranchAddress(b.getFboBranchAddress());
			response.setRepresentative(b.getRepresentative());
			response.setEmail(b.getEmail());
			response.setMobileNo(b.getMobileNo());
			response.setGstNo(b.getGstNo());
			Long fboBranchId = b.getId();
			Optional<Rate> rateOptional = rateRepository.getRate(fboBranchId);
			if(rateOptional.isPresent()) {
				Rate rate = rateOptional.get();
				response.setRate(rate);
			}
			return response;
		}).collect(Collectors.toList());

		return fboBranchs;
	}

	private BranchResponse mapData(Branch branch) {
		BranchResponse response = new BranchResponse();
		response.setId(branch.getId());
		response.setBranchName(branch.getBranchName());
		response.setBranchAddress(branch.getBranchAddress());
		response.setGstNo(branch.getGstNo());
		response.setPanNo(branch.getPanNo());
		return response;
	}

	public void update(FBO data) {
		List<FBOBranch> fboBranches = data.getFboBranches();
		if (fboBranches != null && fboBranches.size() > 0) {
			fboBranches = fboBranches.stream().map(fboBranchRepository::save).collect(Collectors.toList());
			data.setFboBranches(fboBranches);
		}
		fboRepository.save(data);
	}

	public void create(FBORequest request) {
		FBO fbo = new FBO();
		fbo.setFboName(request.getFboName());
		fbo.setBankName(request.getBankName());
		fbo.setAccountType(request.getAccountType());
		fbo.setAccountNo(request.getAccountNo());
		fbo.setAccountHolderName(request.getAccountHolderName());
		fbo.setIfscCode(request.getIfscCode());
		fbo.setMobileNo(request.getMobileNo());
		fboRepository.save(fbo);
	}

	public List<FboBranchLoginResponse> getFboLogins(Long fboId) {
		Optional<FBO> fboOptional = fboRepository.findById(fboId);
		if (fboOptional.isPresent()) {
			FBO fbo = fboOptional.get();
			List<FBOBranchLogin> fboBranchLogins = fbo.getFboBranchLogins();
			return fboBranchLogins.stream().map(l -> {
				FboBranchLoginResponse response = new FboBranchLoginResponse();
				response.setId(l.getId());
				response.setUsername(l.getUsername());
				response.setEmail(l.getEmail());
				response.setMobileNo(l.getMobileNo());
				response.setType(l.getType());
				Long stateId = l.getStateId();
				if (stateId != null) {
					Optional<State> stateOptional = stateRepository.findById(stateId);
					if (stateOptional.isPresent()) {
						State state = stateOptional.get();
						StateResponse stateResponse = new StateResponse();
						stateResponse.setStateId(state.getId());
						stateResponse.setStateName(state.getStateName());
						response.setState(stateResponse);
					}
				}
				Long cityId = l.getCityId();
				if (cityId != null) {
					Optional<City> cityOptional = cityRepository.findById(cityId);
					if (cityOptional.isPresent()) {
						City city = cityOptional.get();
						CityResponse cityResponse = new CityResponse();
						cityResponse.setCityId(city.getId());
						cityResponse.setCityName(city.getCityName());
						response.setCity(cityResponse);
					}
				}
				Long fboBranchId = l.getFboBranchId();
				if (fboBranchId != null) {
					Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
					if (fboBranchOptional.isPresent()) {
						FBOBranch fboBranch = fboBranchOptional.get();
						FboBranchResponse fboBranchResponse = new FboBranchResponse();
						fboBranchResponse.setId(fboBranch.getId());
						fboBranchResponse.setFboBranchName(fboBranch.getFboBranchName());
						response.setFboBranch(fboBranchResponse);
					}
				}
				return response;
			}).collect(Collectors.toList());
		}
		return null;
	}

	public String getFboName(Long fboId) {
		return fboRepository.findById(fboId).map(FBO::getFboName).orElse("");
	}

}
