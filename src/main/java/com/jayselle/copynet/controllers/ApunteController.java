package com.jayselle.copynet.controllers;

import com.jayselle.copynet.dtos.ApunteDto;
import com.jayselle.copynet.entities.Apunte;
import com.jayselle.copynet.services.ApunteService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(value = "http://localhost:8081")
@RestController
public class ApunteController {

    @Autowired
    ApunteService apunteService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/api/apuntes")
    public List<ApunteDto> getAllApuntes(){

        TypeMap<Apunte,ApunteDto> typeMap = modelMapper.getTypeMap(Apunte.class,ApunteDto.class);
        if (typeMap == null){
            modelMapper.addMappings(new PropertyMap<Apunte,ApunteDto>() {
                protected void configure(){
                    map().setNombre_catedra(source.getCatedra().getNombre_catedra());
                    map().setNombre_departamento(source.getCatedra().getDepartamento().getNombre_departamento());
                    map().setNombre_nivel(source.getCatedra().getNivel().getNombre_nivel());
                    map().setNombre_tipo_apunte(source.getTipoApunte().getNombre_tipo_apunte());
                }
            });
        }

        List<Apunte> apuntes = apunteService.findAll();
        List<ApunteDto> apuntesDtos = new ArrayList<>();

        for (Apunte a : apuntes){
            ApunteDto apunteDto = modelMapper.map(a,ApunteDto.class);
            apuntesDtos.add(apunteDto);
        }

        return apuntesDtos;
    }

}
