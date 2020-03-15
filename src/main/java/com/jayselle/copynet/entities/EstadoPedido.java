package com.jayselle.copynet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="estadopedido")
public class EstadoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_estado_pedido")
    private Long id_estado_pedido;

    @Column(name="nombre_estado_pedido")
    private String nombre_estado_pedido;
}
