package com.idcard.Payload;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class ImageFileDTO {
    private String id;
    private boolean status;
    private String fileName;
    private String fileType;
    private MultipartFile fileData;
    private String createDate;

}
