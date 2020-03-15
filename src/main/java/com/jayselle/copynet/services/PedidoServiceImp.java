package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.*;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.repositories.EstadoPedidoRepository;
import com.jayselle.copynet.repositories.PedidoRepository;
import com.jayselle.copynet.repositories.PrecioActualRepository;
import com.jayselle.copynet.repositories.UsuarioRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PedidoServiceImp implements PedidoService{

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    EstadoPedidoRepository estadoPedidoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public List<Pedido> getPedidosByEstado(String nombreEstado) {
        return pedidoRepository.getPedidosByEstado(nombreEstado);
    }

    @Override
    public void cancelarPedido(Long idPedido, Long idUsuario) {
        Optional<Pedido> optionalPedido = pedidoRepository.getPedidosByIdPedidoAndIdUsuario(idPedido,idUsuario);
        if (!optionalPedido.isPresent()){
            throw new MyHttpException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
        } else {
            Pedido pedido = optionalPedido.get();

            List<DetalleEstadoPedido> detalleEstadosPedido = pedido.getDetalleestadospedido();

            Collections.sort(detalleEstadosPedido, Collections.reverseOrder());

            if (!StringUtils.equals(detalleEstadosPedido.get(0).getEstadoPedido().getNombre_estado_pedido(),"Pendiente")){
                throw new MyHttpException(HttpStatus.CONFLICT, "El pedido no se encuentra pendiente");
            } else {
                DetalleEstadoPedido detalle = new DetalleEstadoPedido();
                detalle.setFecha_detalle_estado_pedido(LocalDateTime.now());
                detalle.setEstadoPedido(estadoPedidoRepository.getEstadoPedido("Cancelado"));
                detalleEstadosPedido.add(detalle);
                pedido.setDetalleestadospedido(detalleEstadosPedido);
                pedidoRepository.save(pedido);
            }
        }
    }

    @Override
    public void agregarEstadoPedido(Long idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.getOne(idPedido);
        EstadoPedido estadoPedido = estadoPedidoRepository.getEstadoPedido(nuevoEstado);
        List<DetalleEstadoPedido> detalleEstadosPedido = pedido.getDetalleestadospedido();
        DetalleEstadoPedido detalle = new DetalleEstadoPedido();
        detalle.setFecha_detalle_estado_pedido(LocalDateTime.now());
        detalle.setEstadoPedido(estadoPedido);
        detalleEstadosPedido.add(detalle);
        pedido.setDetalleestadospedido(detalleEstadosPedido);
        pedidoRepository.save(pedido);
    }

    @Override
    public List<Pedido> getPedidosImpresosByLegajo(Long legajo) {
        Optional<Alumno> optionalAlumno = usuarioRepository.findByAlumnoLegajo(legajo);
        if (!optionalAlumno.isPresent()){
            throw new MyHttpException(HttpStatus.NOT_FOUND, "Alumno no encontrado");
        } else {

            Alumno alumno = optionalAlumno.get();

            if (!alumno.getHabilitado()){
                throw new MyHttpException(HttpStatus.UNAUTHORIZED, "Alumno inhabilitado");
            } else {

                List<Pedido> pedidosImpresos = new ArrayList<>();

                for (Pedido pedido : alumno.getPedidos()){
                    if (StringUtils.equals("Impreso",pedido.getDetalleEstadoActual().getEstadoPedido().getNombre_estado_pedido())){
                        pedidosImpresos.add(pedido);
                    }
                }

                return pedidosImpresos;

            }
        }
    }

    @Transactional
    @Override
    public void registrarPedido(Pedido pedido) {

        List<LineaPedido> lineas = pedido.getLineaspedido();

        for (LineaPedido linea : lineas){

            if (StringUtils.isEmpty(linea.getEncuadernacion_linea_pedido()) || StringUtils.isEmpty(linea.getPlegado_linea_pedido()) || StringUtils.isEmpty(linea.getColor_linea_pedido())){
                throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Controlar especificaciones de la linea del apunte "+linea.getApunte().getTitulo_apunte());
            } else if (!StringUtils.equalsAnyIgnoreCase(linea.getEncuadernacion_linea_pedido(),"sin","abrochado","anillado")){
                throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Controlar la encuadernacion de la linea del apunte "+linea.getApunte().getTitulo_apunte());
            } else if (!StringUtils.equalsAnyIgnoreCase(linea.getPlegado_linea_pedido(),"simple","doble")){
                throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Controlar el plegado de la linea del apunte "+linea.getApunte().getTitulo_apunte());
            } else if (!StringUtils.equalsAnyIgnoreCase(linea.getColor_linea_pedido(),"n/b","color")){
                throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Controlar el color de la linea del apunte "+linea.getApunte().getTitulo_apunte());
            } else if (linea.getCopias_linea_pedido()<=0){
                throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Controlar las copias de la linea del apunte "+linea.getApunte().getTitulo_apunte());
            } else {

                Double cantHojasLinea;
                if (StringUtils.equalsIgnoreCase(linea.getPlegado_linea_pedido(),"doble")){
                    cantHojasLinea = Math.ceil(((linea.getApunte().getCantidad_paginas_apunte()*linea.getCopias_linea_pedido()) / 2));
                } else {
                    cantHojasLinea = (double)(linea.getApunte().getCantidad_paginas_apunte()*linea.getCopias_linea_pedido());
                }

                Integer cantMinHojasParaAbrochar = 2;
                Integer cantMaxHojasParaAbrochar = 100;
                Integer cantMinHojasParaAnillar = 20;

                if (StringUtils.equalsIgnoreCase(linea.getEncuadernacion_linea_pedido(),"abrochado")){
                    if (cantHojasLinea<cantMinHojasParaAbrochar){
                        throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "El apunte "+linea.getApunte().getTitulo_apunte()+" no se puede abrochar. Minimo deben ser "+cantMinHojasParaAbrochar);
                    } else if (cantHojasLinea>cantMaxHojasParaAbrochar) {
                        throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "El apunte "+linea.getApunte().getTitulo_apunte()+" no se puede abrochar. Como máximo deben ser "+cantMaxHojasParaAbrochar);
                    }
                } else if (StringUtils.equalsIgnoreCase(linea.getEncuadernacion_linea_pedido(),"anillado")){
                    if (cantHojasLinea<cantMinHojasParaAnillar){
                        throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "El apunte "+linea.getApunte().getTitulo_apunte()+" no se puede anillar. Minimo deben ser "+cantMinHojasParaAnillar);
                    }
                }
            }

        }

        List<DetalleEstadoPedido> detalleEstadosPedido = new ArrayList<>();
        DetalleEstadoPedido detalle = new DetalleEstadoPedido();
        detalle.setFecha_detalle_estado_pedido(LocalDateTime.now());
        detalle.setEstadoPedido(estadoPedidoRepository.getEstadoPedido("Pendiente"));
        detalleEstadosPedido.add(detalle);
        pedido.setDetalleestadospedido(detalleEstadosPedido);
        String comentario = pedido.getComentario_pedido();
        pedido.setComentario_pedido(StringUtils.abbreviate(comentario,250));

        pedidoRepository.save(pedido);

    }

    @Transactional
    @Override
    public void registrarVencimientos() {

        List<Pedido> pedidosImpresos = pedidoRepository.getPedidosByEstado("Impreso");
        List<Pedido> pedidosVencidos = new ArrayList<>();

        if (!pedidosImpresos.isEmpty()){

            LocalDateTime today = LocalDateTime.now();

            for(Pedido pedidoImpreso : pedidosImpresos){

                int cantDias = 0;

                DetalleEstadoPedido detalleEstadoActual = pedidoImpreso.getDetalleEstadoActual();

                LocalDateTime fechaDeImpresion = detalleEstadoActual.getFecha_detalle_estado_pedido();

                while(today.isAfter(fechaDeImpresion)){

                    if (DayOfWeek.SATURDAY != fechaDeImpresion.getDayOfWeek() && DayOfWeek.SUNDAY != fechaDeImpresion.getDayOfWeek()) {
                        cantDias++;
                    }

                    fechaDeImpresion = fechaDeImpresion.plusDays(1);

                }

                if (cantDias>=10) pedidosVencidos.add(pedidoImpreso);

            }

            if (!pedidosVencidos.isEmpty()){

                EstadoPedido estadoVencido = estadoPedidoRepository.getEstadoPedido("Vencido");

                for(Pedido pedidoVencido : pedidosVencidos){
                    List<DetalleEstadoPedido> detalleEstadosPedido = pedidoVencido.getDetalleestadospedido();
                    DetalleEstadoPedido detalle = new DetalleEstadoPedido();
                    detalle.setFecha_detalle_estado_pedido(LocalDateTime.now());
                    detalle.setEstadoPedido(estadoVencido);
                    detalleEstadosPedido.add(detalle);
                    pedidoVencido.setDetalleestadospedido(detalleEstadosPedido);
                }

            }

        }

    }

    @Override
    public List<Pedido> getPedidosByEstadoAndLegajo(String estado, Long legajo) {

        Optional<Alumno> alumnoOptional = usuarioRepository.findByAlumnoLegajo(legajo);
        if (!alumnoOptional.isPresent()){
            throw new MyHttpException(HttpStatus.NOT_FOUND, "Alumno no encontrado");
        } else {
            Alumno alumno = alumnoOptional.get();
            List<Pedido> pedidos;
            if (StringUtils.equals(estado,"todos")){
                pedidos = pedidoRepository.getAllPedidosByIdUsuario(alumno.getId_usuario());
            } else {
                pedidos = pedidoRepository.getPedidosByIdUsuarioAndEstado(alumno.getId_usuario(),estado);
            }
            return pedidos;
        }
    }

}
