package com.ruco.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruco.constant.SearchBuilder;
import com.ruco.model.Customer;
import com.ruco.repo.CustomerRepository;
import com.ruco.request.CustomerRequest;
import com.ruco.request.SearchRequest;
import com.ruco.response.CustomerResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

	public List<Customer> getValid() {
		return customerRepository.findAllByIsDeleteFalse();
	}

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		String searchTxt = request.getSearchTxt();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by("customerName"));
		Page<Customer> page = customerRepository.findAll(SearchBuilder.customerHasSearchText(searchTxt), pageable);
		return page.map(this::mapData);
	}

	private CustomerResponse mapData(Customer customer) {
		CustomerResponse response = new CustomerResponse();
		response.setId(customer.getId());
		response.setCustomerName(customer.getCustomerName());
		response.setCustomerAddress(customer.getCustomerAddress());
		response.setCustomerType(customer.getCustomerType());
		response.setEmail(customer.getEmail());
		response.setMobileNo(customer.getMobileNo());
		return response;
	}

	public void create(CustomerRequest request) {
		Customer customer = new Customer();
		customer.setCustomerName(request.getCustomerName());
		customer.setCustomerAddress(request.getCustomerAddress());
		customer.setCustomerType(request.getCustomerType());
		customer.setEmail(request.getEmail());
		customer.setMobileNo(request.getMobileNo());
		customerRepository.save(customer);
	}

	public void update(CustomerRequest request) {
		Long id = request.getId();
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			customer.setCustomerName(request.getCustomerName());
			customer.setCustomerAddress(request.getCustomerAddress());
			customer.setCustomerType(request.getCustomerType());
			customer.setEmail(request.getEmail());
			customer.setMobileNo(request.getMobileNo());
			customerRepository.save(customer);
		}

	}

	public CustomerResponse get(Long id) {
		return customerRepository.findById(id).map(this::mapData).orElse(null);
	}

	public void delete(Long id) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			customer.setIsDelete(true);
			customerRepository.save(customer);
		}
	}

}
