package com.jayselle.copynet.controllers;

import com.jayselle.copynet.dtos.PostUsuarioDto;
import com.jayselle.copynet.entities.Alumno;
import com.jayselle.copynet.error.GenericResponse;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.services.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(value = "http://localhost:8081")
@RestController
public class DefaultController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> login(@RequestParam String username, @RequestParam String password){

        try {
            String token = usuarioService.login(username,password);
            GenericResponse gr = new GenericResponse(HttpStatus.OK, token);
            return new ResponseEntity<>(gr,HttpStatus.OK);
        } catch (MyHttpException ex){
            GenericResponse gr = new GenericResponse(ex.getStatus(), ex.getMensaje());
            return new ResponseEntity<>(gr,ex.getStatus());
        }

    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse> register(@RequestBody PostUsuarioDto postUsuarioDto){

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PostUsuarioDto>> violations = validator.validate(postUsuarioDto);
        if (!violations.isEmpty()) {
            List<String> mensajes = new ArrayList<>();
            violations.forEach(violation -> {
                mensajes.add(violation.getMessage());
            });
            GenericResponse gr = new GenericResponse(HttpStatus.UNPROCESSABLE_ENTITY,"Campos inválidos",mensajes);
            return new ResponseEntity<>(gr, HttpStatus.UNPROCESSABLE_ENTITY);
        } else {

            Alumno alumno = modelMapper.map(postUsuarioDto, Alumno.class);

            try {
                String token = usuarioService.registrarUsuario(alumno);
                GenericResponse gr = new GenericResponse(HttpStatus.OK,token);
                return new ResponseEntity<>(gr, HttpStatus.OK);
            } catch (MyHttpException ex){
                GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
                return new ResponseEntity<>(gr, ex.getStatus());
            }
        }

    }

}
