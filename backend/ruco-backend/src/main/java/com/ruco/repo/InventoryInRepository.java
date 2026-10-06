package com.ruco.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ruco.model.InventoryIn;

public interface InventoryInRepository extends JpaRepository<InventoryIn, Long> {

	List<InventoryIn> findAllByIsDeleteFalse();

	Page<InventoryIn> findByIsDeleteFalse(Pageable pageable);

	@Query(value = "select sum(inward_qty) from t_inventory_in where branch_id = ?1 and is_delete = 0", nativeQuery = true)
	Optional<Float> getQty(Long branchId);

	List<InventoryIn> findAllByIsDeleteFalseAndSaveDateBetween(LocalDateTime fromDate, LocalDateTime toDate);

	@Query(value = "select sum(inward_qty) from t_inventory_in where branch_id = ?1 and is_delete = 0", nativeQuery = true)
	Optional<Float> getCollectedQtyCountByBranchId(Long branchId);

	@Query(value = "select sum(inward_qty) from t_inventory_in where is_delete = 0", nativeQuery = true)
	Optional<Float> getTotalCollectedQtyCount();

	@Query(value = "select sum(inward_qty) from t_inventory_in where branch_id in (?1) and is_delete = 0", nativeQuery = true)
	Optional<Float> getCollectedQtyCountByBranchIds(List<Long> branchIds);

	List<InventoryIn> findAllByBranchIdAndIsDeleteFalse(Long branchId);

	@Modifying
	@Transactional
	@Query(value = "update t_inventory_in set is_delete = 0 where id in (?1)", nativeQuery = true)
	void resetStatus(List<Long> inventoryInIds);

}
