package com.idcard.Model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_image")
@Data
public class ImageFileEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;
    
    @Column(name = "status")
    private boolean status;

    @Column(name = "file_data")
    @Lob
    private byte[] fileData;

    @Column(name = "createdate")
    private Date createDate;
    
    @Column(name = "createuser")
    private String createUser;
    
    @Column(name = "updatedate")
    private Date updateDate;
    
    @Column(name = "updateuser")
    private String updateUser;

}
