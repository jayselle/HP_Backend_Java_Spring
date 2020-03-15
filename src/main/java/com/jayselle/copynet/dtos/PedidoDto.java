package com.jayselle.copynet.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PedidoDto {

    private Long id_pedido;

    private Float precio_pedido;

    private String comentario_pedido;

    private List<PostLineaDto> lineas;

    private String fecha_pedido;

    private String estado_pedido;

}
