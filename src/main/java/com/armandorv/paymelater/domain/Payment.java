package com.armandorv.paymelater.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 * A Payment.
 */
@Entity
@Table(name = "T_PAYMENT")
public class Payment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	@Size(min = 1, max = 50)
	@Column(name = "subject")
	private String subject;

	@Size(min = 1, max = 250)
	@Column(name = "description")
	private String description;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "beginning")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate beginning;

	@Column(name = "dead_line")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate deadLine;

	@ManyToOne
	@JoinColumn(name="borrower")
	private User borrower;

	@ManyToOne
	@JoinColumn(name="lender")
	private User lender;

	public Payment(){}
	
	public Payment(String subject, String description, Double amount,
			LocalDate deadLine) {
		this.subject = subject;
		this.description = description;
		this.amount = amount;
		this.deadLine = deadLine;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public LocalDate getBeginning() {
		return beginning;
	}

	public void setBeginning(LocalDate beginning) {
		this.beginning = beginning;
	}

	public LocalDate getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(LocalDate deadLine) {
		this.deadLine = deadLine;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public User getBorrower() {
		return borrower;
	}

	public void setBorrower(User borrower) {
		this.borrower = borrower;
	}

	public User getLender() {
		return lender;
	}

	public void setLender(User lender) {
		this.lender = lender;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Payment payment = (Payment) o;

		if (id != payment.id) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "Payment{" + "id=" + id + ", sampleTextAttribute='" + subject
				+ '\'' + ", sampleDateAttribute=" + beginning + '}';
	}
}
