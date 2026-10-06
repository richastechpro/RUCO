package com.ruco.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruco.model.FBOBranch;
import com.ruco.model.Rate;
import com.ruco.repo.FboBranchRepository;
import com.ruco.repo.RateRepository;
import com.ruco.request.SearchRequest;
import com.ruco.response.RateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatesService {

	private final RateRepository rateRepository;
	private final FboBranchRepository fboBranchRepository;

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "saveDate"));
		Page<Rate> pages = rateRepository.findAllByIsDeleteFalse(pageable);
		return pages.map(this::mapData);
	}

	private RateResponse mapData(Rate rate) {
		Long id = rate.getId();
		Long fboBranchId = rate.getFboBranchId();
		RateResponse r = new RateResponse();
		r.setId(id);
		r.setPrice(rate.getPrice());
		Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
		if (fboBranchOptional.isPresent()) {
			FBOBranch fboBranch = fboBranchOptional.get();
			String fboBranchName = fboBranch.getFboBranchName();
			r.setFboBranchName(fboBranchName);
		}
		return r;
	}

}
