package com.idcard.Repository;

import com.idcard.Model.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<StateEntity, Long> {
	Optional<StateEntity> findByStateCode(String stateCode);
}
