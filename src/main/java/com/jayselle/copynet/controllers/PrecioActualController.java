package com.jayselle.copynet.controllers;

import com.jayselle.copynet.dtos.PrecioActualDto;
import com.jayselle.copynet.entities.PrecioActual;
import com.jayselle.copynet.error.GenericResponse;
import com.jayselle.copynet.services.DtoService;
import com.jayselle.copynet.services.PrecioActualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(value = "http://localhost:8081")
@RestController
public class PrecioActualController {

    @Autowired
    PrecioActualService precioActualService;

    @Autowired
    DtoService dtoService;

    @GetMapping("/api/precioactual")
    public PrecioActual findPrecioActual(){
        return precioActualService.getPrecioByDate(LocalDateTime.now());
    }

    @GetMapping("/api/precios")
    public ResponseEntity getPrecios(){
        List<PrecioActual> precios = precioActualService.getPrecios();
        List<PrecioActualDto> precioActualDtos = new ArrayList<>();
        for (PrecioActual precioActual : precios){
            precioActualDtos.add(dtoService.convertPrecioToDto(precioActual));
        }
        precioActualDtos.get(0).setFechahasta_precioactual("-");
        GenericResponse gr = new GenericResponse(HttpStatus.OK,"Lista de precios",precioActualDtos);
        return new ResponseEntity<>(gr, HttpStatus.OK);
    }
}
