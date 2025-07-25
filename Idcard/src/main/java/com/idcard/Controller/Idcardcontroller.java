package com.idcard.Controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.idcard.Model.IdcardEntity;
//import com.idcard.Payload.FormLabelsDTO;
import com.idcard.Payload.IdcardDTO;
import com.idcard.Payload.IdcardGetDTO;
import com.idcard.Payload.LabelDTO;
import com.idcard.Payload.LabelGetDTO;
import com.idcard.Payload.StateDTO;
import com.idcard.Repository.IdCardRepository;
import com.idcard.Service.FileService;
import com.idcard.Service.IdcardService;
import com.idcard.Service.StateService;
import com.idcard.Service.TransactionService;


import com.idcard.Payload.FullSubmitRequest;
import com.idcard.Service.LabelService;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class Idcardcontroller {
	
	@Autowired
	private IdcardService idcardService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
    private StateService stateService;
	
	// /*added
	@Autowired
    private LabelService labelService;

//    @PostMapping("/labelSubmit")
//    public ResponseEntity<String> submitLabels(@RequestBody FullSubmitRequest request) {
//        try {
//			labelService.saveLabelsFromBlocks(request);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        return ResponseEntity.ok("Labels saved successfully.");
//    }
//    
//    @GetMapping("/allLabels")
//    public ResponseEntity<List<LabelGetDTO>> getAllLabels() {
//    	return new ResponseEntity<>(labelService.getAllLabels(), HttpStatus.OK);
//    }
    
	
	@PostMapping(value = "/submitform", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> saveData(@RequestHeader("Authorization") String tokenHeader,@ModelAttribute IdcardDTO dto)
	{ 
		try {
			String response = idcardService.saveIdcard(tokenHeader,dto);
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to save data: " + e.getMessage());
		}
	}
	
	
	@GetMapping("/getCardDetailsById/{id}")
	public ResponseEntity<IdcardGetDTO> getCardDetailsById(@PathVariable("id") Long id) {   
	    return new ResponseEntity<>(idcardService.getCardById(id), HttpStatus.OK);
	}
	
	
	@GetMapping("/download/{id}/{fileType}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long id, @PathVariable String fileType) {
	    // Load file from storage
		Resource file = fileService.getFileByIdAndType(id, fileType); // Implement this

	    String filename = fileType + "_" + id + ".jpg"; // adjust extension based on MIME type

	    return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
	            .body(file);
	}
	
	@PostMapping("/generateTransaction")
	public ResponseEntity<String> generateTransaction(@RequestParam String stateCode) {
	    String trn = transactionService.generateTransactionNumber(stateCode);
	    return ResponseEntity.ok(trn);
	}

	@GetMapping("/getAllStates")
    public ResponseEntity<List<StateDTO>> getAllStates() {
        return new ResponseEntity(stateService.getAllStatesList(),HttpStatus.OK);
    }
	
	private Byte[] convertToByteArray(MultipartFile file) throws IOException {
		byte[] bytes = file.getBytes();
		Byte[] byteObjects = new Byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			byteObjects[i] = bytes[i];
		}
		return byteObjects;
	}
}
