package com.jayselle.copynet.schedule;

import com.jayselle.copynet.services.PedidoService;
import com.jayselle.copynet.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    PedidoService pedidoService;

    @Autowired
    UsuarioService usuarioService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void schedule() {

        pedidoService.registrarVencimientos();
        usuarioService.registrarInhabilitaciones();

    }

}
