package com.jayselle.copynet.dtos;

import lombok.Data;

@Data
public class PostLineaDto {

    private String encuadernacion_linea_pedido;

    private String plegado_linea_pedido;

    private String color_linea_pedido;

    private Integer copias_linea_pedido;

    private Float precio_linea_pedido;

    private ApunteDto apunte;

}
