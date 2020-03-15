package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.Pedido;

import java.util.List;

public interface PedidoService {

    List<Pedido> getPedidosByEstado(String nombreEstado);
    void agregarEstadoPedido(Long idPedido, String nuevoEstado);
    List<Pedido> getPedidosImpresosByLegajo(Long legajo);
    void registrarPedido(Pedido pedido);
    void cancelarPedido(Long idPedido, Long idUsuario);
    void registrarVencimientos();
    List<Pedido> getPedidosByEstadoAndLegajo(String estado, Long legajo);

}
