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
public class SerieRelacionada {
	private SerieConfiguracion datosSerie;
	private List<ValoresMetadato> metadatos;
}
