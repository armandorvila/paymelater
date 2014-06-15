package com.armandorv.paymelater.paypal;

import java.io.IOException;
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

	@Test
	public void testPay() throws IOException {
		assertThat(payPalClient).isNotNull();
		String payPalUrl = payPalClient.pay(1);
		assertThat(payPalUrl).isNotNull();
		assertThat(payPalUrl).contains("www.sandbox.paypal.com");

//		Security.addProvider(new BouncyCastleProvider());
//		
//		String httpsURL = "https://www.google.es";
//		URL myurl = new URL(httpsURL);
//		//HttpURLConnection con = (HttpURLConnection) myurl.openConnection();;
//		HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
//		InputStream ins = con.getInputStream();
//		InputStreamReader isr = new InputStreamReader(ins);
//		BufferedReader in = new BufferedReader(isr);
//
//		String inputLine;
//
//		while ((inputLine = in.readLine()) != null) {
//			System.out.println(inputLine);
//		}
//
//		in.close();
	}
}
