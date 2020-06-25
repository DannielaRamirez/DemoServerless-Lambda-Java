package com.sophos.demoserverless.controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sophos.demoserverless.beans.Error;
import com.sophos.demoserverless.beans.*;
import com.sophos.demoserverless.service.EmpleadoService;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class LambdaController {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final LambdaLogger LOGGER;

	private final EmpleadoService empleadoService;

	public LambdaController(Context context) {
		LOGGER = context.getLogger();
		empleadoService = new EmpleadoService(context);
	}

	public LambdaProxyResponse procesar(LambdaProxyRequest proxyRequest) {
		LOGGER.log(GSON.toJson(proxyRequest));
		final LambdaProxyResponse proxyResponse = new LambdaProxyResponse();
		try {

			final EmpleadoRequest request = GSON.fromJson(proxyRequest.getBody(), EmpleadoRequest.class);
			proxyResponse.setStatusCode(200);

			if(proxyRequest.getHttpMethod().equals("POST") && Objects.isNull(proxyRequest.getPathParameters())) {
				final EmpleadoResponse response = empleadoService.post(request);
				proxyResponse.setBody(GSON.toJson(response));
			} else if (proxyRequest.getHttpMethod().equals("PUT") && proxyRequest.getPathParameters().containsKey("codigo")) {
				final EmpleadoResponse response = empleadoService.put(UUID.fromString(proxyRequest.getPathParameters().get("codigo")), request);
				proxyResponse.setBody(GSON.toJson(response));
			} else {
				proxyResponse.setStatusCode(405);
				proxyResponse.setBody(GSON.toJson(Map.of("error", "Método no soportado")));
			}

		} catch (Exception e) {
			LOGGER.log(e.toString());
			if(e instanceof Error) {
				final Error error = (Error)e;
				proxyResponse.setStatusCode(error.getCodigo());
				proxyResponse.setBody(GSON.toJson(error));
			} else {
				proxyResponse.setStatusCode(500);
				proxyResponse.setBody(GSON.toJson(Map.of("error", e.getMessage())));
			}
		}

		return proxyResponse;
	}
}
