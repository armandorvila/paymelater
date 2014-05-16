package com.armandorv.paymelater.ceca;

import java.util.HashMap;
import java.util.Map;

public class CecaForm {

	private Map<String, String> params = new HashMap<>();

	private String url;

	public CecaForm(Map<String, String> params, String url) {
		super();
		this.params = params;
		this.url = url;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}