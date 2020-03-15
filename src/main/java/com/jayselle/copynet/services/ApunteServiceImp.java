package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.Apunte;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.repositories.ApunteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApunteServiceImp implements ApunteService {

    @Autowired
    ApunteRepository apunteRepository;

    @Override
    public List<Apunte> findAll() {
        return apunteRepository.findAll();
    }

    @Override
    public Apunte findApunteById(Long id) {
        Optional<Apunte> apunteOptional = apunteRepository.findById(id);
        if (apunteOptional.isPresent()){
            return apunteOptional.get();
        } else {
            throw new MyHttpException(HttpStatus.NOT_FOUND, "Apunte no encontrado");
        }
    }

    @Override
    public void registrarApunte(Apunte apunte) {

        Optional<Apunte> optionalApunte = apunteRepository.getApunteByTitulo(apunte.getTitulo_apunte());
        if (optionalApunte.isPresent()){
            throw new MyHttpException(HttpStatus.CONFLICT, "Titulo ya registrado. Probá con otro.");
        } else {
            apunteRepository.save(apunte);
        }

    }

}
