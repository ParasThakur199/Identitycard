package com.idcard.Service;

import java.io.IOException;
import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.idcard.CommonData.Juleandate;
import com.idcard.Model.IdcardEntity;
import com.idcard.Model.UserEntity;
import com.idcard.Payload.ApprovedCardDTO;
import com.idcard.Payload.IdcardDTO;
import com.idcard.Payload.IdcardGetDTO;
import com.idcard.Repository.IdCardRepository;
import com.idcard.Repository.StateRepository;
import com.idcard.config.JwtService;

import jakarta.transaction.Transactional;

@Service
public class IdcardServiceImpl implements IdcardService {

	@Autowired
	private IdCardRepository idrepo;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private JwtService helper;

	@Override
	public String saveIdcard(String tokenHeader, IdcardDTO dto) throws IOException {
		final UserEntity claims2 = helper.getUserDetailsFromToken(tokenHeader);

		String transactionId = transactionService.generateTransactionNumber(dto.getStateCode());
		IdcardEntity idc = new IdcardEntity();
		idc.setCardno(dto.getCardno());
		idc.setName(dto.getName());
		idc.setDesignation(dto.getDesignation());
		idc.setEmpcode(dto.getEmpcode());
		idc.setPostingplace(dto.getPostingplace());
		idc.setValidupto(dto.getValidupto());
		idc.setDob(dto.getDob());
		idc.setIdentificationmark(dto.getIdentificationmark());
		idc.setEmergencycontactname(dto.getEmergencycontactname());
		idc.setEmergencycontactno(dto.getEmergencycontactno());
		idc.setAddress(dto.getAddress());
		idc.setHeight(dto.getHeight());
		idc.setBloodgroup(dto.getBloodgroup());
		idc.setMobile(dto.getMobile());
		idc.setOfficeaddress(dto.getOfficeaddress());
		idc.setStateCode(dto.getStateCode());
		idc.setPhoto(convertToByteArray(dto.getPhoto()));
		idc.setSignature1(convertToByteArray(dto.getSignature1()));
		idc.setSignature2(convertToByteArray(dto.getSignature2()));
		idc.setLogo(convertToByteArray(dto.getLogo()));
		idc.setWatermark(convertToByteArray(dto.getWatermark()));
		idc.setTransactionId(transactionId);
		idc.setCreateUser(claims2.getUserId());
		idc.setCreateDate(Juleandate.getCurrentDateTime());
		idc.setCardStatus("P");
		idrepo.save(idc);

		String cid = String.valueOf(idc.getId());
		return cid;
	}

	@Transactional
	@Override
	public List<IdcardGetDTO> getAllActiveCards() {
		List<IdcardEntity> entities = idrepo.findAllByCardStatus("A");

		// Convert Entity list to DTO list
		List<IdcardGetDTO> dtoList = entities.stream().map(entity -> {
			IdcardGetDTO dto = new IdcardGetDTO();
			dto.setId(entity.getId());
			dto.setCardno(entity.getCardno());
			dto.setName(entity.getName());
			dto.setDesignation(entity.getDesignation());
			dto.setEmpcode(entity.getEmpcode());
			dto.setPostingplace(entity.getPostingplace());
			dto.setIssuedate(Juleandate.getDatetoString(entity.getIssuedate()));
			dto.setValidupto(entity.getValidupto());
			dto.setDob(entity.getDob());
			dto.setIdentificationmark(entity.getIdentificationmark());
			dto.setEmergencycontactname(entity.getEmergencycontactname());
			dto.setEmergencycontactno(entity.getEmergencycontactno());
			dto.setAddress(entity.getAddress());
			dto.setHeight(entity.getHeight());
			dto.setBloodgroup(entity.getBloodgroup());
			dto.setMobile(entity.getMobile());
			dto.setOfficeaddress(entity.getOfficeaddress());
			dto.setTransactionId(entity.getTransactionId());
			dto.setCardStatus(entity.getCardStatus());
			dto.setStateCode(entity.getStateCode());
			dto.setCreateUser(entity.getCreateUser());
			dto.setCreateDate(Juleandate.getDatetoString(entity.getCreateDate()));

//		            dto.setStateName(stateRepository.findByStateCode(entity.getStateCode()).get().getStateName());
			// Skipping photo, signature1, signature2, logo, watermark
			return dto;
		}).collect(Collectors.toList());

		return dtoList;
	}

