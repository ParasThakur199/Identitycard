package com.idcard.Service;

import java.io.IOException;
import java.util.List;

import com.idcard.Payload.ApprovedCardDTO;
import com.idcard.Payload.IdcardDTO;
import com.idcard.Payload.IdcardGetDTO;

public interface IdcardService {
	String saveIdcard(String tokenHeader, IdcardDTO dto) throws IOException;
//	Long saveIdcard(IdcardDTO dto) throws IOException;
	List<IdcardGetDTO> getAllPendingCards();
	List<IdcardGetDTO> getAllActiveCards();

	IdcardGetDTO getCardById(Long id);
	String updateApproveRejectCard(String tokenHeader, ApprovedCardDTO dto);
//	IdcardGetDTO getCardDetailsById(String id);
	
}
