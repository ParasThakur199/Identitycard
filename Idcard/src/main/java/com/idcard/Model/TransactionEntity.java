package com.idcard.Model;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
//import java.time.LocalDate;

@Entity
@Data
@Table(name = "tbl_transactions")
public class TransactionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String stateCode;
    private String year;
    private int serialNumber;
    private Date createDate;
    private Date updateDate;
}
