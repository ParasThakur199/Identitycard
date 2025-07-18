package com.idcard.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcard.Model.StateEntity;
import com.idcard.Payload.StateDTO;
import com.idcard.Repository.StateRepository;

@Service
public class StateServiceImpl implements StateService{
	 @Autowired
	    private StateRepository stateRepository;

	    

	    @Override
	    public List<StateDTO> getAllStatesList() {
	        List<StateEntity> states = stateRepository.findAll();
	        return states.stream().map(entity -> {
	            StateDTO dto = new StateDTO();
	            dto.setState_id(entity.getState_id());
	            dto.setStateName(entity.getStateName());
	            dto.setStateCode(entity.getStateCode());
	            return dto;
	        }).collect(Collectors.toList());
	    }

}
