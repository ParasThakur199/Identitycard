package com.idcard.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.idcard.Payload.FullSubmitRequest;
import com.idcard.Service.LabelService;

public class LabelController {
	@Autowired
    private LabelService labelService;

    @PostMapping("/labelSubmit")
    public ResponseEntity<String> submitLabels(@RequestBody FullSubmitRequest request) {
        try {
			labelService.saveLabelsFromBlocks(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return ResponseEntity.ok("Labels saved successfully.");
    }
}
