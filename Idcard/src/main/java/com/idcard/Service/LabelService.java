package com.idcard.Service;

import java.io.IOException;
import java.util.List;

import com.idcard.Payload.FullSubmitRequest;
import com.idcard.Payload.LabelGetDTO;

public interface LabelService {
	String saveLabelsFromBlocks(FullSubmitRequest request) throws IOException;
	List<LabelGetDTO> getAllLabels();
}
