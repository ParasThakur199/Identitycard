package com.idcard.Payload;

import java.util.List;

import lombok.Data;
@Data
public class BlockLabelRequest {
	private String page;
	private String section;
	private String block;
    private List<LabelDTO> labels;
}
