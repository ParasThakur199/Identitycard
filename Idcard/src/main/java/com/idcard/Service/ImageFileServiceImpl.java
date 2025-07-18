package com.idcard.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcard.CommonData.Juleandate;
import com.idcard.Model.ImageFileEntity;
import com.idcard.Payload.ImageFileDTO;
import com.idcard.Payload.ImageFileGetDTO;
import com.idcard.Payload.Login_Token_DTO;
import com.idcard.Repository.ImageFileRepository;
import com.idcard.exception.ResourceNotFoundException;


@Service
public class ImageFileServiceImpl implements ImageFileService {

	@Autowired
	private ImageFileRepository imageFileRepository; 
	
//	@Autowired
//	private JwtHelper helper;
	
	@Override
	public String saveImageFile(String tokenHeader ,List<ImageFileDTO> dtoList) {
//		final Login_Token_DTO claims2 = helper.getAllClaimsFromToken_inDTO(tokenHeader);
		String status = "0";
		 List<ImageFileEntity> entities = dtoList.stream().map(dto -> {
		        try {
		            ImageFileEntity entity = new ImageFileEntity();
		            entity.setFileName(dto.getFileName());
		            entity.setFileType(dto.getFileType());
		            entity.setFileData(dto.getFileData().getBytes());
		            entity.setCreateDate(Juleandate.getCurrentDateTime());
		            entity.setCreateUser("system");
		            return entity;
		        } catch (Exception e) {
		            throw new ResourceNotFoundException("Image not saved");
		        }
		    }).toList();

		 imageFileRepository.saveAll(entities);
		 status = "1";
		return status;
	}

	@Override
	public List<ImageFileGetDTO> getAllImageFile() {
		List<ImageFileEntity> findAllData = imageFileRepository.findAll();
		 return findAllData.stream().map(entity -> {
		        ImageFileGetDTO dto = new ImageFileGetDTO();
		        dto.setId(""+entity.getId());
		        dto.setFileName(entity.getFileName());
		        dto.setFileType(entity.getFileType());
		        dto.setFileData(entity.getFileData());
		        dto.setCreateDate(entity.getCreateDate());
		        return dto;
		    }).collect(Collectors.toList());
		
	}

	@Override
	public ImageFileGetDTO getImageByIdAndFlagType(String id, String flagType) {
		Optional<ImageFileEntity> optenty = imageFileRepository.findByIdAndFileType(Long.parseLong(id),flagType);
		ImageFileGetDTO dto = new ImageFileGetDTO();
		if(optenty.isPresent()) {
			ImageFileEntity entity = optenty.get();
			dto.setFileName(entity.getFileName());
		}
		return dto;
	}

}
