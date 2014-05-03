package com.armandorv.paymelater.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.armandorv.paymelater.Application;
import com.armandorv.paymelater.repository.PaymentRepository;
import com.armandorv.paymelater.service.PaymentService;
import com.armandorv.paymelater.web.rest.dto.PaymentDTO;

/**
 * Test class for the PaymentResource REST controller.
 *
 * @see PaymentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@ActiveProfiles("dev")
public class PaymentResourceTest {

	private static final Long DEFAULT_ID = new Long(1L);

	private static final LocalDate DEFAULT_SAMPLE_DATE_ATTR = new LocalDate(0L);

	private static final LocalDate UPD_SAMPLE_DATE_ATTR = new LocalDate();

	private static final String DEFAULT_SAMPLE_TEXT_ATTR = "sampleTextAttribute";

	private static final String UPD_SAMPLE_TEXT_ATTR = "sampleTextAttributeUpt";

	@Inject
	private PaymentRepository paymentRepository;

	@Inject
	private PaymentService paymentService;

	private MockMvc restPaymentMockMvc;

	private PaymentDTO payment;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		PaymentResource paymentResource = new PaymentResource();
		ReflectionTestUtils.setField(paymentResource, "paymentRepository",
				paymentRepository);
		ReflectionTestUtils.setField(paymentResource, "paymentService",
				paymentService);

		this.restPaymentMockMvc = MockMvcBuilders.standaloneSetup(
				paymentResource).build();

		payment = new PaymentDTO();
		payment.setSubject(DEFAULT_SAMPLE_TEXT_ATTR);
		payment.setDescription(DEFAULT_SAMPLE_TEXT_ATTR
				+ DEFAULT_SAMPLE_TEXT_ATTR);
		payment.setDeadLine(new LocalDate());
		payment.setAmount(20.0);
		payment.setLender("admin");
		payment.setBorrower("user");
	}

	@Test
	public void testCRUDPayment() throws Exception {

		// Create Payment
		restPaymentMockMvc.perform(
				post("/app/rest/payments").contentType(
						TestUtil.APPLICATION_JSON_UTF8).content(
						TestUtil.convertObjectToJsonBytes(payment))).andExpect(
				status().isOk());

		// Read Payment
		restPaymentMockMvc
				.perform(get("/app/rest/payments/{id}", DEFAULT_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(
						jsonPath("$.subject").value(
								DEFAULT_SAMPLE_TEXT_ATTR.toString()));

		// Update Payment
		payment.setSubject("UPDATED SUBJECT");

		restPaymentMockMvc.perform(
				post("/app/rest/payments").contentType(
						TestUtil.APPLICATION_JSON_UTF8).content(
						TestUtil.convertObjectToJsonBytes(payment))).andExpect(
				status().isOk());

		// Delete Payment
//		restPaymentMockMvc.perform(
//				delete("/app/rest/payments/{id}", DEFAULT_ID).accept(
//						TestUtil.APPLICATION_JSON_UTF8)).andExpect(
//				status().isOk());
//
//		// Read nonexisting Payment
//		restPaymentMockMvc.perform(
//				get("/app/rest/payments/{id}", DEFAULT_ID).accept(
//						TestUtil.APPLICATION_JSON_UTF8)).andExpect(
//				status().isNotFound());

	}
}
