package com.babelgroup.gede.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Registro {

    private Integer id;
    private String expediente;
    private String identificadorExpediente;
    private String documento;
    private String referenciaRepositorio;
    private String observaciones;
    private String estado; //1. OK, 2. KO

    public Registro(String expediente, String identificadorExpediente, String documento, String referenciaRepositorio, String observaciones,
                    String estado) {
        super();
        this.expediente = expediente;
        this.documento = documento;
        this.referenciaRepositorio = referenciaRepositorio;
        this.observaciones = observaciones;
        this.estado = estado;
    }

}
