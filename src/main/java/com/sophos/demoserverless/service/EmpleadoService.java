package com.sophos.demoserverless.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.sophos.demoserverless.beans.EmpleadoRequest;
import com.sophos.demoserverless.beans.EmpleadoResponse;
import com.sophos.demoserverless.beans.Error;
import com.sophos.demoserverless.model.Empleado;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

public class EmpleadoService {

	private static final String HK_PARAMETRO = "EMPLEADO";
	private static final String GSI_CEDULA = "cedula-index";

	private final LambdaLogger LOGGER;
	private final DynamoDBMapper mapper;

	public EmpleadoService(Context context) {
		LOGGER = context.getLogger();

		final Properties properties = new Properties();
		try {
			properties.load(getClass().getClassLoader().getResourceAsStream("demo.properties"));
			LOGGER.log("AWS Region: " + properties.getProperty("aws.region"));
		} catch (IOException e) {
			LOGGER.log("Error cargando las propiedades: " + e);
		}

		final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
			.withRegion(properties.getProperty("aws.region", "us-west-1"))
			.build()
		;

		this.mapper = new DynamoDBMapper(dynamoDB);
	}

	public void get(UUID codigo) {
		final Empleado empleado = mapper.load(Empleado.class, HK_PARAMETRO, codigo.toString());
		if(Objects.isNull(empleado)) {
			throw new Error(404, "No existe el código '" + codigo + "'");
		}
	}

	public EmpleadoResponse post(EmpleadoRequest request) {
		validateCedula(request.getCedula(), null);

		final Empleado empleado = new Empleado();
		empleado.setHk(HK_PARAMETRO);
		empleado.setSk(UUID.randomUUID().toString());
		mapRequest(empleado, request);

		mapper.save(empleado);

		return mapResponse(empleado);
	}

	public EmpleadoResponse put(UUID codigo, EmpleadoRequest request) {
		get(codigo);
		validateCedula(request.getCedula(), codigo);

		final Empleado empleado = new Empleado();
		empleado.setHk(HK_PARAMETRO);
		empleado.setSk(codigo.toString());
		mapRequest(empleado, request);

		mapper.save(empleado);

		return mapResponse(empleado);
	}

	private void mapRequest(Empleado empleado, EmpleadoRequest request) {
		empleado.setCedula(request.getCedula());
		empleado.setNombre(request.getNombre());
		empleado.setEdad(request.getEdad());
		empleado.setCiudad(request.getCiudad());
		empleado.setBusqueda(generateSearchField(empleado));
		LOGGER.log("Request: " + request);
	}

	private EmpleadoResponse mapResponse(Empleado empleado) {
		final EmpleadoResponse response = new EmpleadoResponse();
		response.setCodigo(UUID.fromString(empleado.getSk()));
		response.setCedula(empleado.getCedula());
		response.setNombre(empleado.getNombre());
		response.setEdad(empleado.getEdad());
		response.setCiudad(empleado.getCiudad());
		LOGGER.log("Response: " + response);
		return response;
	}

	private String generateSearchField(Empleado empleado) {
		return String.join(
			" ",
			List.of(
				empleado.getSk(),
				empleado.getCedula(),
				empleado.getNombre(),
				String.valueOf(empleado.getEdad()),
				empleado.getCiudad()
			)
		)
			.strip()
			.toLowerCase()
		;
	}

	private void validateCedula(String cedula, UUID codigo) {
		final Empleado empleado = new Empleado();
		empleado.setCedula(cedula);

		final DynamoDBQueryExpression<Empleado> queryExpression = new DynamoDBQueryExpression<>();
		queryExpression.setHashKeyValues(empleado);
		queryExpression.setIndexName(GSI_CEDULA);
		queryExpression.setConsistentRead(false);

		final List<Empleado> empleados = mapper.query(Empleado.class, queryExpression);
		if(!empleados.isEmpty() && (Objects.isNull(codigo) || !empleados.get(0).getSk().equals(codigo.toString()))) {
			throw new Error(409, "Ya existe la cédula '" + cedula + "'");
		}
	}

}
