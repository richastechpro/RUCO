package com.ruco.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ruco.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findAllByIsDeleteFalse();

	Page<Ticket> findAllByIsDeleteFalse(Pageable pageable);

	@Query(value = "select count(*) from m_ticket where status = 'OPEN' and is_delete = 0", nativeQuery = true)
	Optional<Integer> getOpenTicketCount();

}
