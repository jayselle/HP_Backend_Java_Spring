package com.jayselle.copynet.services;

import com.jayselle.copynet.dtos.AlumnoDto;
import com.jayselle.copynet.dtos.PedidoDto;
import com.jayselle.copynet.dtos.PrecioActualDto;
import com.jayselle.copynet.entities.Alumno;
import com.jayselle.copynet.entities.Pedido;
import com.jayselle.copynet.entities.PrecioActual;

import java.util.List;

public interface DtoService {

    List<PedidoDto> convertPedidosToDto(List<Pedido> pedidos);
    PedidoDto convertPedidoToDto(Pedido pedido);
    PrecioActualDto convertPrecioToDto(PrecioActual precioActual);
    PrecioActual convertPrecioDtoToEntity(PrecioActualDto precioActualDto);
    AlumnoDto convertAlumnoToDto(Alumno alumno);

}
