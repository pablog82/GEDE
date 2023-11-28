package com.babelgroup.gede.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ErrorValidacion {

	private String campo;
	private String error;
	private String ayuda;

}
