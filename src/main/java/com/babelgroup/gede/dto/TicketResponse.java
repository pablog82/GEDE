package com.babelgroup.gede.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TicketResponse {
	private Boolean exito;

	private List<String> errores;
	private Integer codigoRespuesta;
	private String ticket;
	private String usuario;
	private String organismo;
	private Date caducidad;
	private Boolean valido;
	private String identificadorUsuario;
	private String nombreCompleto;
}
