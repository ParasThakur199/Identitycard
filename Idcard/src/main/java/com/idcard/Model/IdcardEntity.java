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

//import java.util.Date;

@Entity
@Table(name = "tbl_carddetails")
@Data
public class IdcardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String transactionId;
    private String stateCode; 
    private String cardno;
    private String name;
    private String designation;
    private String empcode;
    private String postingplace;
	private Date issuedate;
    private String validupto;
    private String dob;
    private String identificationmark;
    private String emergencycontactname;
    private String emergencycontactno;
    private String address;
    private String height;
    private String bloodgroup;
    private String mobile;
    private String officeaddress;
    @Column(name="cardstatus",length = 1)
    private String cardStatus;
    
    @Column(name = "createuser", length = 50)
    private String createUser;
    private Date createDate;
    @Column(name = "changeuser", length = 50)
    private String changeUser;
    private Date changeDate;
    
    @Lob
    private byte[] photo;

    @Lob
    private byte[] signature1;

    @Lob
    private byte[] signature2;

    @Lob
    private byte[] logo;

    @Lob
    private byte[] watermark;
    
    // Getters and setters
    
}
