package com.idcard.Repository;

import com.idcard.Model.TransactionEntity;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;



public interface TransactionRepository extends JpaRepository<TransactionEntity,String> {
//	Optional<TransactionEntity> findTopByStateCodeAndDateYearOrderByIdDesc(String stateCode, String year);
//	@Query("SELECT t FROM TransactionEntity t WHERE t.stateCode = :stateCode AND FUNCTION('YEAR', t.date) = :year ORDER BY t.id DESC")
//	Optional<TransactionEntity> findTopByStateCodeAndYear(@Param("stateCode") String stateCode, @Param("year") String year);

	@Query("Select e from TransactionEntity e where e.year = ?1 and e.stateCode = ?2")
	Optional<TransactionEntity> findByScodeAndYear(String currentYearDate, String stateCode);

}
