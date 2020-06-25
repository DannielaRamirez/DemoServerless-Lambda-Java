package com.sophos.demoserverless.beans;

import java.util.Map;

public class LambdaProxyRequest {

	private String body;
	private String resource;
	private Map<String, String> pathParameters;
	private String httpMethod;
	private Map<String, String> stageVariables;
	private String path;

	@Override
	public String toString() {
		return "LambdaProxyRequest{" +
			"body='" + body + '\'' +
			", resource='" + resource + '\'' +
			", pathParameters=" + pathParameters +
			", httpMethod='" + httpMethod + '\'' +
			", stageVariables=" + stageVariables +
			", path='" + path + '\'' +
		'}';
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public Map<String, String> getPathParameters() {
		return pathParameters;
	}

	public void setPathParameters(Map<String, String> pathParameters) {
		this.pathParameters = pathParameters;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Map<String, String> getStageVariables() {
		return stageVariables;
	}

	public void setStageVariables(Map<String, String> stageVariables) {
		this.stageVariables = stageVariables;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
