package com.jayselle.copynet.exception;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public class MyHttpException extends RuntimeException{

    private String mensaje;
    private HttpStatus status;

    public MyHttpException(HttpStatus status,String mensaje) {
        this.status = status;
        this.mensaje = mensaje;
    }

    public MyHttpException(HttpStatus status,List<String> mensaje) {
        this.status = status;
        String msj = mensaje
                .stream()
                .map(unMensaje -> { return "{\"mensaje\":\"" + unMensaje + "\"}";
                }).collect(Collectors.joining(","));
        this.mensaje = "["+msj+"]";
    }

    public String getMensaje() {
        return mensaje;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
