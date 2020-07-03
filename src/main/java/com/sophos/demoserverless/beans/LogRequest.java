package com.sophos.demoserverless.beans;

public class LogRequest {

	private String responsable;
	private String metodo;
	private String codigo;
	private EmpleadoResponse entidad;

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public EmpleadoResponse getEntidad() {
		return entidad;
	}

	public void setEntidad(EmpleadoResponse entidad) {
		this.entidad = entidad;
	}

}
