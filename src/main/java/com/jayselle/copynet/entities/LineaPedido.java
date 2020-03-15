package com.jayselle.copynet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="lineapedido")
public class LineaPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_linea_pedido")
    private Long id_linea_pedido;

    @Column(name="encuadernacion_linea_pedido")
    private String encuadernacion_linea_pedido;

    @Column(name="plegado_linea_pedido")
    private String plegado_linea_pedido;

    @Column(name="color_linea_pedido")
    private String color_linea_pedido;

    @Column(name="copias_linea_pedido")
    private Integer copias_linea_pedido;

    @ManyToOne
    @JoinColumn(name="id_apunte")
    private Apunte apunte;

    @ManyToOne
    @JoinColumn(name="id_pedido")
    private Pedido pedido;
}
