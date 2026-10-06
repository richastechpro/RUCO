package com.ruco.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ruco.model.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {

	List<Area> findAllByIsDeleteFalse();

	Page<Area> findAll(Specification<Area> areaHasSearchText, Pageable pageable);

}
