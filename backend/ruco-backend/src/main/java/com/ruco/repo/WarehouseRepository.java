package com.ruco.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ruco.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

	List<Warehouse> findAllByIsDeleteFalse();

	Page<Warehouse> findByIsDeleteFalse(Pageable pageable);

}
