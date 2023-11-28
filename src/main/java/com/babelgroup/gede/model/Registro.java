package com.babelgroup.gede.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Registro {

	private Integer id;
	private String expediente;
	private String documento;
	private String referenciaRepositorio;
	private String observaciones;
	private Integer estado;

	public Registro(String expediente, String documento, String referenciaRepositorio, String observaciones,
			Integer estado) {
		super();
		this.expediente = expediente;
		this.documento = documento;
		this.referenciaRepositorio = referenciaRepositorio;
		this.observaciones = observaciones;
		this.estado = estado;
	}

}
