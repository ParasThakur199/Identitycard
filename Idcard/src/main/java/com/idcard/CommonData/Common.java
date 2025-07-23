package com.idcard.CommonData;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idcard.Payload.DesignationDTO;
import com.idcard.Service.DesignationService;

@RestController
@RequestMapping("/alluser/all")
public class Common {
	
	@Autowired
	private DesignationService designationService;
	
	@GetMapping("/alldesignationdetails")
    public ResponseEntity<List<DesignationDTO>> getAllLabels() {
    	return new ResponseEntity<>(designationService.getDesignationDetails(), HttpStatus.OK);
    }
	
}
