package com.armandorv.paymelater.web.rest.dto;

import org.joda.time.LocalDate;

import com.armandorv.paymelater.domain.util.CustomLocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;

public class PaymentDTO {

	private String subject;
	private String description;
	private String location;
	
	private String borrower;

	private Double amount;
	
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
	private LocalDate deadLine;

	public PaymentDTO() {

	}

	public PaymentDTO(String subject, String description, String location,
			String borrower, Double amount, LocalDate deadLine) {
		super();
		this.subject = subject;
		this.description = description;
		this.location = location;
		this.borrower = borrower;
		this.amount = amount;
		this.deadLine = deadLine;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(LocalDate deadLine) {
		this.deadLine = deadLine;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
