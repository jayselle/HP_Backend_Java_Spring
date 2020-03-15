package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.Catedra;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.repositories.CatedraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CatedraServiceImp implements CatedraService {

    @Autowired
    CatedraRepository catedraRepository;

    @Override
    public void registrarCatedra(Catedra catedra) {

         String nombreCatedraTrim = catedra.getNombre_catedra().trim();
         String newNombreCatedra = nombreCatedraTrim.substring(0, 1).toUpperCase() + nombreCatedraTrim.substring(1).toLowerCase();
         catedra.setNombre_catedra(newNombreCatedra);

         Optional<Catedra> catedraOptional = catedraRepository.getCatedraByDepartamentoAndNivel(catedra.getNombre_catedra(),catedra.getDepartamento().getNombre_departamento(),catedra.getNivel().getNombre_nivel());
         if (catedraOptional.isPresent()){
             throw new MyHttpException(HttpStatus.CONFLICT, "Catedra ya existente para: '"+catedra.getDepartamento().getNombre_departamento()+" - "+catedra.getNivel().getNombre_nivel()+"'");
         } else {
             catedraRepository.save(catedra);
         }

    }

    @Override
    public Catedra getCatedraByDepartamentoAndNivel(String catedra, String departamento, String nivel) {
        Optional<Catedra> optionalCatedra = catedraRepository.getCatedraByDepartamentoAndNivel(catedra,departamento,nivel);
        if (optionalCatedra.isPresent()){
            return optionalCatedra.get();
        } else {
            throw new MyHttpException(HttpStatus.NOT_FOUND, "La catedra no existe");
        }
    }
}
