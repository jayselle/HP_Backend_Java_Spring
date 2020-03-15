package com.jayselle.copynet.repositories;

import com.jayselle.copynet.entities.Alumno;
import com.jayselle.copynet.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    @Query(value = "FROM Usuario u where u.user_usuario = ?1")
    Optional<Usuario> findByUserName(String username);

    @Query(value = "FROM Usuario u where u.id_usuario = ?1")
    Usuario findByUserId(Long id);

    @Query("FROM Alumno WHERE legajo = ?1")
    Optional<Alumno> findByAlumnoLegajo(Long legajo);

    @Query("FROM Alumno WHERE user_usuario = ?1")
    Optional<Alumno> findAlumnoByUsername(String username);

    @Query("FROM Usuario WHERE email_usuario = ?1")
    Optional<Usuario> findByUserEmail(String email);

    @Query("FROM Alumno")
    List<Alumno> getAllAlumnos();

    @Query("SELECT COUNT(*) FROM Alumno")
    Integer getCantAlumnosRegistrados();

    @Query("FROM Alumno WHERE id_usuario = ?1")
    Optional<Alumno> getAlumnoByIdUsuario(Long id);

}
