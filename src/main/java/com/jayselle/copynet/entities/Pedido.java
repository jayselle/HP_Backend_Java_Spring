package com.jayselle.copynet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_pedido")
    private Long id_pedido;

    @Column(name="comentario_pedido")
    private String comentario_pedido;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="id_pedido")
    private List<DetalleEstadoPedido> detalleestadospedido;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="id_pedido")
    private List<LineaPedido> lineaspedido;

    public DetalleEstadoPedido getDetalleEstadoActual(){
        Collections.sort(detalleestadospedido, Collections.reverseOrder());
        return detalleestadospedido.get(0);
    }

    public Optional<DetalleEstadoPedido> getDetalleByNombreEstado(String nombreEstado){
        Optional<DetalleEstadoPedido> optional;
        for (DetalleEstadoPedido detalle : detalleestadospedido){
            if (StringUtils.equals(detalle.getEstadoPedido().getNombre_estado_pedido(),nombreEstado)){
                optional = Optional.of(detalle);
                return optional;
            }
        }
        optional = Optional.empty();
        return optional;
    }
}