	private byte[] convertToByteArray(MultipartFile file) {
		try {
			if (file != null) {
				return file.getBytes();
			}
			throw new ResolutionException("Upload Document");

		} catch (Exception e) {
			throw new ResolutionException("Invalid Document");
		}

	}

	@Override
	public String updateApproveRejectCard(String tokenHeader, ApprovedCardDTO dto) {
		String cid = "0";
		final UserEntity claims2 = helper.getUserDetailsFromToken(tokenHeader);
		Optional<IdcardEntity> optenty = idrepo.findById(dto.getId());
		if (optenty.isPresent()) {
			IdcardEntity idc = optenty.get();
			idc.setCardStatus(dto.getCardStatus());
			String trn = idc.getTransactionId();
			String lastFour = trn.length() >= 4 ? trn.substring(trn.length() - 4) : trn;
			int nextNumber = Integer.parseInt(lastFour) + 1; // Increment
			String padded = String.format("%04d", nextNumber);
			if (dto.getCardStatus().equalsIgnoreCase("A")) {
				idc.setIssuedate(Juleandate.getCurrentDateTime());
				idc.setCardno(padded);
			}
			idrepo.save(idc);

			cid = "1";
		}
		return cid;
	}

	@Transactional
	@Override
	public List<IdcardGetDTO> getAllPendingCards() {
		List<IdcardEntity> entities = idrepo.findAllByCardStatus("P");

		// Convert Entity list to DTO list
		List<IdcardGetDTO> dtoList = entities.stream().map(entity -> {
			IdcardGetDTO dto = new IdcardGetDTO();
			dto.setId(entity.getId());
			dto.setCardno(entity.getCardno());
			dto.setName(entity.getName());
			dto.setDesignation(entity.getDesignation());
			dto.setEmpcode(entity.getEmpcode());
			dto.setPostingplace(entity.getPostingplace());
			dto.setIssuedate(Juleandate.getDatetoString(entity.getIssuedate()));
			dto.setValidupto(entity.getValidupto());
			dto.setDob(entity.getDob());
			dto.setIdentificationmark(entity.getIdentificationmark());
			dto.setEmergencycontactname(entity.getEmergencycontactname());
			dto.setEmergencycontactno(entity.getEmergencycontactno());
			dto.setAddress(entity.getAddress());
			dto.setHeight(entity.getHeight());
			dto.setBloodgroup(entity.getBloodgroup());
			dto.setMobile(entity.getMobile());
			dto.setOfficeaddress(entity.getOfficeaddress());
			dto.setTransactionId(entity.getTransactionId());
			dto.setCardStatus(entity.getCardStatus());
			dto.setStateCode(entity.getStateCode());

//		            dto.setStateName(stateRepository.findByStateCode(entity.getStateCode()).get().getStateName());
			// Skipping photo, signature1, signature2, logo, watermark
			return dto;
		}).collect(Collectors.toList());

		return dtoList;
	}

	@Override
	public IdcardGetDTO getCardById(Long id) {
		Optional<IdcardEntity> optenty = idrepo.findById(id);
		IdcardGetDTO dto = new IdcardGetDTO();
		if (optenty.isPresent()) {
			IdcardEntity enty = optenty.get();
			dto.setId(enty.getId());
			dto.setCardno(enty.getCardno());
			dto.setTransactionId(enty.getTransactionId());
			dto.setStateCode(enty.getStateCode());
			dto.setName(enty.getName());
			dto.setDesignation(enty.getDesignation());
			dto.setEmpcode(enty.getEmpcode());
			dto.setPostingplace(enty.getPostingplace());
			dto.setIssuedate(Juleandate.getDatetoString(enty.getIssuedate()));
			dto.setValidupto(enty.getValidupto());
			dto.setDob(enty.getDob());
			dto.setIdentificationmark(enty.getIdentificationmark());
			dto.setEmergencycontactname(enty.getEmergencycontactname());
			dto.setEmergencycontactno(enty.getEmergencycontactno());
			dto.setAddress(enty.getAddress());
			dto.setHeight(enty.getHeight());
			dto.setBloodgroup(enty.getBloodgroup());
			dto.setMobile(enty.getMobile());
			dto.setOfficeaddress(enty.getOfficeaddress());
			dto.setCardStatus(enty.getCardStatus());
			dto.setPhoto(enty.getPhoto());
			dto.setSignature1(enty.getSignature1());
			dto.setSignature2(enty.getSignature2());
			dto.setLogo(enty.getLogo());
			dto.setWatermark(enty.getWatermark());

		}
		return dto;
	}

}
