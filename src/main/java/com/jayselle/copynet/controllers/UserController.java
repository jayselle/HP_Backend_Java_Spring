package com.jayselle.copynet.controllers;

import com.jayselle.copynet.dtos.PedidoDto;
import com.jayselle.copynet.dtos.PostLineaDto;
import com.jayselle.copynet.dtos.PostPedidoDto;
import com.jayselle.copynet.entities.*;
import com.jayselle.copynet.components.TokenDecoder;
import com.jayselle.copynet.error.GenericResponse;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin(value = "http://localhost:8081")
@RestController
public class UserController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ApunteService apunteService;

    @Autowired
    PedidoService pedidoService;

    @Autowired
    DtoService dtoService;

    @Autowired
    TokenDecoder tokendecoder;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/api/users/user/{id}")
    public Usuario getUserDetail(@PathVariable Integer id){
        return usuarioService.findById(id);
    }

    @GetMapping("/api/users/user/pedidos")
    public ResponseEntity getPedidos(HttpServletRequest request, @RequestParam String estado){

        try {

            Alumno alumno = usuarioService.getAlumnoByIdUsuario(tokendecoder.getIdUser(request));

            List<Pedido> pedidos = pedidoService.getPedidosByEstadoAndLegajo(estado, alumno.getLegajo());

            List<PedidoDto> pedidoDtos = new ArrayList<>();

            for (Pedido pedido : pedidos) {

                pedidoDtos.add(dtoService.convertPedidoToDto(pedido));

            }

            GenericResponse gr = new GenericResponse(HttpStatus.OK, "Pedidos de " + alumno.getLegajo() + " con estado " + estado, pedidoDtos);
            return new ResponseEntity<>(gr, HttpStatus.OK);

        } catch (MyHttpException ex){
            GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
            return new ResponseEntity<>(gr, ex.getStatus());
        }

    }

    @PostMapping("/api/users/user/pedido")
    public ResponseEntity registrarPedido(HttpServletRequest request, @RequestBody PostPedidoDto postPedidoDto){

        Usuario usuario = usuarioService.findByUserId(tokendecoder.getIdUser(request));

        Pedido pedido = modelMapper.map(postPedidoDto,Pedido.class);
        List<PostLineaDto> postLineaDtos = postPedidoDto.getLineas();

        if (postLineaDtos.size()>0){
            List<LineaPedido> lineaPedidos = new ArrayList<>();
            for(PostLineaDto linea: postLineaDtos) {
                LineaPedido lineaPedido = modelMapper.map(linea,LineaPedido.class);
                Apunte apunte;
                try {
                    apunte = apunteService.findApunteById(linea.getApunte().getId_apunte());
                } catch (MyHttpException ex){
                    GenericResponse gr = new GenericResponse(ex.getStatus(),"Un apunte no fue encontrado");
                    return new ResponseEntity<>(gr, ex.getStatus());
                }
                lineaPedido.setApunte(apunte);
                lineaPedidos.add(lineaPedido);
            }

            pedido.setLineaspedido(lineaPedidos);
            pedido.setUsuario(usuario);

            try{
                pedidoService.registrarPedido(pedido);
                GenericResponse gr = new GenericResponse(HttpStatus.OK,"Pedido registrado");
                return new ResponseEntity<>(gr, HttpStatus.OK);
            } catch (MyHttpException ex){
                GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
                return new ResponseEntity<>(gr, ex.getStatus());
            }

        } else {
            GenericResponse gr = new GenericResponse(HttpStatus.UNPROCESSABLE_ENTITY,"Pedido sin lineas");
            return new ResponseEntity<>(gr, HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @PutMapping("/api/users/user/pedido")
    public ResponseEntity cancelarPedido(HttpServletRequest request, @RequestParam Long id_pedido){

        Usuario usuario = usuarioService.findByUserId(tokendecoder.getIdUser(request));

        try {
            pedidoService.cancelarPedido(id_pedido,usuario.getId_usuario());
            GenericResponse gr = new GenericResponse(HttpStatus.OK,"Pedido cancelado");
            return new ResponseEntity<>(gr, HttpStatus.OK);
        } catch (MyHttpException ex){
            GenericResponse gr = new GenericResponse(ex.getStatus(),ex.getMensaje());
            return new ResponseEntity<>(gr, ex.getStatus());
        }

    }

}
