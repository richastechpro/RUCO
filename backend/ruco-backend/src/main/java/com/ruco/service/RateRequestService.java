package com.ruco.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruco.enums.TicketRequestStatus;
import com.ruco.model.Rate;
import com.ruco.model.RateRequest;
import com.ruco.repo.RateRepository;
import com.ruco.repo.RateRequestRepository;
import com.ruco.request.SearchRequest;
import com.ruco.response.RateRequestResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateRequestService {

	private final RateRequestRepository rateRequestRepository;
	private final RateRepository rateRepository;

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "saveDate"));
		Page<RateRequest> pages = rateRequestRepository.findAllByIsDeleteFalse(pageable);
		return pages.map(this::mapData);
	}

	private RateRequestResponse mapData(RateRequest rateRequest) {
		Long id = rateRequest.getId();
		Float oldPrice = rateRequest.getOldPrice();
		Float newPrice = rateRequest.getNewPrice();
		Long fboBranchId = rateRequest.getFboBranchId();
		String fboBranchName = rateRequest.getFboBranchName();
		Long userId = rateRequest.getUserId();
		String username = rateRequest.getUsername();
		TicketRequestStatus status = rateRequest.getStatus();
		RateRequestResponse r = new RateRequestResponse();
		r.setId(id);
		r.setOldPrice(oldPrice);
		r.setNewPrice(newPrice);
		r.setFboBranchId(fboBranchId);
		r.setFboBranchName(fboBranchName);
		r.setUserId(userId);
		r.setUsername(username);
		r.setStatus(status.getLabel());
		return r;
	}

	public void approve(Long id) {
		Optional<RateRequest> rateRequestOptional = rateRequestRepository.findById(id);
		if (rateRequestOptional.isPresent()) {
			RateRequest rateRequest = rateRequestOptional.get();
			Long fboBranchId = rateRequest.getFboBranchId();
			Optional<Rate> oldRateOptional = rateRepository.getRate(fboBranchId);
			if (oldRateOptional.isPresent()) {
				Rate oldRate = oldRateOptional.get();
				oldRate.setIsDelete(true);
				rateRepository.save(oldRate);
			}
			Float newPrice = rateRequest.getNewPrice();
			Rate newRate = new Rate();
			newRate.setFboBranchId(fboBranchId);
			newRate.setPrice(newPrice);
			rateRepository.save(newRate);
			rateRequest.setStatus(TicketRequestStatus.APPROVED);
			rateRequestRepository.save(rateRequest);
		}
	}

	public void reject(Long id) {
		Optional<RateRequest> rateRequestOptional = rateRequestRepository.findById(id);
		if (rateRequestOptional.isPresent()) {
			RateRequest rateRequest = rateRequestOptional.get();
			rateRequest.setStatus(TicketRequestStatus.REJECTED);
			rateRequestRepository.save(rateRequest);
		}
	}

}
