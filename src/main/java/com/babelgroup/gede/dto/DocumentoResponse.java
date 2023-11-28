package com.babelgroup.gede.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class DocumentoResponse {

	private Boolean exito;
	private List<String> errores;
	private Integer codigoRespuesta;
	private String identificador;
	private List<ErrorValidacion> erroresValidacion;
	private DatosDocumento datosDocumento;
	private List<ValoresMetadato> metadatos;
	private SerieRelacionada serie;
}
