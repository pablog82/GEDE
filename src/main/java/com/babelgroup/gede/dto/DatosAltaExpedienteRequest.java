package com.babelgroup.gede.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class DatosAltaExpedienteRequest {

	private String deposito;
	private String tipoDocumental;
	private List<Metadato> metadatos;
	private Boolean borrador;
	
}
