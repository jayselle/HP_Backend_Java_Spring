package com.jayselle.copynet.repositories2;

import com.jayselle.copynet.entities2.UAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UAlumnoRepository extends JpaRepository<UAlumno, Long> {

    @Query("FROM UAlumno WHERE legajo_ualumno = ?1 AND dni_ualumno = ?2")
    Optional<UAlumno> getUAlumnoByLegajoAndDNI(Long legajo, String dni);

    @Query("SELECT COUNT(*) FROM UAlumno")
    Integer getCantUAlumnos();

}
