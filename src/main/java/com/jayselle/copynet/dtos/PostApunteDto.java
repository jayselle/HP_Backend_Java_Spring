package com.jayselle.copynet.dtos;

import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@GroupSequence({PostApunteDto.class, PostApunteDto.BasicInfo.class, PostApunteDto.AdvancedInfo.class})
public class PostApunteDto {

    @NotBlank(message = "Titulo: Completar")
    @Size(min = 3, message = "Titulo: Mínimo 3 caracteres", groups = PostApunteDto.BasicInfo.class)
    @Size(max = 40, message = "Titulo: Máximo 40 caracteres", groups = PostApunteDto.BasicInfo.class)
    @Pattern(regexp = "^[\\S].+", message = "Titulo: Sin espacio en blanco al principio", groups = PostApunteDto.AdvancedInfo.class)
    private String titulo_apunte;

    @NotBlank(message = "Autor: Completar")
    @Size(min = 3, message = "Autor: Mínimo 3 caracteres", groups = PostApunteDto.BasicInfo.class)
    @Size(max = 20, message = "Autor: Máximo 20 caracteres", groups = PostApunteDto.BasicInfo.class)
    @Pattern(regexp = "^[\\S].+", message = "Autor: Sin espacio en blanco al principio", groups = PostApunteDto.AdvancedInfo.class)
    private String autor_apunte;

    @NotBlank(message = "Paginas: Completar")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Paginas: Solo numero positivo", groups = PostApunteDto.BasicInfo.class)
    private String cantidad_paginas_apunte;

    @NotBlank(message = "Catedra: Completar")
    private String nombre_catedra;

    @NotBlank(message = "Tipo de Apunte: Completar")
    private String tipoapunte;

    private String nombre_departamento;

    private String nombre_nivel;

    static interface BasicInfo {

    }

    static interface AdvancedInfo {

    }

}
