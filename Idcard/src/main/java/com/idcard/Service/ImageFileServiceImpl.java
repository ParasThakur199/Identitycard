package com.idcard.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcard.CommonData.Juleandate;
import com.idcard.Model.ImageFileEntity;
import com.idcard.Model.UserEntity;
import com.idcard.Payload.ImageFileDTO;
import com.idcard.Payload.ImageFileGetDTO;
import com.idcard.Payload.Login_Token_DTO;
import com.idcard.Repository.ImageFileRepository;
import com.idcard.config.JwtService;
import com.idcard.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class ImageFileServiceImpl implements ImageFileService {

	@Autowired
	private ImageFileRepository imageFileRepository;

	@Autowired
	private JwtService helper;

	@Override
	public String saveImageFile(String tokenHeader, List<ImageFileDTO> dtoList) {
		final UserEntity claims2 = helper.getUserDetailsFromToken(tokenHeader);
		String status = "0";
		List<ImageFileEntity> entities = dtoList.stream().map(dto -> {
			try {
				String id = dto.getId();
				if (id == null) {
					id = "0";
				}
				Optional<ImageFileEntity> byId = imageFileRepository.findById(Long.parseLong(id));
				if (byId.isPresent()) {
					ImageFileEntity entity = byId.get();
					entity.setFileName(dto.getFileName());
					entity.setFileType(dto.getFileType());
					entity.setStatus(dto.isStatus());
					if (dto.getFileData() != null) {
						entity.setFileData(dto.getFileData().getBytes());
					}
					entity.setUpdateDate(Juleandate.getCurrentDateTime());
					entity.setUpdateUser(claims2.getUserId());
					return entity;
				} else {
					ImageFileEntity entity = new ImageFileEntity();
					entity.setFileName(dto.getFileName());
					entity.setFileType(dto.getFileType());
					entity.setStatus(dto.isStatus());
					entity.setFileData(dto.getFileData().getBytes());
					entity.setCreateDate(Juleandate.getCurrentDateTime());
					entity.setCreateUser(claims2.getUserId());
					return entity;
				}
			} catch (Exception e) {
				throw new ResourceNotFoundException("Image not saved");
			}
		}).toList();

		imageFileRepository.saveAll(entities);
		status = "1";
		return status;
	}

	@Override
	public Map<String, List<ImageFileGetDTO>> getAllImageFile() {
		List<ImageFileEntity> findAllData = imageFileRepository.findAll();

		return findAllData.stream().map(entity -> {
			ImageFileGetDTO dto = new ImageFileGetDTO();
			dto.setId("" + entity.getId());
			dto.setFileName(entity.getFileName());
			dto.setStatus(entity.isStatus());
			dto.setFileType(entity.getFileType());
			dto.setFileData(entity.getFileData());
			dto.setCreateDate(entity.getCreateDate());
			return dto;
		}).collect(Collectors.groupingBy(ImageFileGetDTO::getFileType));
	}

	@Override
	public ImageFileGetDTO getImageByIdAndFlagType(String id, String flagType) {
		Optional<ImageFileEntity> optenty = imageFileRepository.findByIdAndFileType(Long.parseLong(id), flagType);
		ImageFileGetDTO dto = new ImageFileGetDTO();
		if (optenty.isPresent()) {
			ImageFileEntity entity = optenty.get();
			dto.setFileName(entity.getFileName());
			dto.setFileData(entity.getFileData());
		}
		return dto;
	}

	@Transactional
	@Override
	public List<ImageFileGetDTO> getallActiveStatus(String tokenHeader) {
		final UserEntity claims2 = helper.getUserDetailsFromToken(tokenHeader);
		List<ImageFileEntity> findAllData = imageFileRepository.findByStatus(true);

		return findAllData.stream().map(entity -> {
			ImageFileGetDTO dto = new ImageFileGetDTO();
			dto.setId("" + entity.getId());
			dto.setFileName(entity.getFileName());
			dto.setStatus(entity.isStatus());
			dto.setFileType(entity.getFileType());
			dto.setFileData(entity.getFileData());
			dto.setCreateDate(entity.getCreateDate());
			return dto;
		}).collect(Collectors.toList());
	}
}
