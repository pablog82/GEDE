package com.babelgroup.gede.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
