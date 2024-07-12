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
public class ExpedienteResponse {

	private Boolean exito;
	private List<String> errores;
	private Integer codigoRespuesta;
	private String identificador;
	private List<ErrorValidacion> erroresValidacion;

	/**
	 * {
  "exito": true,
  "errores": [
    "string"
  ],
  "codigoRespuesta": 0,
  "identificador": "string",
  "erroresValidacion": [
    {
      "campo": "string",
      "error": "string",
      "ayuda": "string",
      "erroresEstructura": [
        {
          "idEstructura": "string",
          "errores": [
            null
          ]
        }
      ],
      "valoresErroneos": [
        "string"
      ]
    }
  ],
  "datosExpediente": {
    "serie": {
      "identificador": "string",
      "codigo": "string",
      "descripcion": "string",
      "fechaCierre": "2024-07-12T06:30:47.270Z",
      "fechaApertura": "2024-07-12T06:30:47.270Z"
    },
    "cerrado": true,
    "borrador": true,
    "deposito": "string",
    "identificador": "string",
    "archivo": "string",
    "nombreTipoDocumental": "string",
    "tituloTipoDocumental": "string",
    "organismo": "string"
  },
  "metadatos": [
    {
      "nombre": "string",
      "titulo": "string",
      "tipoDato": "TEXTO",
      "valores": [
        {
          "valor": "string",
          "texto": "string"
        }
      ],
      "estructuras": [
        {
          "nombreTipoDocumental": "string",
          "tituloTipoDocumental": "string",
          "identificador": "string",
          "estructurasestructuras": [
            null
          ]
        }
      ],
      "ficheros": [
        {
          "identificador": "string",
          "nombre": "string",
          "mimetype": "string",
          "descripcion": "string"
        }
      ]
    }
  ],
  "actividad": {
    "datosActividad": {
      "codigo": "string",
      "nombreTipoDocumental": "string",
      "tituloTipoDocumental": "string",
      "identificador": "string",
      "valorRepresentativo": "string",
      "organismo": "string"
    },
    "metadatos": [
      {
        "nombre": "string",
        "titulo": "string",
        "tipoDato": "TEXTO",
        "valores": [
          {
            "valor": "string",
            "texto": "string"
          }
        ],
        "estructuras": [
          {
            "nombreTipoDocumental": "string",
            "tituloTipoDocumental": "string",
            "identificador": "string",
            "estructurasestructuras": [
              null
            ]
          }
        ],
        "ficheros": [
          {
            "identificador": "string",
            "nombre": "string",
            "mimetype": "string",
            "descripcion": "string"
          }
        ]
      }
    ]
  },
  "regulaciones": [
    {
      "datosRegulacion": {
        "codigo": "string",
        "nombreTipoDocumental": "string",
        "tituloTipoDocumental": "string",
        "identificador": "string",
        "valorRepresentativo": "string",
        "organismo": "string"
      },
      "metadatos": [
        {
          "nombre": "string",
          "titulo": "string",
          "tipoDato": "TEXTO",
          "valores": [
            {
              "valor": "string",
              "texto": "string"
            }
          ],
          "estructuras": [
            {
              "nombreTipoDocumental": "string",
              "tituloTipoDocumental": "string",
              "identificador": "string",
              "estructurasestructuras": [
                null
              ]
            }
          ],
          "ficheros": [
            {
              "identificador": "string",
              "nombre": "string",
              "mimetype": "string",
              "descripcion": "string"
            }
          ]
        }
      ]
    }
  ],
  "serie": {
    "datosSerie": {
      "identificador": "string",
      "codigo": "string",
      "descripcion": "string",
      "fechaCierre": "2024-07-12T06:30:47.270Z",
      "fechaApertura": "2024-07-12T06:30:47.270Z",
      "idSeriePadre": "string",
      "idCuadroClasificacion": "string",
      "tipo": "EXPEDIENTE",
      "entidad": "SERIE",
      "aprobada": true,
      "nombreTipoDocumental": "string",
      "tituloTipoDocumental": "string",
      "organismo": "string"
    },
    "metadatos": [
      {
        "nombre": "string",
        "titulo": "string",
        "tipoDato": "TEXTO",
        "valores": [
          {
            "valor": "string",
            "texto": "string"
          }
        ],
        "estructuras": [
          {
            "nombreTipoDocumental": "string",
            "tituloTipoDocumental": "string",
            "identificador": "string",
            "estructurasestructuras": [
              null
            ]
          }
        ],
        "ficheros": [
          {
            "identificador": "string",
            "nombre": "string",
            "mimetype": "string",
            "descripcion": "string"
          }
        ]
      }
    ]
  },
  "agentes": [
    {
      "datosAgente": {
        "codigo": "string",
        "nombreTipoDocumental": "string",
        "tituloTipoDocumental": "string",
        "identificador": "string",
        "valorRepresentativo": "string",
        "organismo": "string",
        "motivo": "string"
      },
      "metadatos": [
        {
          "nombre": "string",
          "titulo": "string",
          "tipoDato": "TEXTO",
          "valores": [
            {
              "valor": "string",
              "texto": "string"
            }
          ],
          "estructuras": [
            {
              "nombreTipoDocumental": "string",
              "tituloTipoDocumental": "string",
              "identificador": "string",
              "estructurasestructuras": [
                null
              ]
            }
          ],
          "ficheros": [
            {
              "identificador": "string",
              "nombre": "string",
              "mimetype": "string",
              "descripcion": "string"
            }
          ]
        }
      ]
    }
  ]
}
	 */

	
}
