package com.sophos.demoserverless.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sophos.demoserverless.beans.EmpleadoResponse;
import com.sophos.demoserverless.beans.LogRequest;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class SqsService {

	private static final String MESSAGE_GROUP_ID = "5BfpLY9VkP6s5CaHaJJsLFnCAdxZN2FQbnXJpZdf";

	private final AmazonSQS sqs;
	private final LambdaLogger LOGGER;
	private final ObjectMapper objectMapper;
	private final String sqsQueueUrl;

	public SqsService(Context context) {
		LOGGER = context.getLogger();

		final Properties properties = new Properties();
		try {
			properties.load(getClass().getClassLoader().getResourceAsStream("demo.properties"));
			LOGGER.log("AWS SQS QUEUE URL: " + properties.getProperty("aws.sqsurl"));
		} catch (IOException e) {
			LOGGER.log("Error cargando las propiedades: " + e);
		}

		sqs = AmazonSQSClientBuilder.standard()
			.withRegion(properties.getProperty("aws.region"))
			.build()
		;

		sqsQueueUrl = properties.getProperty("aws.sqsurl");

		objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
	}

	public void queueLog(EmpleadoResponse empleado, String responsable, String metodo) {
		final LogRequest logRequest = new LogRequest();
		logRequest.setResponsable(responsable);
		logRequest.setMetodo(metodo);
		logRequest.setCodigo(empleado.getCodigo().toString());
		logRequest.setEntidad(empleado);

		try {
			final SendMessageRequest messageRequest = new SendMessageRequest()
				.withQueueUrl(sqsQueueUrl)
				.withMessageGroupId(MESSAGE_GROUP_ID)
				.withMessageDeduplicationId(UUID.randomUUID().toString())
				.withMessageBody(objectMapper.writeValueAsString(logRequest))
				;
			sqs.sendMessage(messageRequest);
		} catch (JsonProcessingException | AmazonServiceException e) {
			LOGGER.log("Error encolando el log de operaciones -> " + e.getLocalizedMessage());
		}
	}

}
