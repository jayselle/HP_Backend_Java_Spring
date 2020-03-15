package com.jayselle.copynet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tipoapunte")
public class TipoApunte implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_tipo_apunte")
    private Long id_tipo_apunte;

    @Column(name="nombre_tipo_apunte")
    private String nombre_tipo_apunte;
}
