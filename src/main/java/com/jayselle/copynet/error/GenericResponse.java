package com.jayselle.copynet.error;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GenericResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private String mensaje;
    private Object data;

    public GenericResponse(HttpStatus httpStatus, String mensaje) {
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.mensaje = mensaje;
    }

    public GenericResponse(HttpStatus httpStatus, String mensaje, Object data) {
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.mensaje = mensaje;
        this.data = data;
    }

    public GenericResponse(Integer status, List<ObjectError> allErrors){
        this.timestamp = LocalDateTime.now();
        this.status = status;
        String temp = allErrors
                .stream()
                .map(error -> {
                    if (error instanceof FieldError){
                        return "{\"nombre\":\"" + ((FieldError) error).getField() + "\",\"mensaje\":\"" + error.getDefaultMessage() + "\"}";
                    } else {
                        return "{\"nombre\":\"" + error.getCode() + "\",\"mensaje\":\"" + error.getDefaultMessage() + "\"}";
                    }
                }).collect(Collectors.joining(","));
        this.mensaje = "[" + temp + "]";
    }

}
