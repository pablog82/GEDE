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
public class DatosAltaDocumentoRequest {

	private String deposito;
	private String tipoDocumental;
	private List<ValoresMetadato> metadatos;
	private Boolean borrador;
	private String identificadorBinario;// op
	private String formatoBinario;
	
	
	
//	{
//		  "deposito": "???",
//		  "tipoDocumental": "idoc:type_23", 
//		  "metadatos": [ //obligatorios
//		    {
//		      "nombre": [
//		        "doc:field_23_1",
//				"idoc:field_23_3",
//				"idoc:field_23_4",
//				"idoc:field_23_6",
//				"accesibilidadDocumento",
//				"fechaAltaDocumento",
//				"formatoDocumento",
//				"nombreNaturalDocumento"
//		      ]
//		    }
//		  ],
//		  "borrador": false,
//		  "identificadorBinario": "string",
//		  "formatoBinario": "PDF", //obligatorio
//		}
}
