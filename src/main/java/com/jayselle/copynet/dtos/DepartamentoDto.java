package com.jayselle.copynet.dtos;

import lombok.Data;

import java.util.List;

@Data
public class DepartamentoDto {

    private String nombre_departamento;

    private List<NivelDto> niveles;

}
