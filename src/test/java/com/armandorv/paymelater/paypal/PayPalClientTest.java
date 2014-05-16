package com.armandorv.paymelater.paypal;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.armandorv.paymelater.Application;

/**
 * Test class for the UserResource REST controller.
 *
 * @see PayPalClientTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
public class PayPalClientTest {
	
	@Inject
	private PayPalClient payPalClient;
	
	//@Test
	public void testPay(){
		assertThat(payPalClient).isNotNull();
		String payPalUrl = payPalClient.pay(1);
		assertThat(payPalUrl).isNotNull();
		assertThat(payPalUrl).contains("www.sandbox.paypal.com");
	}
}
