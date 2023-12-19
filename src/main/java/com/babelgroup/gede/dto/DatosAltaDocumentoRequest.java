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
	private String identificadorExpediente;
	private String identificadorBinario;
	private String formatoBinario;
	private String tipoDocumental;
	private List<Metadato> metadatos;
	private Boolean borrador;
	private Boolean trabajo;
	
	
	
	
	
//	{
//    "deposito": "DEPO_OFICINA",
//    "identificadorExpediente": null,
//    "identificadorBinario": "08CBDFF018F9462EA59693177867E53C",
//    "formatoBinario": "PDF",
//"tipoDocumental": "idoc:type_23",
//"borrador": false,
//"trabajo": true		
//    "metadatos": [
//        {
//            "nombre": "serieDocumento",
//            "valores": [
//                "00S170003410384931769341013709"
//            ]
//        },
//        {
//            "nombre": "nombreFichero",
//            "valores": [
//                "prueba documento.pdf"
//            ]
//        },
//        {
//            "nombre": "idoc:field_23_1",
//            "valores": ["TD99"]
//        },
//        {
//            "nombre": "idiomaDocumento",
//            "valores": [
//                "es"
//            ]
//        },
//        {
//            "nombre": "fechaAltaDocumento",
//            "valores": [
//                "2023-12-01T13:18:16+01:00"
//            ]
//        },
//        {
//            "nombre": "idoc:field_23_13",
//            "valores": [
//                "PEOPLENET"
//            ]
//        },
//        {
//            "nombre": "idoc:field_23_14",
//            "valores": [
//                "PDF"
//            ]
//        },
//        {
//            "nombre": "idoc:field_23_3",
//            "valores": [
//                "L01410917"
//            ]
//        },
//        {
//            "nombre": "idoc:field_23_4",
//            "valores": [
//                "0"
//            ]
//        },
//        {
//            "nombre": "idoc:field_23_6",
//            "valores": [
//                "EE99"
//            ]
//        },
//        {
//            "nombre": "accesibilidadDocumento",
//            "valores": [
//                "R"
//            ]
//        },
//        {
//            "nombre": "formatoDocumento",
//            "valores": [
//                "Electronico"
//            ]
//        },
//        {
//            "nombre": "nombreNaturalDocumento",
//            "valores": [
//                "El nombre del documento"
//            ]
//        },
//        {
//            "nombre": "idoc:field_23_8",
//            "valores": [
//                "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e"
//                       ]
//        }
//    ]
//}
}
