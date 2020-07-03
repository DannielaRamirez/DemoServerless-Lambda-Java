package com.sophos.demoserverless.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.sophos.demoserverless.beans.EmpleadoRequest;
import com.sophos.demoserverless.beans.EmpleadoResponse;
import com.sophos.demoserverless.beans.Error;
import com.sophos.demoserverless.model.Empleado;
import com.sophos.demoserverless.repository.EmpleadoRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EmpleadoService {

	private final EmpleadoRepository empleadoRepository;
	private final SqsService sqsService;

	public EmpleadoService(Context context) {
		empleadoRepository = new EmpleadoRepository(context);
		sqsService = new SqsService(context);
	}

	public void get(UUID codigo) {
		empleadoRepository.findById(codigo)
			.orElseThrow(() -> new Error(404, "No existe el código '" + codigo + "'"));
	}

	public EmpleadoResponse post(EmpleadoRequest request) {
		validateCedula(request.getCedula(), null);

		final Empleado empleado = new Empleado();
		empleado.setHk(EmpleadoRepository.HK_PARAMETRO);
		empleado.setSk(UUID.randomUUID().toString());
		mapRequest(empleado, request);

		final EmpleadoResponse response = mapResponse(empleadoRepository.save(empleado));

		sqsService.queueLog(response, "", "POST");

		return response;
	}

	public EmpleadoResponse put(UUID codigo, EmpleadoRequest request) {
		get(codigo);
		validateCedula(request.getCedula(), codigo);

		final Empleado empleado = new Empleado();
		empleado.setHk(EmpleadoRepository.HK_PARAMETRO);
		empleado.setSk(codigo.toString());
		mapRequest(empleado, request);

		final EmpleadoResponse response = mapResponse(empleadoRepository.save(empleado));

		sqsService.queueLog(response, "", "PUT");

		return response;
	}

	private void mapRequest(Empleado empleado, EmpleadoRequest request) {
		empleado.setCedula(request.getCedula());
		empleado.setNombre(request.getNombre());
		empleado.setEdad(request.getEdad());
		empleado.setCiudad(request.getCiudad());
		empleado.setBusqueda(generateSearchField(empleado));
	}

	private EmpleadoResponse mapResponse(Empleado empleado) {
		final EmpleadoResponse response = new EmpleadoResponse();
		response.setCodigo(UUID.fromString(empleado.getSk()));
		response.setCedula(empleado.getCedula());
		response.setNombre(empleado.getNombre());
		response.setEdad(empleado.getEdad());
		response.setCiudad(empleado.getCiudad());
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
		final List<Empleado> empleados = empleadoRepository.findByCedula(cedula);
		if(!empleados.isEmpty() && (Objects.isNull(codigo) || !empleados.get(0).getSk().equals(codigo.toString()))) {
			throw new Error(409, "Ya existe la cédula '" + cedula + "'");
		}
	}

}
