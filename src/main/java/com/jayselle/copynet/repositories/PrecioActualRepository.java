package com.jayselle.copynet.repositories;

import com.jayselle.copynet.entities.PrecioActual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrecioActualRepository extends JpaRepository<PrecioActual, Long> {

    @Query("FROM PrecioActual WHERE ?1 BETWEEN fechadesde_precioactual AND fechahasta_precioactual")
    PrecioActual getPrecioByDate(LocalDateTime localDateTime);

    @Override
    @Query("FROM PrecioActual ORDER BY fechadesde_precioactual DESC")
    List<PrecioActual> findAll();
}
