package com.jayselle.copynet.repositories;

import com.jayselle.copynet.entities.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Long> {

    @Query("FROM EstadoPedido WHERE nombre_estado_pedido = ?1")
    EstadoPedido getEstadoPedido(String estadoPedido);

}
