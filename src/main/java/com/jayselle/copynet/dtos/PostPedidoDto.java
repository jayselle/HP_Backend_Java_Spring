package com.jayselle.copynet.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PostPedidoDto {

    private Float precio_pedido;

    private String comentario_pedido;

    private List<PostLineaDto> lineas;

}
