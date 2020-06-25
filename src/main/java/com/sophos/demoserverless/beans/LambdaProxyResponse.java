package com.sophos.demoserverless.beans;

import java.util.Map;

public class LambdaProxyResponse {

	private int statusCode;
	private Map<String, String> headers = Map.of(
		"Access-Control-Allow-Origin", "*",
		"Access-Control-Allow-Methods", "*",
		"Content-Type", "application/json"
	);
	private String body;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
