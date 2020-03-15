package com.jayselle.copynet.repositories;

import com.jayselle.copynet.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {

    @Query("FROM Rol WHERE nombre_rol = ?1")
    Rol getRolByName(String name);

}
