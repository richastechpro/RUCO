package com.ruco.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ruco.model.InventoryOut;
import com.ruco.projection.InventoryOutProjection;

public interface InventoryOutRepository extends JpaRepository<InventoryOut, Long> {

	List<InventoryOut> findAllByIsDeleteFalse();

	Page<InventoryOut> findAllByIsDeleteFalse(Pageable pageable);

	@Query(value = "select sum(qty) from t_inventory_out where branch_id = ?1 and is_delete = 0", nativeQuery = true)
	Optional<Float> getOutwardQtyByBranchId(Long branchId);

	@Query(value = "select sum(qty) from t_inventory_out where is_delete = 0", nativeQuery = true)
	Optional<Float> getTotalOutwardQty();

	List<InventoryOut> findAllByIsDeleteFalseAndSaveDateBetween(LocalDateTime fromDate, LocalDateTime toDate);

	@Query(value = "select branch_id branch, sum(qty) qty from t_inventory_out where is_delete = 0 group by branch_id", nativeQuery = true)
	List<InventoryOutProjection> getQtyByBranch();

	@Query(value = "select sum(qty) from t_inventory_out where branch_id in (?1) and is_delete = 0", nativeQuery = true)
	Optional<Float> getOutwardQtyByBranchIds(List<Long> branchIds);

}
