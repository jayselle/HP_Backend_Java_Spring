package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.Alumno;
import com.jayselle.copynet.entities.Usuario;

public interface UsuarioService {

    String login(String username, String password);
    Usuario findById(Integer id);
    Usuario findByUserId(Long id);
    String registrarUsuario(Alumno alumno);
    Alumno getAlumno(Long legajo);
    Alumno getAlumnoByIdUsuario(Long id);
    void habilitarAlumno(Long legajo);
    void registrarInhabilitaciones();
}
