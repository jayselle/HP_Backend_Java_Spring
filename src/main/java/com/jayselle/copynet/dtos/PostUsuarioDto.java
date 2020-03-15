package com.jayselle.copynet.dtos;

import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@GroupSequence({PostUsuarioDto.class, PostUsuarioDto.BasicInfo.class, PostUsuarioDto.AdvancedInfo.class})
public class PostUsuarioDto {

    @NotBlank(message = "Legajo: Completar")
    @Pattern(regexp = "^[0-9]{5}", message = "Legajo: Solo 5 numeros", groups = PostUsuarioDto.BasicInfo.class)
    private String legajo;

    @NotBlank(message = "DNI: Completar")
    @Pattern(regexp = "^[0-9]{8}", message = "DNI: Solo 8 numeros", groups = PostUsuarioDto.BasicInfo.class)
    private String dni_usuario;

    @NotEmpty(message = "Usuario: Completar")
    @Size(min = 3, message = "Usuario: Mínimo 3 caracteres", groups = PostUsuarioDto.BasicInfo.class)
    @Size(max = 20, message = "Usuario: Máximo 20 caracteres", groups = PostUsuarioDto.BasicInfo.class)
    @Pattern(regexp = "^((?![\\s$=<>+,.]).)*$", message = "Usuario: Sin espacios ni caracteres invalidos & = < > + , .", groups = PostUsuarioDto.AdvancedInfo.class)
    private String user_usuario;

    @NotEmpty(message = "Contraseña: Completar")
    @Size(min = 3, message = "Contraseña: Mínimo 3 caracteres", groups = PostUsuarioDto.BasicInfo.class)
    @Size(max = 20, message = "Contraseña: Máximo 20 caracteres", groups = PostUsuarioDto.BasicInfo.class)
    @Pattern(regexp = "^((?!\\s).)*$", message = "Contraseña: Sin espacios en blanco", groups = PostUsuarioDto.AdvancedInfo.class)
    private String password_usuario;

    @NotEmpty(message = "Email: Completar")
    @Pattern(regexp = ".+@.+", message = "Email: Formato invalido", groups = PostUsuarioDto.BasicInfo.class)
    private String email_usuario;


    @Pattern(regexp = "^(?:(?:00)?549?)?0?(?:11|[2368]\\d)(?:(?=\\d{0,2}15)\\d{2})??\\d{8}$|^$", message = "Celular: Formato invalido", groups = PostUsuarioDto.BasicInfo.class)
    private String celular_usuario;

    static interface BasicInfo {

    }

    static interface AdvancedInfo {

    }

}
