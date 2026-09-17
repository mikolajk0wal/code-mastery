package com.macias34.codemastery.user.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "confirmation_token")
	private String confirmationToken;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp createdAt;

	private String email;

	public ConfirmationToken(String email) {
		this.email = email;
		this.confirmationToken = UUID.randomUUID().toString();
	}

}
