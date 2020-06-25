package com.sophos.demoserverless.beans;

import java.util.UUID;

public class EmpleadoResponse {

	private UUID codigo;
	private String cedula;
	private String nombre;
	private Integer edad;
	private String ciudad;

	@Override
	public String toString() {
		return "EmpleadoResponse{" +
			"codigo=" + codigo +
			", cedula='" + cedula + '\'' +
			", nombre='" + nombre + '\'' +
			", edad=" + edad +
			", ciudad='" + ciudad + '\'' +
			'}';
	}

	public UUID getCodigo() {
		return codigo;
	}

	public void setCodigo(UUID codigo) {
		this.codigo = codigo;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

}
