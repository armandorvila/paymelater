package com.armandorv.paymelater.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.armandorv.paymelater.domain.Payment;
import com.armandorv.paymelater.repository.PaymentRepository;
import com.armandorv.paymelater.service.PaymentService;
import com.armandorv.paymelater.web.rest.dto.PaymentDTO;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Payment.
 */
@RestController
@RequestMapping("/app")
public class PaymentResource {

	private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

	@Inject
	private PaymentRepository paymentRepository;

	@Inject
	private PaymentService paymentService;

	/**
	 * POST /rest/payments -> Create a new payment.
	 */
	@RequestMapping(value = "/rest/payments", method = RequestMethod.POST, produces = "application/json")
	@Timed
	public void create(@RequestBody PaymentDTO dto) {
		log.debug("REST request to save Payment : {}", dto);

		paymentService.createPayment(
				dto.getLender(),
				dto.getBorrower(),
				new Payment(dto.getSubject(), dto.getDescription(), dto
						.getAmount(), null));
	}

	/**
	 * GET /rest/payments -> get all the payments.
	 */
	@RequestMapping(value = "/rest/payments", method = RequestMethod.GET, produces = "application/json")
	@Timed
	public List<Payment> getAll() {
		log.debug("REST request to get all Payments");
		return paymentRepository.findAll();
	}

	/**
	 * GET /rest/payments/:id -> get the "id" payment.
	 */
	@RequestMapping(value = "/rest/payments/{id}", method = RequestMethod.GET, produces = "application/json")
	@Timed
	public Payment get(@PathVariable Long id, HttpServletResponse response) {
		log.debug("REST request to get Payment : {}", id);
		Payment payment = paymentRepository.findOne(id);
		if (payment == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return payment;
	}

	/**
	 * DELETE /rest/payments/:id -> delete the "id" payment.
	 */
	@RequestMapping(value = "/rest/payments/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@Timed
	public void delete(@PathVariable Long id, HttpServletResponse response) {
		log.debug("REST request to delete Payment : {}", id);
		paymentRepository.delete(id);
	}
}
