package com.idcard.Payload;

import lombok.Data;

@Data
public class StateDTO {
	private Long state_id;

    private String stateName;
    private String stateCode;
}
