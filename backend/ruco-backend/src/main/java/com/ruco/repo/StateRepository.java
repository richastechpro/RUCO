package com.ruco.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ruco.model.State;
import com.ruco.projection.StateCityProjection;

public interface StateRepository extends JpaRepository<State, Long> {

	List<State> findAllByIsDeleteFalseOrderByStateNameAsc();

	Optional<State> findByStateName(String stateName);

	@Query(value = "select s.id stateid, s.state_name statename, c.id cityid, c.city_name cityname from m_city c inner join m_state s on s.id = c.state_id order by s.state_name", nativeQuery = true)
	List<StateCityProjection> getAllValidStateCity();

	Page<State> findAll(Specification<State> stateHasSearchText, Pageable pageable);

}
