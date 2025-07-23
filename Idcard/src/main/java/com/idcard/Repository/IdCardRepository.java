package com.idcard.Repository;

import com.idcard.Model.IdcardEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface IdCardRepository extends JpaRepository<IdcardEntity, Long>{

	@Query("Select e from IdcardEntity e where e.cardStatus = ?1")
	List<IdcardEntity> findAllByCardStatus(String cardStatus);

	

}
