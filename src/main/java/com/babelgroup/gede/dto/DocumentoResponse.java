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

	
//	private DatosDocumento datosDocumento;
//	private List<Metadato> metadatos;
//	private SerieRelacionada serie;
	
//	{
//	    "datosDocumento": {
//	        "series": [
//	            {
//	                "codigo": "SERIE",
//	                "descripcion": "Serie documental por defecto",
//	                "fechaApertura": null,
//	                "fechaCierre": null,
//	                "identificador": "00S170003410384931769341013709"
//	            }
//	        ],
//	        "nombreTipoDocumental": "idoc:type_23",
//	        "archivo": "OFICINA",
//	        "tituloTipoDocumental": "Documento GONCE",
//	        "borrador": false,
//	        "formatoBinario": {
//	            "codigo": "PDF",
//	            "descripcion": "PDF (.pdf)"
//	        },
//	        "firmaAutoContenida": false,
//	        "firmantes": false,
//	        "trabajo": true,
//	        "deposito": "DEPO_OFICINA",
//	        "formatoFirma": null,
//	        "completo": false,
//	        "identificador": "00D170297989841006146301871594",
//	        "organismo": "SAETAS"
//	    },
//	"erroresValidacion": [],
//    "identificador": "00D170297989841006146301871594",
//    "codigoRespuesta": 201,
//    "exito": true,
//    "errores": [],
//	    "serie": null,
//	    "metadatos": [
//	        {
//	            "valores": [],
//	            "titulo": "Expedientes",
//	            "tipoDato": "EXPEDIENTE",
//	            "nombre": "expedienteDoc",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Fecha de digitalización orgánica",
//	            "tipoDato": "FECHA",
//	            "nombre": "fechaDigitalizacionDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": ".pdf",
//	                    "texto": ".pdf"
//	                }
//	            ],
//	            "titulo": "Extensión del documento",
//	            "tipoDato": "TEXTO",
//	            "nombre": "extensionFichero",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "SHA-512",
//	                    "texto": "SHA-512"
//	                }
//	            ],
//	            "titulo": "Función hash",
//	            "tipoDato": "TEXTO",
//	            "nombre": "funcionHash",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Fecha cierre",
//	            "tipoDato": "FECHA",
//	            "nombre": "fechaCierreDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "application/pdf",
//	                    "texto": "application/pdf"
//	                }
//	            ],
//	            "titulo": "Mimetype fichero",
//	            "tipoDato": "TEXTO",
//	            "nombre": "mimetypeFichero",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Número de registro",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_10",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Oficina de registro",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_11",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Firmantes",
//	            "tipoDato": "FIRMA",
//	            "nombre": "firmaDoc",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Tipo de registro",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_12",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "PEOPLENET",
//	                    "texto": "PEOPLENET"
//	                }
//	            ],
//	            "titulo": "Unidad productora",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_13",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "PDF",
//	                    "texto": "PDF"
//	                }
//	            ],
//	            "titulo": "Nombre del formato",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_14",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Entidad",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_15",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Fecha secretaria",
//	            "tipoDato": "FECHA",
//	            "nombre": "idoc:field_23_16",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Libro",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_17",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "prueba documento.pdf",
//	                    "texto": "prueba documento.pdf"
//	                }
//	            ],
//	            "titulo": "Nombre fichero",
//	            "tipoDato": "TEXTO",
//	            "nombre": "nombreFichero",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Número secretaría",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_18",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "es",
//	                    "texto": "es"
//	                }
//	            ],
//	            "titulo": "Idioma",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idiomaDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Unidad orgánica",
//	            "tipoDato": "TEXTO",
//	            "nombre": "unidadOrganicaDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "bb3f0424158f3fcf62b756a0c3ae3ff90c3c9bf1c2c5271c024be9c6b53ff05a86310b8e66d333538da28b423ddec73608632f130aff224f9ffdf4d2a3cb1948",
//	                    "texto": "bb3f0424158f3fcf62b756a0c3ae3ff90c3c9bf1c2c5271c024be9c6b53ff05a86310b8e66d333538da28b423ddec73608632f130aff224f9ffdf4d2a3cb1948"
//	                }
//	            ],
//	            "titulo": "Valor hash",
//	            "tipoDato": "TEXTO",
//	            "nombre": "valorHash",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "32598",
//	                    "texto": "32598"
//	                }
//	            ],
//	            "titulo": "Tamaño del fichero",
//	            "tipoDato": "NUMERO",
//	            "nombre": "tamanoFichero",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "2023-12-19T10:58:18+01:00",
//	                    "texto": "2023-12-19T10:58:18+01:00"
//	                }
//	            ],
//	            "titulo": "Fecha creación",
//	            "tipoDato": "FECHA",
//	            "nombre": "fechaAltaDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "R",
//	                    "texto": "Restringido"
//	                }
//	            ],
//	            "titulo": "Accesibilidad del documento",
//	            "tipoDato": "TEXTO",
//	            "nombre": "accesibilidadDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Documento origen",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_5",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "0",
//	                    "texto": "0"
//	                }
//	            ],
//	            "titulo": "Origen",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_4",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "L01410917",
//	                    "texto": "L01410917"
//	                }
//	            ],
//	            "titulo": "Órgano",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_3",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "ES_L01410917_2023_00D170297989841006146301871594",
//	                    "texto": "ES_L01410917_2023_00D170297989841006146301871594"
//	                }
//	            ],
//	            "titulo": "Identificador ENI",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_2",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "TD99",
//	                    "texto": "TD99"
//	                }
//	            ],
//	            "titulo": "Tipo documental",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_1",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "00D170297989841006146301871594",
//	                    "texto": "00D170297989841006146301871594"
//	                }
//	            ],
//	            "titulo": "Identificador",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "CSV del documento",
//	            "tipoDato": "TEXTO",
//	            "nombre": "csvDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Número de páginas",
//	            "tipoDato": "NUMERO",
//	            "nombre": "numeroPaginasDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "Electronico",
//	                    "texto": "Electronico"
//	                }
//	            ],
//	            "titulo": "Formato",
//	            "tipoDato": "TEXTO",
//	            "nombre": "formatoDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Fecha de registro",
//	            "tipoDato": "FECHA",
//	            "nombre": "idoc:field_23_9",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e",
//	                    "texto": "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e"
//	                }
//	            ],
//	            "titulo": "Versión NTI",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_8",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "00S170003410384931769341013709",
//	                    "texto": "SERIE - Serie documental por defecto"
//	                }
//	            ],
//	            "titulo": "Serie",
//	            "tipoDato": "SERIE",
//	            "nombre": "serieDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [],
//	            "titulo": "Tipo de firma",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_7",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "El nombre del documento",
//	                    "texto": "El nombre del documento"
//	                }
//	            ],
//	            "titulo": "Nombre Natural",
//	            "tipoDato": "TEXTO",
//	            "nombre": "nombreNaturalDocumento",
//	            "estructuras": [],
//	            "ficheros": []
//	        },
//	        {
//	            "valores": [
//	                {
//	                    "valor": "EE99",
//	                    "texto": "EE99"
//	                }
//	            ],
//	            "titulo": "Estado de elaboración",
//	            "tipoDato": "TEXTO",
//	            "nombre": "idoc:field_23_6",
//	            "estructuras": [],
//	            "ficheros": []
//	        }
//	    ]
//	    
//	}
	
}
