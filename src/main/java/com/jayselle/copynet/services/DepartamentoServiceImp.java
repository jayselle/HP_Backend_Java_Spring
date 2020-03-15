package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.Departamento;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.repositories.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartamentoServiceImp implements DepartamentoService {

    @Autowired
    DepartamentoRepository departamentoRepository;

    @Override
    public Departamento getDepartamentoByName(String nombreDepartamento) {
        Optional<Departamento> optionalDepartamento = departamentoRepository.getDepartamentoByName(nombreDepartamento);
        if (optionalDepartamento.isPresent()){
            return optionalDepartamento.get();
        } else {
            throw new MyHttpException(HttpStatus.NOT_FOUND,"Departamento no encontrado");
        }
    }
}
