package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.PrecioActual;

import java.time.LocalDateTime;
import java.util.List;

public interface PrecioActualService {

    PrecioActual getPrecioByDate(LocalDateTime localDateTime);
    void actualizarPrecio(PrecioActual precioActual);
    List<PrecioActual> getPrecios();
}
