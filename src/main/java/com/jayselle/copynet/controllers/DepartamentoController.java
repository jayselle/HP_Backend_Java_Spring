package com.jayselle.copynet.controllers;

import com.jayselle.copynet.dtos.DepartamentoDto;
import com.jayselle.copynet.entities.Departamento;
import com.jayselle.copynet.error.GenericResponse;
import com.jayselle.copynet.repositories.DepartamentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(value = "http://localhost:8081")
@RestController
public class DepartamentoController {

    @Autowired
    DepartamentoRepository departamentoRepository;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/api/departamentos")
    public ResponseEntity getDepartamentos(){

        List<Departamento> departamentos = departamentoRepository.findAll();

        List<DepartamentoDto> departamentoDtos = new ArrayList<>();

        for (Departamento departamento : departamentos){

            DepartamentoDto departamentoDto = modelMapper.map(departamento,DepartamentoDto.class);
            departamentoDtos.add(departamentoDto);

        }

        GenericResponse gr = new GenericResponse(HttpStatus.OK,"Departamentos y niveles",departamentoDtos);
        return new ResponseEntity<>(gr, HttpStatus.OK);

    }

}
