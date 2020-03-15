package com.jayselle.copynet.dtos;

import lombok.Data;

@Data
public class ApunteDto {

    private Long id_apunte;

    private String titulo_apunte;

    private String autor_apunte;

    private Integer cantidad_paginas_apunte;

    private Boolean estado_apunte;

    private String nombre_catedra;

    private String nombre_departamento;

    private String nombre_nivel;

    private String nombre_tipo_apunte;

}
