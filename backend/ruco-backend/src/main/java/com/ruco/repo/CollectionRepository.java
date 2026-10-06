package com.ruco.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ruco.model.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

	List<Collection> findAllByIsDeleteFalse();

	@Query(value = "select concat('Collection ', LPAD(COUNT(*) + 1, 5, '0')) from t_collection where type = 'BULK' and is_delete = 0", nativeQuery = true)
	Optional<String> getLatestCollectionName();

	Page<Collection> findByIsDeleteFalseAndIsAssignedTrue(Pageable pageable);

	Page<Collection> findAll(Specification<Collection> collectionHasSearchText, Pageable pageable);

	@Query(value = "select count(*) from t_collection where branch_id = ?1 and is_delete = 0", nativeQuery = true)
	Optional<Integer> getCollectionCount(Long branchId);

	@Query(value = "select count(*) from t_collection where branch_id = ?1 and is_assigned = 0 and is_delete = 0", nativeQuery = true)
	Optional<Integer> getNotAssignedCount(Long branchId);

	@Query(value = "select count(*) from (select count(c.id) from t_collection c inner join t_collection_item ci on ci.collection_id = c.id where c.branch_id = ?1 and c.is_assigned = 1 and ci.is_collected = 0 and c.is_delete = 0 and ci.is_delete = 0 group by c.id) cnt", nativeQuery = true)
	Optional<Integer> getNotCollectedCount(Long branchId);

	@Query(value = "select count(*) from t_collection where is_assigned = 0 and is_delete = 0", nativeQuery = true)
	Optional<Integer> getNotAssignedCount();

	@Query(value = "select count(*) from (select count(c.id) from t_collection c inner join t_collection_item i on i.collection_id = c.id where c.is_assigned = 1 and i.is_collected = 0 and c.is_delete = 0 and i.is_delete = 0 group by c.id) cnt", nativeQuery = true)
	Optional<Integer> getNotCollectedCount();

	@Query(value = "select count(*) from t_collection where branch_id = ?1 and (pickupboy_id1 = ?2 or pickupboy_id2 = ?2) and is_delete = 0", nativeQuery = true)
	Optional<Integer> getCollectionCount(Long branchId, Long userId);

	@Query(value = "select count(*) from (select count(c.id) from t_collection c inner join t_collection_item ci on ci.collection_id = c.id where c.branch_id = ?1 and (pickupboy_id1 = ?2 or pickupboy_id2 = ?2) and c.is_assigned = 1 and ci.is_collected = 0 and c.is_delete = 0 and ci.is_delete = 0 group by c.id) cnt", nativeQuery = true)
	Optional<Integer> getNotCollectedCount(Long branchId, Long userId);

	/* <------- FBO_SUPERADMIN -----------> */

	@Query(value = "select count(*) from (select count(c.id) from t_collection c inner join t_collection_item ci on ci.collection_id = c.id where c.is_delete = 0 and ci.is_delete = 0 and ci.fbo_branch_id in (?1) group by c.id) cnt", nativeQuery = true)
	Optional<Integer> getTotalCollections(List<Long> fboBranchIds);

	@Query(value = "select count(*) from (select count(c.id) from t_collection c inner join t_collection_item ci on ci.collection_id = c.id where c.is_delete = 0 and ci.is_delete = 0 and ci.fbo_branch_id in (?1) and c.is_assigned = 0 group by c.id) cnt", nativeQuery = true)
	Optional<Integer> getNotAssignedCount(List<Long> fboBranchIds);

	@Query(value = "select count(*) from (select count(c.id) from t_collection c inner join t_collection_item ci on ci.collection_id = c.id where c.is_delete = 0 and ci.is_delete = 0 and ci.fbo_branch_id in (?1) and c.is_assigned = 1 and ci.is_collected = 0 group by c.id) cnt", nativeQuery = true)
	Optional<Integer> getNotCollectedCount(List<Long> fboBranchIds);

}
