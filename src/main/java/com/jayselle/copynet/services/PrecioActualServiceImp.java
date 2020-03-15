package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.PrecioActual;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.repositories.PrecioActualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class PrecioActualServiceImp implements PrecioActualService {

    @Autowired
    PrecioActualRepository precioActualRepository;

    @Override
    public PrecioActual getPrecioByDate(LocalDateTime localDateTime) {
        return precioActualRepository.getPrecioByDate(localDateTime);
    }

    @Override
    public void actualizarPrecio(PrecioActual precioNuevo) {
        if (!Pattern.matches("^(\\d*\\.)?\\d+$",precioNuevo.getPreciocopia_precioactual().toString()) || !Pattern.matches("^(\\d*\\.)?\\d+$",precioNuevo.getPreciocolor_precioactual().toString()) || !Pattern.matches("^(\\d*\\.)?\\d+$",precioNuevo.getPrecioanillado_precioactual().toString())){
            throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Solo numeros enteros o decimales");
        } else {

            PrecioActual precioVigente = getPrecioByDate(LocalDateTime.now());
            if ((precioVigente.getPreciocopia_precioactual().compareTo(precioNuevo.getPreciocopia_precioactual()) == 0) &&
                    (precioVigente.getPreciocolor_precioactual().compareTo(precioNuevo.getPreciocolor_precioactual()) == 0) &&
                    (precioVigente.getPrecioanillado_precioactual().compareTo(precioNuevo.getPrecioanillado_precioactual()) == 0)){

                throw new MyHttpException(HttpStatus.CONFLICT, "No hay ningún cambio");

            } else {

                precioVigente.setFechahasta_precioactual(LocalDateTime.now());
                precioNuevo.setFechadesde_precioactual(LocalDateTime.now());
                precioNuevo.setFechahasta_precioactual(LocalDateTime.now().plusYears(5000));
                precioActualRepository.save(precioNuevo);

            }
        }
    }

    @Override
    public List<PrecioActual> getPrecios() {
        return precioActualRepository.findAll();
    }
}
