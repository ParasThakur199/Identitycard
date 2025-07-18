package com.idcard.Service;

import java.util.List;

import com.idcard.Payload.ImageFileDTO;
import com.idcard.Payload.ImageFileGetDTO;

public interface ImageFileService {

	public String saveImageFile(String tokenHeader, List<ImageFileDTO> dtoList);

	public List<ImageFileGetDTO> getAllImageFile();

	public ImageFileGetDTO getImageByIdAndFlagType(String id, String flagType);
	
}
