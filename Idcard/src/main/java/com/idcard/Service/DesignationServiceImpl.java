package com.idcard.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcard.Model.DesignationEntity;
import com.idcard.Payload.DesignationDTO;
import com.idcard.Repository.DesignationRepository;

@Service
public class DesignationServiceImpl implements DesignationService{

	@Autowired
	private DesignationRepository designationRepository;
	
	@Override
	public List<DesignationDTO> getDesignationDetails() {
	    List<DesignationEntity> desigList = designationRepository.findAll();
	    
	    List<DesignationDTO> dtoList = desigList.stream().map(entity -> {
	        DesignationDTO dto = new DesignationDTO();
	        dto.setId(String.valueOf(entity.getId()));
	        dto.setName(entity.getName());
	        return dto;
	    }).collect(Collectors.toList());

	    return dtoList;
	}

	
	
	
}
