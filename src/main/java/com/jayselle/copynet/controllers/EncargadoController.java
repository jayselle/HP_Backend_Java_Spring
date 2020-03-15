package com.jayselle.copynet.controllers;

import com.jayselle.copynet.dtos.*;
import com.jayselle.copynet.entities.*;
import com.jayselle.copynet.error.GenericResponse;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.services.*;
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
public class EncargadoController {

    @Autowired
    ApunteService apunteService;

    @Autowired
    CatedraService catedraService;

    @Autowired
    TipoApunteService tipoApunteService;

    @Autowired
    PedidoService pedidoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    DtoService dtoService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/api/encargados/apuntes")
    public ResponseEntity<GenericResponse> postNewApunte(@RequestBody PostApunteDto postApunteDto){

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PostApunteDto>> violations = validator.validate(postApunteDto);
        if (!violations.isEmpty()) {
            List<String> mensajes = new ArrayList<>();
            violations.forEach(violation -> {
                mensajes.add(violation.getMessage());
            });
            GenericResponse gr = new GenericResponse(HttpStatus.UNPROCESSABLE_ENTITY,"Campos inválidos",mensajes);
            return new ResponseEntity<>(gr, HttpStatus.UNPROCESSABLE_ENTITY);
        } else {

            Apunte apunte = modelMapper.map(postApunteDto, Apunte.class);

            try {
                apunte.setCatedra(catedraService.getCatedraByDepartamentoAndNivel(postApunteDto.getNombre_catedra(),postApunteDto.getNombre_departamento(),postApunteDto.getNombre_nivel()));
                apunte.setTipoApunte(tipoApunteService.getTipoApunteByNombre(postApunteDto.getTipoapunte()));
                apunte.setEstado_apunte(true);
                apunteService.registrarApunte(apunte);
                GenericResponse gr = new GenericResponse(HttpStatus.OK,"¡Apunte registrado!");
                return new ResponseEntity<>(gr,HttpStatus.OK);
            } catch (MyHttpException ex){
                GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
                return new ResponseEntity<>(gr, ex.getStatus());
            }

        }
    }

    @GetMapping("/api/encargados/pedidos")
    public ResponseEntity getPedidosByEstado(@RequestParam String estado){

        List<Pedido> pedidos = pedidoService.getPedidosByEstado(estado);

        List<PedidoDto> pedidoDtos = new ArrayList<>();

        for(Pedido pedido : pedidos) {

            pedidoDtos.add(dtoService.convertPedidoToDto(pedido));

        }

        GenericResponse gr = new GenericResponse(HttpStatus.OK,"Pedidos con estado: "+estado,pedidoDtos);
        return new ResponseEntity<>(gr, HttpStatus.OK);

    }

    @GetMapping("/api/encargados/user/pedidos")
    public ResponseEntity getPedidosByEstadoAndLegajo(@RequestParam String estado, @RequestParam String legajo){

        try {

            List<Pedido> pedidos = pedidoService.getPedidosByEstadoAndLegajo(estado, Long.parseLong(legajo));

            List<PedidoDto> pedidoDtos = new ArrayList<>();

            for (Pedido pedido : pedidos) {

                pedidoDtos.add(dtoService.convertPedidoToDto(pedido));

            }

            GenericResponse gr = new GenericResponse(HttpStatus.OK, "Pedidos de " + legajo + " con estado " + estado, pedidoDtos);
            return new ResponseEntity<>(gr, HttpStatus.OK);

        } catch (MyHttpException ex){
            GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
            return new ResponseEntity<>(gr, ex.getStatus());
        }
    }

    @PutMapping("/api/encargados/pedidos/nuevoEstado")
    public void agregarEstadoPedido(@RequestParam Long idPedido, @RequestParam String nuevoEstado){
        pedidoService.agregarEstadoPedido(idPedido,nuevoEstado);
    }

    @GetMapping("/api/encargados/user/pedidos/cobrados")
    public ResponseEntity getPedidosCobrados(@RequestParam Long legajo){
        try {

            List<Pedido> pedidosImpresos = pedidoService.getPedidosImpresosByLegajo(legajo);

            List<PedidoDto> pedidoDtos = new ArrayList<>();

            for(Pedido pedido : pedidosImpresos) {

                pedidoDtos.add(dtoService.convertPedidoToDto(pedido));

            }

            GenericResponse gr = new GenericResponse(HttpStatus.OK,"Pedidos impresos",pedidoDtos);
            return new ResponseEntity<>(gr, HttpStatus.OK);

        } catch (MyHttpException ex){
            GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
            return new ResponseEntity<>(gr, ex.getStatus());
        }
    }

    @GetMapping("/api/encargados/user")
    public ResponseEntity getAlumno(@RequestParam Long legajo){

        try{
            AlumnoDto alumnoDto = dtoService.convertAlumnoToDto(usuarioService.getAlumno(legajo));
            GenericResponse gr = new GenericResponse(HttpStatus.OK,"Alumno encontrado",alumnoDto);
            return new ResponseEntity<>(gr, HttpStatus.OK);
        } catch (MyHttpException ex){
            GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
            return new ResponseEntity<>(gr, ex.getStatus());
        }
    }

    @PutMapping("/api/encargados/user/habilitarAlumno")
    public ResponseEntity habilitarAlumno(@RequestParam Long legajo){
        try{
            usuarioService.habilitarAlumno(legajo);
            GenericResponse gr = new GenericResponse(HttpStatus.OK,"Alumno habilitado");
            return new ResponseEntity<>(gr, HttpStatus.OK);
        } catch (MyHttpException ex){
            GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
            return new ResponseEntity<>(gr, ex.getStatus());
        }

    }

}
