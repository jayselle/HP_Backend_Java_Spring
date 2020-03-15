package com.jayselle.copynet.repositories;

import com.jayselle.copynet.entities.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento,Long> {

    @Query("FROM Departamento WHERE nombre_departamento = ?1")
    Optional<Departamento> getDepartamentoByName(String name);

    @Query("SELECT d.nombre_departamento FROM Departamento d " +
            "INNER JOIN Catedra c ON c.departamento.id_departamento = d.id_departamento " +
            "INNER JOIN Apunte a ON a.catedra.id_catedra = c.id_catedra " +
            "INNER JOIN LineaPedido lp ON lp.apunte.id_apunte = a.id_apunte " +
            "INNER JOIN Pedido p ON p.id_pedido = lp.pedido.id_pedido ")
    List<String> getDepartamentosEnPedidosCobrados();

}
