package com.armandorv.paymelater.paypal;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;

@Component
public class PayPalClient {

	private final Logger log = LoggerFactory.getLogger(PayPalClient.class);

	@PostConstruct
	public void init() {
		InputStream is = PayPalClient.class
				.getResourceAsStream("/sdk_config.properties");
		try {
			PayPalResource.initConfig(is);
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
			throw new RuntimeException("", e);
		}
	}

	public String pay(double subtotal) {
		APIContext apiContext = null;
		try {
			apiContext = new APIContext(GenerateAccessToken.getAccessToken());
		} catch (PayPalRESTException e) {
			log.error("Error creating PayPal API Context.");
			throw new RuntimeException("", e);
		}

		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(payPalTransaction(subtotal));

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		// ###Redirect URLs
		RedirectUrls redirectUrls = new RedirectUrls();
		
		redirectUrls.setCancelUrl("http://localhost:8080/#/debts/payPalCancelled");
		redirectUrls.setReturnUrl("http://localhost:8080/#/debts/payPalSuccessful");
		payment.setRedirectUrls(redirectUrls);

		try {
			Payment createdPayment = payment.create(apiContext);
			
			log.info("Created payment with id = " + createdPayment.getId()
					+ " and status = " + createdPayment.getState());
			
			for (Links link : createdPayment.getLinks()) {
				if (link.getRel().equalsIgnoreCase("approval_url")) {
					return link.getHref();
				}
			}
			
			return null;
			
		} catch (PayPalRESTException e) {
			log.error("Paying with Pay Pal");
			throw new RuntimeException("", e);
		}
	}

	private Transaction payPalTransaction(double subtotal) {
		// Let's you specify details of a payment amount.
		Details details = new Details();
		details.setShipping("1");
		details.setSubtotal("5");
		details.setTax("1");

		// Let's you specify a payment amount.
		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal("7");
		amount.setDetails(details);

		// A transaction defines the contract of a
		// payment - what is the payment for and who
		// is fulfilling it. Transaction is created with
		// a `Payee` and `Amount` types
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction
				.setDescription("This is the payment transaction description.");
		return transaction;
	}
}
