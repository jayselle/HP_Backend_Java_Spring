package com.jayselle.copynet.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name="alumno")
@PrimaryKeyJoinColumn(name="id_usuario")
public class Alumno extends Usuario{

    @Column(name="legajo")
    private Long legajo;

    @Column(name="habilitado")
    private Boolean habilitado;

    @Column(name="fecha_habilitacion")
    private LocalDateTime fecha_habilitacion;

    public Alumno(String nombre, String apellido, String dni, String username, String password, String email, String celular, Long legajo, Boolean estado, LocalDateTime fechaHabilitacion){
        super(nombre,apellido,dni,username,password,email,celular);
        this.legajo = legajo;
        this.habilitado = estado;
        this.fecha_habilitacion = fechaHabilitacion;
    }
}
