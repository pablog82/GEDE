package com.babelgroup.gede.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Metadato {
	private String nombre;
	private String[] valores;
}
