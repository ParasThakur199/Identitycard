package com.idcard.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tbl_states")
public class StateEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long state_id;

    private String stateName;
    private String stateCode;
}
