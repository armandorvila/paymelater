package com.armandorv.paymelater.repository;

import java.util.List;

import com.armandorv.paymelater.domain.Payment;
import com.armandorv.paymelater.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Payment entity.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	@Query("select p from Payment p where p.borrower = ?1")
	List<Payment> findByBorrower(User borrower);
	
	@Query("select p from Payment p where p.lender = ?1")
	List<Payment> findByLender(User lender);
}
