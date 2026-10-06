package com.ruco.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ruco.model.Company;
import com.ruco.repo.CompanyRepository;
import com.ruco.request.CompanyRequest;
import com.ruco.response.AreaResponse;
import com.ruco.response.CityResponse;
import com.ruco.response.CompanyResponse;
import com.ruco.response.StateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

	private final LocationService locationService;
	private final CompanyRepository companyRepository;

	public Page<?> get(int limit, int offset) {
		Pageable pageable = PageRequest.of(offset, limit);
		Page<Company> page = companyRepository.findByIsDeleteFalse(pageable);
		return page.map(this::mapData);
	}
	
	private CompanyResponse mapData(Company company) {
		CompanyResponse response = new CompanyResponse();
		response.setId(company.getId());
		response.setCompanyName(company.getCompanyName());
		response.setCompanyAddress(company.getCompanyAddress());
		response.setContactPerson(company.getContactPerson());
		response.setMobileNo(company.getMobileNo());
		response.setEmailId(company.getEmailId());
		response.setPanNo(company.getPanNo());
		response.setGstNo(company.getGstNo());
		return response;
	}

	public void delete(Long id) {
		Optional<Company> companyOptional = companyRepository.findById(id);
		if (companyOptional.isPresent()) {
			Company company = companyOptional.get();
			company.setIsDelete(true);
			companyRepository.save(company);
		}
	}

	public CompanyResponse get(Long id) {
		Optional<Company> companyOptional = companyRepository.findById(id);
		if (companyOptional.isPresent()) {
			Company company = companyOptional.get();
			Long stateId = company.getStateId();
			Long cityId = company.getCityId();
			Long areaId = company.getAreaId();

			List<StateResponse> locations = locationService.getLocations();

			StateResponse selectedState = locations.stream().filter(state -> state.getStateId().equals(stateId))
					.findFirst().orElse(null);

			CityResponse selectedCity = selectedState != null
					? selectedState.getCities().stream().filter(city -> city.getCityId().equals(cityId)).findFirst()
							.orElse(null)
					: null;

			AreaResponse selectedArea = selectedCity != null
					? selectedCity.getAreas().stream().filter(area -> area.getAreaId().equals(areaId)).findFirst()
							.orElse(null)
					: null;

			if (selectedState != null) {
				selectedState.setCities(null);
			}

			if (selectedCity != null) {
				selectedCity.setAreas(null);
			}

			CompanyResponse response = new CompanyResponse();
			response.setCompanyName(company.getCompanyName());
			response.setCompanyAddress(company.getCompanyAddress());
			response.setState(selectedState);
			response.setCity(selectedCity);
			response.setArea(selectedArea);
			response.setContactPerson(company.getContactPerson());
			response.setMobileNo(company.getMobileNo());
			response.setEmailId(company.getEmailId());
			response.setPanNo(company.getPanNo());
			response.setGstNo(company.getGstNo());
			return response;

		}
		return null;
	}

	public void update(CompanyRequest request) {
		Long id = request.getId();
		Optional<Company> companyOptional = companyRepository.findById(id);
		if (companyOptional.isPresent()) {
			Company company = companyOptional.get();
			company.setCompanyName(request.getCompanyName());
			company.setCompanyAddress(request.getCompanyAddress());
			company.setStateId(request.getStateId());
			company.setCityId(request.getCityId());
			company.setAreaId(request.getAreaId());
			company.setContactPerson(request.getContactPerson());
			company.setMobileNo(request.getMobileNo());
			company.setEmailId(request.getEmailId());
			company.setPanNo(request.getPanNo());
			company.setGstNo(request.getGstNo());
			companyRepository.save(company);
		}
	}

	

}
