package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.Apunte;

import java.util.List;

public interface ApunteService {

    List<Apunte> findAll();
    void registrarApunte(Apunte apunte);
    Apunte findApunteById(Long id);
}
