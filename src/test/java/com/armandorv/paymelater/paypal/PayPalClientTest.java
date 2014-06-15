package com.armandorv.paymelater.paypal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

/**
 * Test class for the UserResource REST controller.
 *
 * @see PayPalClientTest
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//@ActiveProfiles("dev")
public class PayPalClientTest {

	@Inject
	private PayPalClient payPalClient;

	@Test
	public void testPay() throws IOException {
//		assertThat(payPalClient).isNotNull();
//		String payPalUrl = payPalClient.pay(1);
//		assertThat(payPalUrl).isNotNull();
//		assertThat(payPalUrl).contains("www.sandbox.paypal.com");

		Security.addProvider(new BouncyCastleProvider());
		
		String httpsURL = "https://www.google.es";
		URL myurl = new URL(httpsURL);
		//HttpURLConnection con = (HttpURLConnection) myurl.openConnection();;
		HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
		InputStream ins = con.getInputStream();
		InputStreamReader isr = new InputStreamReader(ins);
		BufferedReader in = new BufferedReader(isr);

		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
		}

		in.close();
	}
}
