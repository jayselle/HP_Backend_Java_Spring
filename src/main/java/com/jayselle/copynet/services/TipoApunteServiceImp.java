package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.TipoApunte;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.repositories.TipoApunteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoApunteServiceImp implements TipoApunteService {

    @Autowired
    TipoApunteRepository tipoApunteRepository;

    @Override
    public List<TipoApunte> findAll() {
        return tipoApunteRepository.findAll();
    }

    @Override
    public TipoApunte getTipoApunteByNombre(String tipoapunte) {
        Optional<TipoApunte> tipoApunteOptional = tipoApunteRepository.getTipoApunteByName(tipoapunte);
        if (tipoApunteOptional.isPresent()){
            return tipoApunteOptional.get();
        } else {
            throw new MyHttpException(HttpStatus.NOT_FOUND, "El tipo de apunte no existe");
        }
    }
}
