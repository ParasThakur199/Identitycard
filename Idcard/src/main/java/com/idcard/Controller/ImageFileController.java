package com.idcard.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idcard.Payload.FullSubmitRequest;
import com.idcard.Payload.ImageFileDTO;
import com.idcard.Payload.ImageFileGetDTO;
import com.idcard.Payload.Response_Status;
import com.idcard.Service.ImageFileService;

import lombok.Data;



@RestController
@CrossOrigin
@RequestMapping("/user")
public class ImageFileController {
	
	@Autowired
	private ImageFileService imageFileService;
	
	@Data
	public class ImageFileDTOListWrapper {
	    private List<ImageFileDTO> dtoList;
	}
	
	@PostMapping(value = "/addImageFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> imageFileHandler(@RequestHeader("Authorization") String tokenHeader,@ModelAttribute ImageFileDTOListWrapper wrapper) {
		Response_Status st = new Response_Status();
        try {
        	String saveImageFile = imageFileService.saveImageFile(tokenHeader,wrapper.getDtoList());
        	if(saveImageFile.equalsIgnoreCase("0")) {
        		st.setMessage("Error Data Not save");
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(st);
        	}else {
        		st.setMessage("Image File saved successfully.");
                return ResponseEntity.ok(st); 
        	}
        } catch (Exception e) {
        	st.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(st);
		}
    }
	
	// Get Inquest Paper Receipt Detail
		@GetMapping("/getallimagefile")
		public ResponseEntity<?> getAllImageFileHandler() {
			Map<String, List<ImageFileGetDTO>> getAllImageFile = imageFileService.getAllImageFile();
			return ResponseEntity.status(HttpStatus.OK).body(getAllImageFile);
		}
		
		@PostMapping("/addImageIdAndFlagType")
		public ResponseEntity<?> addImageByIdAndFlagType(@RequestBody List<Map<String, String>> keyValuedata) {
		    List<ImageFileGetDTO> resultList = keyValuedata.stream()
		        .map(entry -> {
		            String id = entry.get("id");
		            String flagType = entry.get("flagType");
		            return imageFileService.getImageByIdAndFlagType(id, flagType);
		        })
		        .toList();

		    return ResponseEntity.ok(resultList);
		}
		
}
