package com.sophos.demoserverless.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "DemoServerless")
public class Empleado {

	@DynamoDBHashKey
	private String hk;

	@DynamoDBRangeKey
	private String sk;

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "cedula-index")
	private String cedula;

	@DynamoDBAttribute
	private String nombre;

	@DynamoDBAttribute
	private Integer edad;

	@DynamoDBAttribute
	private String ciudad;

	@DynamoDBAttribute
	private String busqueda;

	public String getHk() {
		return hk;
	}

	public void setHk(String hk) {
		this.hk = hk;
	}

	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
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

	public String getBusqueda() {
		return busqueda;
	}

	public void setBusqueda(String busqueda) {
		this.busqueda = busqueda;
	}

}
