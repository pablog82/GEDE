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
	
	
	
	
	
/*
 * 
 * {
  "deposito": "DEPO_OFICINA",
  "tipoDocumental": "idoc:type_22",
  "borrador": false,
  "metadatos": [
    {
      "nombre": "nombreNaturalExpediente",
      "valores": ["Expediente de prueba carga Expedientes HistOricos"]
    },
    {
      "nombre": "formatoExpediente",
      "valores": ["Electronico"]
    },
    {
      "nombre": "idoc:field_22_9",
      "valores": ["L01410917"]
    },
    {
      "nombre": "idoc:field_22_4",
      "valores": ["SAETAS"]
    },
    {
      "nombre": "idExpediente",
      "valores": ["008/2001/HST/Archivo-1"]
    },
    {
      "nombre": "idoc:field_22_13",
      "valores": ["008/2001"]
    },
    {
      "nombre": "serieExpediente",
      "valores": ["00S170003410384931769341013709"]
    }
  ]
}
 * 
 */
}
