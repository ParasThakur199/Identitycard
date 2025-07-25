package com.idcard.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idcard.Model.ImageFileEntity;

public interface ImageFileRepository extends JpaRepository<ImageFileEntity, Long> {

	Optional<ImageFileEntity> findByIdAndFileType(long l, String flagType);

	Optional<ImageFileEntity> findById(Long id);


	@Query("select e from ImageFileEntity e where e.status = ?1")
	List<ImageFileEntity> findByStatus(boolean b);

}
