package com.ruco.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ruco.model.FBO;

public interface FboRepository extends JpaRepository<FBO, Long> {

	List<FBO> findAllByIsDeleteFalse();

	Page<FBO> findAll(Specification<FBO> fboHasSearchText, Pageable pageable);

	@Query(value = "select count(*) from (select f.id from m_fbo f inner join m_fbo_branch fb on fb.fbo_id = f.id where fb.branch_id = ?1 and f.is_delete = 0 and fb.is_delete = 0 group by f.id) cnt", nativeQuery = true)
	Optional<Integer> getFboCount(Long branchId);

	Optional<FBO> findByIdAndIsDeleteFalse(Long fboId);

}
