package com.jayselle.copynet.repositories;

import com.jayselle.copynet.entities.Catedra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatedraRepository extends JpaRepository<Catedra, Long> {

    @Query("FROM Catedra WHERE nombre_catedra = ?1")
    Optional<Catedra> getCatedraByName(String name);

    @Query("FROM Catedra WHERE nombre_catedra = ?1 AND departamento.nombre_departamento = ?2 AND nivel.nombre_nivel = ?3")
    Optional<Catedra> getCatedraByDepartamentoAndNivel(String nombreCatedra, String nombreDepartamento, String nombreNivel);

    @Query("SELECT c.nombre_catedra FROM Catedra c " +
            "INNER JOIN c.apuntes a " +
            "INNER JOIN a.lineaspedido lp " +
            "INNER JOIN lp.pedido p " +
            "INNER JOIN p.detalleestadospedido dep " +
            "INNER JOIN dep.estadoPedido ep " +
            "WHERE ep.nombre_estado_pedido = 'Cobrado' AND dep.fecha_detalle_estado_pedido IN( " +
            "SELECT MAX(dep.fecha_detalle_estado_pedido) from DetalleEstadoPedido dep " +
            "GROUP BY dep.pedido.id_pedido)")
    List<String> getCatedrasEnPedidosCobrados();

}
