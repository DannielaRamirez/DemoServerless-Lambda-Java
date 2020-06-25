package com.sophos.demoserverless.beans;

public class Error extends RuntimeException {

	private int codigo;
	private String error;

	public Error(int codigo, String error) {
		super(error);
		this.codigo = codigo;
		this.error = error;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
