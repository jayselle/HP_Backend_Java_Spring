package com.jayselle.copynet.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="usuario")
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_usuario")
    private Long id_usuario;

    @Column(name="nombre_usuario")
    private String nombre_usuario;

    @Column(name="apellido_usuario")
    private String apellido_usuario;

    @Column(name="dni_usuario")
    private String dni_usuario;

    @Column(name="user_usuario")
    private String user_usuario;

    @Column(name="email_usuario")
    private String email_usuario;

    @Column(name="celular_usuario")
    private String celular_usuario;

    @Column(name="password_usuario")
    private String password_usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_rol")
    private Rol rol;

    @OneToMany
    @JoinColumn(name="id_usuario")
    private List<Pedido> pedidos;

    public Usuario(String nombre, String apellido, String dni, String username, String password, String email, String celular){
        this.nombre_usuario = nombre;
        this.apellido_usuario = apellido;
        this.dni_usuario = dni;
        this.user_usuario = username;
        this.password_usuario = password;
        this.email_usuario = email;
        this.celular_usuario = celular;
    }

}
