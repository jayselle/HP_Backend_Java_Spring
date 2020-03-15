package com.jayselle.copynet.repositories;

import com.jayselle.copynet.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("FROM Pedido p " +
            "INNER JOIN p.detalleestadospedido dep " +
            "INNER JOIN dep.estadoPedido ep " +
            "WHERE ep.nombre_estado_pedido = ?1 AND dep.fecha_detalle_estado_pedido IN( " +
            "SELECT MAX(dep.fecha_detalle_estado_pedido) FROM DetalleEstadoPedido dep " +
            "GROUP BY dep.pedido.id_pedido) " +
            "ORDER BY dep.fecha_detalle_estado_pedido DESC")
    List<Pedido> getPedidosByEstado(String nombreEstado);

    @Query("FROM Pedido p " +
            "INNER JOIN p.detalleestadospedido dep " +
            "INNER JOIN dep.estadoPedido ep " +
            "WHERE ep.nombre_estado_pedido = ?1 AND YEAR(dep.fecha_detalle_estado_pedido) = ?2 AND " +
            "dep.fecha_detalle_estado_pedido IN( " +
            "SELECT MAX(dep.fecha_detalle_estado_pedido) FROM DetalleEstadoPedido dep " +
            "GROUP BY dep.pedido.id_pedido) " +
            "ORDER BY dep.fecha_detalle_estado_pedido DESC")
    List<Pedido> getPedidosByEstadoAndYear(String nombreEstado, int year);

    @Query("FROM Pedido p " +
            "INNER JOIN p.detalleestadospedido dep " +
            "INNER JOIN dep.estadoPedido ep " +
            "WHERE p.usuario.id_usuario = ?1 AND dep.fecha_detalle_estado_pedido IN( " +
            "SELECT MAX(dep.fecha_detalle_estado_pedido) FROM DetalleEstadoPedido dep " +
            "GROUP BY dep.pedido.id_pedido) " +
            "ORDER BY dep.fecha_detalle_estado_pedido DESC")
    List<Pedido> getAllPedidosByIdUsuario(Long idUsuario);

    @Query("SELECT p FROM Pedido p " +
            "INNER JOIN p.detalleestadospedido dep " +
            "INNER JOIN dep.estadoPedido ep " +
            "WHERE p.usuario.id_usuario = ?1 AND ep.nombre_estado_pedido = ?2 AND dep.fecha_detalle_estado_pedido IN( " +
            "SELECT MAX(dep.fecha_detalle_estado_pedido) FROM DetalleEstadoPedido dep " +
            "GROUP BY dep.pedido.id_pedido) " +
            "ORDER BY dep.fecha_detalle_estado_pedido DESC")
    List<Pedido> getPedidosByIdUsuarioAndEstado(Long idUsuario, String nombreEstado);

    @Query("FROM Pedido WHERE id_pedido = ?1 AND usuario.id_usuario = ?2")
    Optional<Pedido> getPedidosByIdPedidoAndIdUsuario(Long idPedido, Long idUsuario);

    @Query("SELECT COUNT(*) FROM Pedido p " +
            "INNER JOIN p.detalleestadospedido dep " +
            "INNER JOIN dep.estadoPedido ep " +
            "WHERE ep.nombre_estado_pedido = ?1 AND dep.fecha_detalle_estado_pedido IN( " +
            "SELECT MAX(dep.fecha_detalle_estado_pedido) FROM DetalleEstadoPedido dep " +
            "GROUP BY dep.pedido.id_pedido)")
    Integer getCantEstados(String nombreEstado);
}
