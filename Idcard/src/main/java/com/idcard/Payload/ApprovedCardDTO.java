package com.idcard.Payload;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApprovedCardDTO {
	private Long id;

    @Size(max = 1, message = "Status must be at most 1 characters")
    private String cardStatus;
}
