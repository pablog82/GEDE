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
public class DatosAltaFirmantesRequest {

	private String tipoDocumental;
	private List<ValoresMetadato> metadatos;

}
