package com.jayselle.copynet.repositories;

import com.jayselle.copynet.entities.Apunte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApunteRepository extends JpaRepository<Apunte,Long> {

    @Query("FROM Apunte WHERE titulo_apunte = ?1")
    Optional<Apunte> getApunteByTitulo(String titulo);

    @Query("SELECT COUNT(*) FROM Apunte")
    Integer getCantApuntesRegistrados();

    @Query("SELECT a.titulo_apunte FROM Apunte a " +
            "INNER JOIN a.lineaspedido lp " +
            "INNER JOIN lp.pedido p " +
            "INNER JOIN p.detalleestadospedido dep " +
            "INNER JOIN dep.estadoPedido ep " +
            "WHERE ep.nombre_estado_pedido = 'Cobrado' AND dep.fecha_detalle_estado_pedido IN( " +
            "SELECT MAX(dep.fecha_detalle_estado_pedido) from DetalleEstadoPedido dep " +
            "GROUP BY dep.pedido.id_pedido)")
    List<String> getApuntesEnPedidosCobrados();

}
