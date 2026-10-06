package com.ruco.service.audit;

import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "100"))
	@Query("SELECT a FROM AuditLog a WHERE a.userId = :userId")
	Stream<AuditLog> streamByUserId(@Param("userId") Long userId);

}
