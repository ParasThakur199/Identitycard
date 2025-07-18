package com.idcard.Service;

import org.springframework.core.io.Resource;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idcard.Model.IdcardEntity;
import com.idcard.Payload.IdcardDTO;
import com.idcard.Repository.IdCardRepository;

public interface FileService {
	Resource getFileByIdAndType(Long id, String fileType);
	
	// added
	byte[] generatepdf(Long idc);
	// changed
//	byte[] generatepdf(IdcardEntity entity);
//	public List<IdcardDTO> getAllIdcards() {
//	    List<IdcardEntity> entities = Idcardrepository.findAll();
//	    return entities.stream()
//	        .map(entity -> modelMapper.map(entity, IdcardDTO.class)) // or manual mapping
//	        .collect(Collectors.toList());
//	}
}
