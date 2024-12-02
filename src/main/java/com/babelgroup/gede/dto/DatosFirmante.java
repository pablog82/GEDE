package com.babelgroup.gede.dto;

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
public class DatosFirmante {

	private String identificador;
	private String nombreTipoDocumental;
	private String tituloTipoDocumental;
	private String fechaIncorporacion;
}
