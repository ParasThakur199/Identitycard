package com.idcard.AdminController;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idcard.Payload.ApprovedCardDTO;
import com.idcard.Payload.IdcardDTO;
import com.idcard.Payload.IdcardGetDTO;
import com.idcard.Payload.Response_Status;
import com.idcard.Service.FileService;
import com.idcard.Service.IdcardService;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class ApproveRejectCardController {

	@Autowired
	private IdcardService idcardService;
	
	@Autowired
	private FileService fileService;
	
	@PutMapping(value = "/approveRejectCard")
	public ResponseEntity<?> approveRejectData(@RequestHeader("Authorization") String tokenHeader,@RequestBody ApprovedCardDTO dto)
	{ 
		Response_Status res = new Response_Status();
		try {
			String response = idcardService.updateApproveRejectCard(tokenHeader,dto);
			if(response.equalsIgnoreCase("1")) {
				res.setMessage("Status Updated successfully");
				return ResponseEntity.ok(res);
			}else {
				res.setMessage("Failed");
				return ResponseEntity.ok(res);
			}				
		} catch (Exception e) {
			res.setMessage(e.getMessage());
			return ResponseEntity.status(500).body("Failed to update data: " + res);
		}
	}
	
	@GetMapping("/allPendingCards")
	public ResponseEntity<List<IdcardGetDTO>> getAllPendingCards() {   
	    return new ResponseEntity<>(idcardService.getAllPendingCards(), HttpStatus.OK);
	}
	
	@GetMapping("/allActiveCards")
	public ResponseEntity<List<IdcardGetDTO>> allActiveCards() {   
	    return new ResponseEntity<>(idcardService.getAllActiveCards(), HttpStatus.OK);
	}
	
	
	
	@GetMapping("/getCardById/{id}")
	public ResponseEntity<?> getCardById(@PathVariable("id") Long id) throws IOException {
		try {
			return new ResponseEntity<>(idcardService.getCardById(id), HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	@GetMapping("/pdf/{id}")
	public ResponseEntity<?> malePotencyPdf(@PathVariable("id") Long id) throws IOException {
		try {
			byte filedata[] = fileService.generatepdf(id);
			String filename = "empcard(" + id + ").pdf";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			return ResponseEntity.ok().headers(headers).contentLength(filedata.length)
					.contentType(MediaType.APPLICATION_PDF).body(new ByteArrayResource(filedata));
		} catch (Exception e) {
			
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
