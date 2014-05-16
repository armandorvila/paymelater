package com.armandorv.paymelater.ceca;

import java.util.Locale;

import net.sourceforge.spanishbanklib.card.AppInfo;
import net.sourceforge.spanishbanklib.card.CecaCreditCardPayment;
import net.sourceforge.spanishbanklib.card.CecaMerchantConfig;
import net.sourceforge.spanishbanklib.card.PaymentInfo;

import org.springframework.stereotype.Component;

@Component
public class CecaClient {

	private boolean testMode = true;

	private AppInfo appInfo = new AppInfo();
	private PaymentInfo paymentInfo = new PaymentInfo();
	private CecaMerchantConfig cecaMerchantConfig = new CecaMerchantConfig();

	public CecaClient() {
		
		cecaMerchantConfig.setMerchantCode("082108630");
		cecaMerchantConfig.setTerminalId("00000003");
		cecaMerchantConfig.setAcquirerBin("0000554002");
		cecaMerchantConfig.setMerchantTestPassword("87401456");

		appInfo.setMerchantKoUrl("http://localhost:8080/#/debts");
		appInfo.setMerchantOkUrl("http://localhost:8080/#/debts");
		appInfo.setOrderInfoUrl("http://localhost:8080/#/debts");
		

		paymentInfo = new PaymentInfo();
		paymentInfo.setMerchantCurrency(978);
		paymentInfo.setMerchantTitular("UNIOVI");
		paymentInfo.setProductDescription("Uniovi TEST");
	}

	public CecaForm pay(long id, Double amount) {
	
		paymentInfo.setAmount((long) (amount * 100));
		paymentInfo.setPaymentId(id);
		appInfo.setMerchantOkUrl("http://localhost:8080/app/rest/debts/card/payed/"+id);
		
		CecaCreditCardPayment payment = new CecaCreditCardPayment(testMode,
				cecaMerchantConfig, appInfo, paymentInfo);
		
		return new CecaForm(payment.getFormParams(new Locale("ES")),
				payment.getFormUrl());
	}
}
