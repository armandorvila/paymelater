package com.armandorv.paymelater.web.rest;

import java.io.IOException;
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
import org.springframework.web.servlet.ModelAndView;

import com.armandorv.paymelater.ceca.CecaClient;
import com.armandorv.paymelater.ceca.CecaForm;
import com.armandorv.paymelater.domain.Payment;
import com.armandorv.paymelater.paypal.PayPalClient;
import com.armandorv.paymelater.repository.PaymentRepository;
import com.armandorv.paymelater.security.SecurityUtils;
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

	@Inject
	private PayPalClient payPalClient;

	@Inject
	private CecaClient cecaClient;

	/**
	 * POST /rest/charges -> Create a new charge.
	 */
	@RequestMapping(value = "/rest/charges", method = RequestMethod.POST, produces = "application/json")
	@Timed
	public void create(@RequestBody PaymentDTO dto) {
		log.debug("REST request to save Payment : {}", dto);

		paymentService.createPayment(
				SecurityUtils.getCurrentLogin(),
				dto.getBorrower(),
				new Payment(dto.getSubject(), dto.getDescription(), dto
						.getLocation(), dto.getAmount(), dto.getDeadLine()));
	}

	/**
	 * GET /rest/payments -> get all the payments.
	 */
	@RequestMapping(value = "/rest/charges", method = RequestMethod.GET, produces = "application/json")
	@Timed
	public List<Payment> getCharges() {
		log.debug("REST request to get Charges for current user :"
				+ SecurityUtils.getCurrentLogin());
		return paymentService.getCharges(SecurityUtils.getCurrentLogin());
	}

	/**
	 * GET /rest/payments -> get all the payments.
	 */
	@RequestMapping(value = "/rest/debts", method = RequestMethod.GET, produces = "application/json")
	@Timed
	public List<Payment> getDebts() {
		log.debug("REST request to get all Payments");
		log.debug("REST request to get Debts for current user :"
				+ SecurityUtils.getCurrentLogin());
		return paymentService.getDebts(SecurityUtils.getCurrentLogin());
	}

	/**
	 * GET /rest/charges|debts/:id -> get the "id" payment.
	 */
	@RequestMapping(value = { "/rest/charges/{id}", "/rest/debts/{id}" }, method = RequestMethod.GET, produces = "application/json")
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
	@RequestMapping(value = { "/rest/charges/{id}" }, method = RequestMethod.DELETE, produces = "application/json")
	@Timed
	public void delete(@PathVariable Long id, HttpServletResponse response) {
		log.debug("REST request to delete Payment : {}", id);
		paymentRepository.delete(id);
	}

	/**
	 * DELETE /rest/payments/:id -> delete the "id" payment.
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = { "/rest/debts/payPal/{id}" }, method = RequestMethod.GET, produces = "application/json")
	@Timed
	public String payDebtWithPayPal(@PathVariable Long id,
			HttpServletResponse response) throws IOException {
		log.debug("REST request to pay with pay pal Payment : {}", id);
		Payment payment = paymentRepository.findOne(id);
		return payPalClient.pay(payment.getAmount());
	}

	/**
	 * DELETE /rest/payments/:id -> delete the "id" payment.
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = { "/rest/debts/card/{id}" }, method = RequestMethod.GET)
	@Timed
	public ModelAndView payDebtWithCard(@PathVariable Long id,
			HttpServletResponse response) throws IOException {
		log.debug("REST request to pay with card Payment : {}", id);
		Payment payment = paymentRepository.findOne(id);

		if (payment == null) {
			response.sendError(404);
			return null;
		}

		if (!SecurityUtils.getCurrentLogin().equals(
				payment.getBorrower().getLogin())) {
			response.sendError(401);
			return null;
		}

		CecaForm form = cecaClient.pay(payment.getId(), payment.getAmount());
		log.debug("Preparing payment at " + form.getUrl());
		return new ModelAndView("ceca", "form", form);
	}
	
	@RequestMapping(value = { "/rest/debts/card/payed/{id}" }, method = RequestMethod.GET)
	@Timed
	public void cardPaymentSuccesfull(@PathVariable Long id,
			HttpServletResponse response) throws IOException {
		log.debug("REST request to pay with card Payment : {}", id);
		paymentRepository.delete(id);
		response.sendRedirect("/#/debts");
	}
}
