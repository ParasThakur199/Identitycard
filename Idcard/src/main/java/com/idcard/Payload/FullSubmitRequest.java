package com.idcard.Payload;

import java.util.List;

import lombok.Data;
@Data
public class FullSubmitRequest {
	private List<BlockLabelRequest> blockLabelData;
	
//	public List<BlockLabelRequest> getBlockLabelData() {
//        return blockLabelData;
//    }
//
//    public void setBlockLabelData(List<BlockLabelRequest> blockLabelData) {
//        this.blockLabelData = blockLabelData;
//    }
}
