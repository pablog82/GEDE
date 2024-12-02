package com.babelgroup.gede.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SerieConfiguracion {

	private String identificador;

	private String codigo;

	private String descripcion;

	private Date fechaCierre; // date-time

	private Date fechaApertura; // date-time

	private String idSeriePadre;

	private String idCuadroClasificacion;

}
