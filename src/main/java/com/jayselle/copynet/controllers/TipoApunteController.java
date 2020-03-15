package com.jayselle.copynet.controllers;

import com.jayselle.copynet.entities.TipoApunte;
import com.jayselle.copynet.services.TipoApunteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(value = "http://localhost:8081")
@RestController
public class TipoApunteController {

    @Autowired
    TipoApunteService tipoApunteService;

    @GetMapping("/api/tiposapunte")
    public List<TipoApunte> getTiposApunte(){
        return tipoApunteService.findAll();
    }

}
