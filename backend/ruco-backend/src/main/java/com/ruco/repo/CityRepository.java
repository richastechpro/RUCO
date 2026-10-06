package com.ruco.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ruco.model.City;

public interface CityRepository extends JpaRepository<City, Long> {

	List<City> findAllByIsDeleteFalse();

	Optional<City> findByCityName(String cityName);

	Page<City> findAll(Specification<City> cityHasSearchText, Pageable pageable);

}
