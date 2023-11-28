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
public class DatosAltaDocumentoRequest {

	private String deposito;
	private String identificadorExpediente; // op
	private String identificadorCarpeta;// op
	private String rutaCarpeta;// op
	private String tipoDocumental;
	private List<ValoresMetadato> metadatos;
	private String identificadorBinario;// op
	private String formatoBinario;
	
}
