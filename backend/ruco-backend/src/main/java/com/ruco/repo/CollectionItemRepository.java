package com.ruco.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ruco.model.CollectionItem;

public interface CollectionItemRepository extends JpaRepository<CollectionItem, Long> {

	@Query(value = "select certificate_no from (\n"
			+ "    select concat('bs/', left(monthname(?1), 1), count(id), '/', date_format(?1, '%y')) as certificate_no \n"
			+ "    from t_request where isdelete = 0 and collected_status = 'Collected' and month(collected_on) = month(?1) \n"
			+ ") as tbl1", nativeQuery = true)
	String getCertificateNo(String formatteddate);

	@Query(value = "select r.price from t_collection_item ci inner join m_rate r on r.id = ci.rate_id where ci.id = ?1", nativeQuery = true)
	Optional<Float> getRate(Long collectionItemId);

}
