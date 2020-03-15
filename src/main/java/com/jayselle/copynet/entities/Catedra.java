package com.jayselle.copynet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="catedra")
public class Catedra implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_catedra")
    private Long id_catedra;

    @Column(name="nombre_catedra")
    private String nombre_catedra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_departamento")
    private Departamento departamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_nivel")
    private Nivel nivel;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="id_catedra")
    private List<Apunte> apuntes;
}
