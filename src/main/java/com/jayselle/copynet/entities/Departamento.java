package com.jayselle.copynet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="departamento")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_departamento")
    private Long id_departamento;

    @Column(name="nombre_departamento")
    private String nombre_departamento;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="departamento_nivel",
            joinColumns = {@JoinColumn(name="id_departamento")},
            inverseJoinColumns = {@JoinColumn(name="id_nivel")}
    )
    private List<Nivel> niveles = new ArrayList<>();
}
