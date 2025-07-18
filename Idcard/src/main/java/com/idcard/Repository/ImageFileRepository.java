package com.idcard.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idcard.Model.ImageFileEntity;

public interface ImageFileRepository extends JpaRepository<ImageFileEntity, Long> {

	Optional<ImageFileEntity> findByIdAndFileType(long l, String flagType);

}
