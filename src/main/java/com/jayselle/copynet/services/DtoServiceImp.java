package com.jayselle.copynet.services;

import com.jayselle.copynet.dtos.*;
import com.jayselle.copynet.entities.*;
import com.jayselle.copynet.repositories.PrecioActualRepository;
import org.apache.commons.lang3.StringUtils;
import org.decimal4j.util.DoubleRounder;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DtoServiceImp implements DtoService{

    @Autowired
    PrecioActualRepository precioActualRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<PedidoDto> convertPedidosToDto(List<Pedido> pedidos){

        List<PedidoDto> pedidoDtos = new ArrayList<>();

        for (Pedido pedido : pedidos){
            pedidoDtos.add(convertPedidoToDto(pedido));
        }

        return pedidoDtos;

    }

    @Override
    public PedidoDto convertPedidoToDto(Pedido pedido) {

        PedidoDto pedidoDto = modelMapper.map(pedido,PedidoDto.class);

        pedidoDto.setPrecio_pedido(0f);

        List<PostLineaDto> lineaDtoList = new ArrayList<>();

        TypeMap<Apunte, ApunteDto> typeMap = modelMapper.getTypeMap(Apunte.class,ApunteDto.class);
        if (typeMap == null){
            modelMapper.addMappings(new PropertyMap<Apunte,ApunteDto>() {
                protected void configure(){
                    map().setNombre_catedra(source.getCatedra().getNombre_catedra());
                    map().setNombre_departamento(source.getCatedra().getDepartamento().getNombre_departamento());
                    map().setNombre_nivel(source.getCatedra().getNivel().getNombre_nivel());
                    map().setNombre_tipo_apunte(source.getTipoApunte().getNombre_tipo_apunte());
                }
            });
        }

        Optional<DetalleEstadoPedido> optional = pedido.getDetalleByNombreEstado("Pendiente");

        PrecioActual precioActual = precioActualRepository.getPrecioByDate(optional.get().getFecha_detalle_estado_pedido());

        for(LineaPedido lineaPedido : pedido.getLineaspedido()) {

            PostLineaDto postLineaDto = modelMapper.map(lineaPedido,PostLineaDto.class);
            postLineaDto.setPrecio_linea_pedido(calcularPrecioLineaPedido(precioActual,lineaPedido));
            lineaDtoList.add(postLineaDto);
            pedidoDto.setPrecio_pedido((float) DoubleRounder.round(pedidoDto.getPrecio_pedido()+postLineaDto.getPrecio_linea_pedido(),2));

        }

        pedidoDto.setLineas(lineaDtoList);

        DetalleEstadoPedido detalleActual = pedido.getDetalleEstadoActual();

        pedidoDto.setEstado_pedido(detalleActual.getEstadoPedido().getNombre_estado_pedido());

        LocalDateTime fecha = detalleActual.getFecha_detalle_estado_pedido();

        pedidoDto.setFecha_pedido(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        return pedidoDto;

    }

    @Override
    public PrecioActualDto convertPrecioToDto(PrecioActual precioActual) {

        PrecioActualDto precioActualDto = modelMapper.map(precioActual,PrecioActualDto.class);
        precioActualDto.setFechadesde_precioactual(precioActual.getFechadesde_precioactual().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        precioActualDto.setFechahasta_precioactual(precioActual.getFechahasta_precioactual().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        return precioActualDto;

    }

    @Override
    public PrecioActual convertPrecioDtoToEntity(PrecioActualDto precioActualDto) {

        return modelMapper.map(precioActualDto,PrecioActual.class);

    }

    private Float calcularPrecioLineaPedido(PrecioActual precioActual, LineaPedido lineaPedido) {

        Float preciolinea;

        if (StringUtils.equalsIgnoreCase(lineaPedido.getColor_linea_pedido(),"n/b")){
            preciolinea = (lineaPedido.getApunte().getCantidad_paginas_apunte() * lineaPedido.getCopias_linea_pedido()) * precioActual.getPreciocopia_precioactual();
        } else {
            preciolinea = (lineaPedido.getApunte().getCantidad_paginas_apunte() * lineaPedido.getCopias_linea_pedido()) * precioActual.getPreciocolor_precioactual();
        }

        if (StringUtils.equalsIgnoreCase(lineaPedido.getEncuadernacion_linea_pedido(),"anillado")){
            preciolinea = preciolinea + precioActual.getPrecioanillado_precioactual();
        }

        return (float) DoubleRounder.round(preciolinea,2);
    }


    @Override
    public AlumnoDto convertAlumnoToDto(Alumno alumno) {

        AlumnoDto alumnoDto = modelMapper.map(alumno,AlumnoDto.class);

        alumnoDto =  modelMapper.map(alumno,AlumnoDto.class);

        alumnoDto.setEstado(getEstado(alumno.getHabilitado()));
        alumnoDto.setNombreCompleto(alumno.getNombre_usuario()+" "+alumno.getApellido_usuario());

        LocalDateTime fecha = alumno.getFecha_habilitacion();

        alumnoDto.setFechaHabilitacion(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        return alumnoDto;
    }

    private String getEstado(Boolean flag){
        if (flag) {
            return "Habilitado";
        } else {
            return "Inhabilitado";
        }
    }
}
