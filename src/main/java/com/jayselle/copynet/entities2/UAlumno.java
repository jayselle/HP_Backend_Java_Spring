package com.jayselle.copynet.entities2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alumno")
public class UAlumno {

    @Id
    @Column(name="legajo_ualumno")
    private Long legajo_ualumno;

    @Column(name="dni_ualumno")
    private String dni_ualumno;

    @Column(name="nombre_ualumno")
    private String nombre_ualumno;

    @Column(name="apellido_ualumno")
    private String apellido_ualumno;
}
