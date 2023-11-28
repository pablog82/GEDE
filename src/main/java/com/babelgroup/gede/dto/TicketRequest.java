package com.babelgroup.gede.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TicketRequest {
	private String usuario;
	private String organismo;
	private String password;
}
