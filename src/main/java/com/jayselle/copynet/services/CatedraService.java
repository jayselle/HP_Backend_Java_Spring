package com.jayselle.copynet.services;

import com.jayselle.copynet.entities.Catedra;

public interface CatedraService {

    void registrarCatedra(Catedra catedra);
    Catedra getCatedraByDepartamentoAndNivel(String catedra, String departamento, String nivel);

}
