package com.jayselle.copynet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="detalleestadopedido")
public class DetalleEstadoPedido implements Comparable<DetalleEstadoPedido>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_detalle_estado_pedido")
    private Long id_detalle_estado_pedido;

    @Column(name="fecha_detalle_estado_pedido")
    private LocalDateTime fecha_detalle_estado_pedido;

    @ManyToOne
    @JoinColumn(name="id_estado_pedido")
    private EstadoPedido estadoPedido;

    @ManyToOne
    @JoinColumn(name="id_pedido")
    private Pedido pedido;

    @Override
    public int compareTo(DetalleEstadoPedido o) {
        return getFecha_detalle_estado_pedido().compareTo(o.getFecha_detalle_estado_pedido());
    }
}
