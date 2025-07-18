package com.idcard.Repository;

import com.idcard.Model.IdcardEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface IdCardRepository extends JpaRepository<IdcardEntity, Long>{

}
