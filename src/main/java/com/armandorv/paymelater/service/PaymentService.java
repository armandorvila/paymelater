package com.armandorv.paymelater.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.armandorv.paymelater.domain.Payment;
import com.armandorv.paymelater.domain.User;
import com.armandorv.paymelater.exception.UserNotFoundException;
import com.armandorv.paymelater.repository.PaymentRepository;
import com.armandorv.paymelater.repository.UserRepository;

@Service
@Transactional
public class PaymentService {

	private final Logger log = LoggerFactory.getLogger(PaymentService.class);

	@Inject
	private PaymentRepository paymentRepository;

	@Inject
	private UserRepository userRepository;

	public void createPayment(String lenderLogin, String borrowerLogin, Payment payment) {

		log.debug("Creating new Payment lender :" + lenderLogin + " borrwer "
				+ borrowerLogin);
		
		User lender = userRepository.findOne(lenderLogin);
		User borrower = userRepository.findOne(borrowerLogin);
		
		assertUserExists(lender);
		assertUserExists(borrower);
		
		payment.setLender(lender);
		payment.setBorrower(borrower);

		paymentRepository.save(payment);

		log.debug("Payment created lender :" + lenderLogin + " borrwer "
				+ borrowerLogin);
	}

	private void assertUserExists(User user) {
		if(user == null){
			log.info("User not found. Payment creation rejected.");
			throw new UserNotFoundException("User not found");
		}
	}
	
	public List<Payment> getDebts(String borrwerLogin){
		
		User borrower = userRepository.findOne(borrwerLogin);
		assertUserExists(borrower);
		return paymentRepository.findByBorrower(borrower);
	}
	
	public List<Payment> getCharges(String lenderLogin){
		User lender = userRepository.findOne(lenderLogin);
		assertUserExists(lender);
		return paymentRepository.findByLender(lender);
	}
}
