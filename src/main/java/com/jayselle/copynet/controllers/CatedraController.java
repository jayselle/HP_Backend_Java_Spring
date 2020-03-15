package com.jayselle.copynet.controllers;

import com.jayselle.copynet.dtos.CatedraDto;
import com.jayselle.copynet.entities.Catedra;
import com.jayselle.copynet.error.GenericResponse;
import com.jayselle.copynet.repositories.CatedraRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
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
public class CatedraController {

    @Autowired
    CatedraRepository catedraRepository;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/api/catedras")
    public ResponseEntity getCatedras(){

        List<Catedra> catedras = catedraRepository.findAll();
        List<CatedraDto> catedraDtos = new ArrayList<>();

        TypeMap<Catedra,CatedraDto> typeMap = modelMapper.getTypeMap(Catedra.class,CatedraDto.class);
        if (typeMap == null){
            modelMapper.addMappings(new PropertyMap<Catedra,CatedraDto>() {
                protected void configure(){
                    map().setNombre_departamento(source.getDepartamento().getNombre_departamento());
                    map().setNombre_nivel(source.getNivel().getNombre_nivel());
                }
            });
        }

        for(Catedra catedra: catedras) {
            CatedraDto catedraDto = modelMapper.map(catedra,CatedraDto.class);
            catedraDtos.add(catedraDto);
        }

        GenericResponse gr = new GenericResponse(HttpStatus.OK,"Catedras",catedraDtos);
        return new ResponseEntity<>(gr, HttpStatus.OK);

    }

}
