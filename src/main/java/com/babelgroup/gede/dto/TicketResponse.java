package com.babelgroup.gede.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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

	public TicketResponse(Boolean exito, List<String> errores, Integer codigoRespuesta, String ticket, String usuario, String organismo, Date caducidad, Boolean valido, String identificadorUsuario, String nombreCompleto) {
		this.exito = exito;
		this.errores = errores;
		this.codigoRespuesta = codigoRespuesta;
		this.ticket = ticket;
		this.usuario = usuario;
		this.organismo = organismo;
		this.caducidad = caducidad;
		this.valido = valido;
		this.identificadorUsuario = identificadorUsuario;
		this.nombreCompleto = nombreCompleto;
	}
}
