package com.idcard.Payload;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class IdcardDTO {
	private Long id;
    @NotBlank(message = "Card number is required")
    @Size(max = 5, message = "Name must be at most 5 characters")
    private String cardno; 
    private String stateCode; 
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;
    @NotBlank(message = "Designation is required")
    @Size(max = 50, message = "Designation must be at most 50 characters")
    private String designation;
    @NotBlank(message = "Employee code is required")
    @Size(max = 10, message = "Employee code must be at most 10 characters")
    private String empcode;

    @NotBlank(message = "Posting place is required")
    @Size(max = 50, message = "Posting Place must be at most 50 characters")
    private String postingplace;

//    @NotBlank(message = "Issue date is required")
//    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Issue date must be in the format yyyy-MM-dd")
//    private String issuedate;

    @NotBlank(message = "Valid upto date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Valid upto must be in the format yyyy-MM-dd")
    private String validupto;

    @NotBlank(message = "Date of birth is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date of birth must be in the format yyyy-MM-dd")
    private String dob;

    @NotBlank(message = "Identification mark is required")
    @Size(max = 50, message = "Identification mark must be at most 50 characters")
    private String identificationmark;

    @NotBlank(message = "Emergency contact name is required")
    @Pattern(regexp = "\\d{20}", message = "Emergency contact name must be a 20 character")
    private String emergencycontactname;

    @NotBlank(message = "Emergency contact number is required")
    @Pattern(regexp = "\\d{10}", message = "Emergency contact number must be a 10-digit number")
    private String emergencycontactno;

    @NotBlank(message = "Address is required")
    @Size(max = 50, message = "Address must be at most 50 characters")
    private String address;

    @NotBlank(message = "Height is required")
    @Size(max = 3, message = "Height must be at most 3 characters")
    private String height;

    @NotBlank(message = "Blood group is required")
    @Size(max = 3, message = "Blood Group must be at most 3 characters")
    private String bloodgroup;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be a 10-digit number")
    private String mobile;

    @NotBlank(message = "Office address is required")
    @Size(max = 50, message = "Office address must be at most 50 characters")
    private String officeaddress;

//    @NotBlank(message = "Branch is required")
    @Size(max = 50, message = "Branch must be at most 50 characters")
    private String branch;

//    @NotBlank(message = "Department is required")
    @Size(max = 50, message = "Department must be at most 50 characters")
    private String department;
    
//    @Size(max = 50, message = "Remarks must be at most 50 characters")
    private String remarks;
    
    @Size(max = 1, message = "Status must be at most 1 characters")
    private String cardStatus;
    
    private String language;

    @NotNull(message = "Photo is required")
    private MultipartFile photo;

    @NotNull(message = "Signature 1 is required")
    private MultipartFile signature1;

    @NotNull(message = "Signature 2 is required")
    private MultipartFile signature2;

    @NotNull(message = "Logo is required")
    private MultipartFile logo;

    @NotNull(message = "Watermark is required")
    private MultipartFile watermark;	    
	    // Getters and setters
    private String createUser;
    
}
