package com.jayselle.copynet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="apunte")
public class Apunte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_apunte")
    private Long id_apunte;

    @Column(name="titulo_apunte")
    private String titulo_apunte;

    @Column(name="autor_apunte")
    private String autor_apunte;

    @Column(name="cantidad_paginas_apunte")
    private Integer cantidad_paginas_apunte;

    @Column(name="estado_apunte")
    private Boolean estado_apunte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_catedra")
    private Catedra catedra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_tipo_apunte")
    private TipoApunte tipoApunte;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="id_apunte")
    private List<LineaPedido> lineaspedido;
}
