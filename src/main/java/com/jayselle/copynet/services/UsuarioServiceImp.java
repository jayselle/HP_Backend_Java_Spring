package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.*;
import com.jayselle.copynet.entities2.UAlumno;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.repositories.PedidoRepository;
import com.jayselle.copynet.repositories.RolRepository;
import com.jayselle.copynet.repositories.UsuarioRepository;
import com.jayselle.copynet.repositories2.UAlumnoRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.sql.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UAlumnoRepository uAlumnoRepository;

    @Autowired
    RolRepository rolRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findByUserName(username);

        if (!optionalUsuario.isPresent()){
            throw new MyHttpException(HttpStatus.NOT_FOUND, "Datos incorrectos. Probá de nuevo.");
        } else {
            Usuario usuario = optionalUsuario.get();
            if (!passwordEncoder.matches(password,usuario.getPassword_usuario())){
                throw new MyHttpException(HttpStatus.NOT_FOUND, "Datos incorrectos. Probá de nuevo.");
            } else {
                if (StringUtils.equals(usuario.getRol().getNombre_rol(),"alumno")){
                    Optional<Alumno> optionalAlumno = usuarioRepository.findAlumnoByUsername(usuario.getUser_usuario());
                    if (!optionalAlumno.isPresent()){
                        throw new MyHttpException(HttpStatus.NOT_FOUND, "Datos incorrectos. Probá de nuevo.");
                    } else {
                        Alumno alumno = optionalAlumno.get();
                        if (alumno.getHabilitado()){
                            return getJWTToker(usuario);
                        } else {
                            throw new MyHttpException(HttpStatus.UNAUTHORIZED, "Alumno inhabilitado. Acercate a fotocopiadora.");
                        }
                    }
                } else {
                    return getJWTToker(usuario);
                }
            }
        }

    }

    @Override
    public Usuario findById(Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.orElse(null);
    }

    @Override
    public Usuario findByUserId(Long id) {
        return usuarioRepository.findByUserId(id);
    }

    private String getJWTToker(Usuario user){

        String secreyKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRol().getNombre_rol());

        String token = Jwts
                .builder()
                .setId(user.getId_usuario().toString())
                .claim("user",user.getUser_usuario())
                .claim("nombre",user.getNombre_usuario()+" "+user.getApellido_usuario())
                .claim("authority",user.getRol().getNombre_rol())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .signWith(SignatureAlgorithm.HS512, secreyKey.getBytes()).compact();

        return token;

    }

    @Override
    public void registrarInhabilitaciones() {

        List<Alumno> alumnos = usuarioRepository.getAllAlumnos();

        for (Alumno alumno : alumnos){

            Calendar fechaHabilitacion = Calendar.getInstance();
            LocalDateTime localDateTime = alumno.getFecha_habilitacion();
            fechaHabilitacion.set(localDateTime.getYear(),localDateTime.getMonthValue(),localDateTime.getDayOfMonth(),localDateTime.getHour(),localDateTime.getMinute(),localDateTime.getSecond());

            int cantPedidosVencidos = 0;

            List<Pedido> pedidosVencidosAlumno = pedidoRepository.getPedidosByIdUsuarioAndEstado(alumno.getId_usuario(),"Vencido");

            for (Pedido pedido : pedidosVencidosAlumno){

                List<DetalleEstadoPedido> detalleEstadosPedido = pedido.getDetalleestadospedido();
                Collections.sort(detalleEstadosPedido, Collections.reverseOrder());

                if (fechaHabilitacion.before(detalleEstadosPedido.get(0).getFecha_detalle_estado_pedido())){

                    cantPedidosVencidos++;

                }

            }

            if (cantPedidosVencidos>=5){

                alumno.setHabilitado(false);
                usuarioRepository.save(alumno);

            }

        }

    }

    @Override
    public String registrarUsuario(Alumno alumno) {

        Optional optionalUAlumno = uAlumnoRepository.getUAlumnoByLegajoAndDNI(alumno.getLegajo(),alumno.getDni_usuario());
        if (!optionalUAlumno.isPresent()){
            throw new MyHttpException(HttpStatus.NOT_FOUND, "Legajo y/o DNI no coinciden con nuestros registros");
        } else {
            Optional optionalAlumno = usuarioRepository.findByAlumnoLegajo(alumno.getLegajo());
            if (optionalAlumno.isPresent()){
                throw new MyHttpException(HttpStatus.BAD_REQUEST, "Ya existe una cuenta registrada para el alumno");
            } else {
                Optional optionalUsuario = usuarioRepository.findByUserName(alumno.getUser_usuario());
                if (optionalUsuario.isPresent()){
                    throw new MyHttpException(HttpStatus.BAD_REQUEST, "Nombre de usuario ya registrado. Probá con otro");
                } else {
                    Optional optionalUsuarioEmail = usuarioRepository.findByUserEmail(alumno.getEmail_usuario());
                    if (optionalUsuarioEmail.isPresent()){
                        throw new MyHttpException(HttpStatus.BAD_REQUEST, "Email ya registrado. Probá con otro");
                    } else {
                        UAlumno uAlumno = (UAlumno) optionalUAlumno.get();
                        Usuario usuario = new Alumno(uAlumno.getNombre_ualumno(), uAlumno.getApellido_ualumno(), uAlumno.getDni_ualumno(),
                                alumno.getUser_usuario(), passwordEncoder.encode(alumno.getPassword_usuario()), alumno.getEmail_usuario(), alumno.getCelular_usuario(),
                                uAlumno.getLegajo_ualumno(),true, LocalDateTime.now());

                        usuario.setRol(rolRepository.getRolByName("alumno"));

                        usuarioRepository.save(usuario);

                        return getJWTToker(usuario);
                    }
                }
            }
        }

    }

    @Override
    public Alumno getAlumno(Long legajo) {
        Optional<Alumno> alumnoOptional = usuarioRepository.findByAlumnoLegajo(legajo);
        if (!alumnoOptional.isPresent()){
            throw new MyHttpException(HttpStatus.NOT_FOUND, "Alumno no encontrado");
        } else {
            return alumnoOptional.get();
        }
    }

    @Override
    public Alumno getAlumnoByIdUsuario(Long id) {
        Optional<Alumno> alumnoOptional = usuarioRepository.getAlumnoByIdUsuario(id);
        if (!alumnoOptional.isPresent()){
            throw new MyHttpException(HttpStatus.NOT_FOUND, "Alumno no encontrado");
        } else {
            return alumnoOptional.get();
        }
    }

    @Override
    public void habilitarAlumno(Long legajo) {
        Optional<Alumno> alumnoOptional = usuarioRepository.findByAlumnoLegajo(legajo);
        if (!alumnoOptional.isPresent()){
            throw new MyHttpException(HttpStatus.NOT_FOUND, "Alumno no encontrado");
        } else {
            Alumno alumno = alumnoOptional.get();
            alumno.setHabilitado(true);
            alumno.setFecha_habilitacion(LocalDateTime.now());
            usuarioRepository.save(alumno);
        }
    }
}
