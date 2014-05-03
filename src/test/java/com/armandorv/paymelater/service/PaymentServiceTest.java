package com.armandorv.paymelater.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.armandorv.paymelater.Application;
import com.armandorv.paymelater.domain.Payment;
import com.armandorv.paymelater.repository.PaymentRepository;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
public class PaymentServiceTest {

	@Inject
	private PaymentService paymentService;

	@Inject
	private PaymentRepository paymentRepository;

	@Test
	public void testCreatePayment() {
		assertThat(paymentRepository.findAll()).isEmpty();
		paymentService.createPayment("admin", "user", generatePayment());
		assertThat(paymentRepository.findAll()).hasSize(1);
	}

	@Test
	public void getCharges() {
		assertThat(paymentService.getCharges("admin")).hasSize(1);
	}

	@Test
	public void getDebts() {
		assertThat(paymentService.getDebts("user")).hasSize(1);
	}

	private Payment generatePayment() {
		Payment payment = new Payment();
		payment.setAmount(23.0);
		payment.setBeginning(new LocalDate());
		payment.setDeadLine(new LocalDate());
		payment.setSubject("Fuel");
		payment.setDescription("I let 23€ to user.");
		return payment;
	}

}
