package com.ruco.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ruco.model.FBOBranch;

public interface FboBranchRepository extends JpaRepository<FBOBranch, Long> {

	List<FBOBranch> findAllByIsDeleteFalse();

	@Query(value = "select * from m_fbo_branch where branch_id = ?1 and is_delete = 0", nativeQuery = true)
	List<FBOBranch> getFboBranchesByBranchId(Long branchId);

	@Query(value = "select * from m_fbo_branch where id in (?1) and is_delete = 0", nativeQuery = true)
	List<Long> getBranchIds(List<Long> fboBranchIds);

	@Query(value = "select id from m_fbo_branch where branch_id = ?1 and is_delete = 0", nativeQuery = true)
	List<Long> getFboBranchIdsByBranchId(Long branchId);

	@Query(value = "select * from m_fbo_branch where branch_id = ?1 and is_delete = 0", nativeQuery = true)
	List<FBOBranch> getFboBranchsBranchId(Long branchId);

}
