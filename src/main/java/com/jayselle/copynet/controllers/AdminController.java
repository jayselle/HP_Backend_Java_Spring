package com.jayselle.copynet.controllers;

import com.jayselle.copynet.dtos.AdminDashDto;
import com.jayselle.copynet.dtos.CatedraDto;
import com.jayselle.copynet.dtos.PrecioActualDto;
import com.jayselle.copynet.entities.Catedra;
import com.jayselle.copynet.entities.Departamento;
import com.jayselle.copynet.entities.Nivel;
import com.jayselle.copynet.error.GenericResponse;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.services.*;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@CrossOrigin(value = "http://localhost:8081")
@RestController
public class AdminController {

    @Autowired
    CatedraService catedraService;

    @Autowired
    PrecioActualService precioActualService;

    @Autowired
    DepartamentoService departamentoService;

    @Autowired
    AdminDashService adminDashService;

    @Autowired
    DtoService dtoService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/api/admins/catedra")
    public ResponseEntity registrarCatedra(@RequestBody CatedraDto catedraDto){

        Catedra catedra = modelMapper.map(catedraDto, Catedra.class);

        try {
            if (catedra.getNombre_catedra().isEmpty()){
                throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Catedra: Completar nombre");
            } else if (!Pattern.matches("^([a-zA-ZÁ-ÿ0-9 ]+)",catedra.getNombre_catedra())){
                throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Catedra: Solo texto y/o letras");
            } else if (catedra.getNombre_catedra().length()<3 || catedra.getNombre_catedra().length()>25){
                throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Catedra: Entre 3 y 25 caracteres");
            }

            Departamento departamento = departamentoService.getDepartamentoByName(catedraDto.getNombre_departamento());
            catedra.setDepartamento(departamento);
            for(Nivel nivel : departamento.getNiveles()){
                if (StringUtils.equals(nivel.getNombre_nivel(),catedraDto.getNombre_nivel())){
                    catedra.setNivel(nivel);
                }
            }
            if (StringUtils.isEmpty(catedra.getNivel().getNombre_nivel())){
                throw new MyHttpException(HttpStatus.NOT_FOUND,"Nivel no encontrado");
            } else {
                catedraService.registrarCatedra(catedra);
                GenericResponse gr = new GenericResponse(HttpStatus.OK,"Catedra registrada");
                return new ResponseEntity<>(gr, HttpStatus.OK);
            }
        } catch (MyHttpException ex){
            GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
            return new ResponseEntity<>(gr, ex.getStatus());
        } catch (NullPointerException ex){
            GenericResponse gr = new GenericResponse(HttpStatus.UNPROCESSABLE_ENTITY,"Completar datos");
            return new ResponseEntity<>(gr, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/api/admins/precioactual")
    public ResponseEntity actualizarPrecioActual(@RequestBody PrecioActualDto precioActualDto){
        try {
            precioActualService.actualizarPrecio(dtoService.convertPrecioDtoToEntity(precioActualDto));
            GenericResponse gr = new GenericResponse(HttpStatus.OK,"Nuevo precio registrado");
            return new ResponseEntity<>(gr, HttpStatus.OK);
        } catch(MyHttpException ex){
            GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
            return new ResponseEntity<>(gr, ex.getStatus());
        }
    }

    @GetMapping("api/admins/dashboard")
    public ResponseEntity getAdminDashboard(){
        try{
            AdminDashDto adminDashDto = adminDashService.getAdminDashboard();
            GenericResponse gr = new GenericResponse(HttpStatus.OK,"Admin Dashboard",adminDashDto);
            return new ResponseEntity<>(gr, HttpStatus.OK);
        } catch(MyHttpException ex){
            GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
            return new ResponseEntity<>(gr, ex.getStatus());
        }
    }

}