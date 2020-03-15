package com.jayselle.copynet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="nivel")
public class Nivel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_nivel")
    private Long id_nivel;

    @Column(name="nombre_nivel")
    private String nombre_nivel;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "niveles")
    private List<Departamento> departamentos = new ArrayList<>();
}
