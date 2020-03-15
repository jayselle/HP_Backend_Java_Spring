package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.TipoApunte;

import java.util.List;

public interface TipoApunteService {

    List<TipoApunte> findAll();
    TipoApunte getTipoApunteByNombre(String tipoapunte);

}
