package com.jayselle.copynet.repositories;

import com.jayselle.copynet.entities.TipoApunte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoApunteRepository extends JpaRepository<TipoApunte, Long> {

    @Query("FROM TipoApunte WHERE nombre_tipo_apunte = ?1")
    Optional<TipoApunte> getTipoApunteByName(String name);

}
