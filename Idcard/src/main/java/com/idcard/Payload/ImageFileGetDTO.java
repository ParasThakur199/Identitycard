package com.idcard.Payload;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ImageFileGetDTO {
	private String id;
	private String fileName;
	private String fileType;
	private byte[] fileData;
	private Date createDate;
    private boolean status;

}
