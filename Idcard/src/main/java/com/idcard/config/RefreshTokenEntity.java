package com.idcard.config;

import java.time.Instant;

import com.idcard.Model.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class RefreshTokenEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tokenid;
	private String refreshToken;
	private Instant expiry;	
	@OneToOne
	private UserEntity user;
}
