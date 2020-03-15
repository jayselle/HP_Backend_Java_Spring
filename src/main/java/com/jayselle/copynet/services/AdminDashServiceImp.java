package com.jayselle.copynet.services;

import com.jayselle.copynet.dtos.AdminDashDto;
import com.jayselle.copynet.dtos.ChartDataDto;
import com.jayselle.copynet.dtos.PedidoDto;
import com.jayselle.copynet.entities.Pedido;
import com.jayselle.copynet.exception.MyHttpException;
import com.jayselle.copynet.repositories.*;
import com.jayselle.copynet.repositories2.UAlumnoRepository;
import org.apache.commons.lang3.StringUtils;
import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminDashServiceImp implements AdminDashService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ApunteRepository apunteRepository;

    @Autowired
    CatedraRepository catedraRepository;

    @Autowired
    DepartamentoRepository departamentoRepository;

    @Autowired
    UAlumnoRepository uAlumnoRepository;

    @Autowired
    DtoService dtoService;

    @Override
    public AdminDashDto getAdminDashboard() {

        AdminDashDto adminDashDto = new AdminDashDto();

        adminDashDto.setCantAlumnosRegistrados(new ChartDataDto<>("Alumnos registrados",usuarioRepository.getCantAlumnosRegistrados()));
        adminDashDto.setCantApuntesRegistrados(new ChartDataDto<>("Apuntes registrados",apunteRepository.getCantApuntesRegistrados()));

        List<Pedido> pedidosCobrados = pedidoRepository.getPedidosByEstado("Cobrado");
        adminDashDto.setCantPedidosCobrados(new ChartDataDto<>("Pedidos cobrados",pedidosCobrados.size()));
        List<PedidoDto> pedidosDtos = dtoService.convertPedidosToDto(pedidosCobrados);
        adminDashDto.setGananciaTotal(new ChartDataDto<>("Ganancia total",calcularGananciaTotal(pedidosDtos)));

        List<Pedido> pedidosCobradosEnAno = pedidoRepository.getPedidosByEstadoAndYear("Cobrado",2019);
        List<PedidoDto> pedidosCobradosEnAnoDtos = dtoService.convertPedidosToDto(pedidosCobradosEnAno);

        List<ChartDataDto<String, Float>> gananciasMensualesPorAño = getGananciasMensualesPorAño(pedidosCobradosEnAnoDtos);
        adminDashDto.setGananciasMensualesPorAno(gananciasMensualesPorAño);

        float gananciaAnualTotal = 0;
        float gananciaMes = 0;
        ChartDataDto<String, String> mesConMayorGanancia = new ChartDataDto<>("Mes con mayor ingresos","");
        for (ChartDataDto<String, Float> chartDataDto : gananciasMensualesPorAño){
            gananciaAnualTotal = gananciaAnualTotal + chartDataDto.getValor();
            if (chartDataDto.getValor()>gananciaMes){
                mesConMayorGanancia.setValor(chartDataDto.getTexto());
                gananciaMes = chartDataDto.getValor();
            }
        }

        adminDashDto.setMesConMayorGanancia(mesConMayorGanancia);
        adminDashDto.setIngresoPromedioPorMes(new ChartDataDto<>("Ingreso promedio por mes",rounder((double)calcularGananciaTotal(pedidosCobradosEnAnoDtos)/12)));
        adminDashDto.setGananciaAnualTotal(new ChartDataDto<>("Total ganancia anual",calcularGananciaTotal(pedidosCobradosEnAnoDtos)));

        List<String> apuntesEnPedidosCobrados = apunteRepository.getApuntesEnPedidosCobrados();
        List<ChartDataDto<String, Integer>> rankingDeApuntesCobrados = setRanking(apuntesEnPedidosCobrados);

        adminDashDto.setRankingDeApuntes(rankingDeApuntesCobrados);
        adminDashDto.setApunteMasRequerido(new ChartDataDto<>("Apunte mas requerido",rankingDeApuntesCobrados.get(0).getTexto()));

        List<String> catedrasEnPedidosCobrados = catedraRepository.getCatedrasEnPedidosCobrados();
        List<ChartDataDto<String, Integer>> rankingDeCatedrasEnPedidosCobrados = setRanking(catedrasEnPedidosCobrados);

        adminDashDto.setRankingDeCatedras(rankingDeCatedrasEnPedidosCobrados);
        adminDashDto.setCatedraMasRequerida(new ChartDataDto<>("Catedra mas requerida",rankingDeCatedrasEnPedidosCobrados.get(0).getTexto()));

        List<String> departamentosEnPedidosCobrados = departamentoRepository.getDepartamentosEnPedidosCobrados();
        List<ChartDataDto<String, Integer>> rankingDeDepartamentosEnPedidosCobrados = setRanking(departamentosEnPedidosCobrados);

        adminDashDto.setRankingDeDepartamentos(rankingDeDepartamentosEnPedidosCobrados);
        adminDashDto.setDepartamentoMasRequerido(new ChartDataDto<>("Departamento mas requerido",rankingDeDepartamentosEnPedidosCobrados.get(0).getTexto()));

        List<ChartDataDto<String, Integer>> cantEstados = new ArrayList<>();
        Integer cantPedidosPendientes = pedidoRepository.getCantEstados("Pendiente");
        cantEstados.add(new ChartDataDto<>("Pendiente",cantPedidosPendientes));
        Integer cantPedidosImpresos = pedidoRepository.getCantEstados("Impreso");
        cantEstados.add(new ChartDataDto<>("Impreso",cantPedidosImpresos));
        Integer cantPedidosCobrados = pedidoRepository.getCantEstados("Cobrado");
        cantEstados.add(new ChartDataDto<>("Cobrado",cantPedidosCobrados));
        cantEstados.add(new ChartDataDto<>("Cancelado",pedidoRepository.getCantEstados("Cancelado")));
        cantEstados.add(new ChartDataDto<>("Vencido",pedidoRepository.getCantEstados("Vencido")));
        adminDashDto.setCantEstados(cantEstados);

        double porcentajeAlumnosRegistrados = ((double)adminDashDto.getCantAlumnosRegistrados().getValor() / (double)uAlumnoRepository.getCantUAlumnos())*100;
        adminDashDto.setPorcentajeAlumnosRegistrados(new ChartDataDto<>("Alumnos registrados (%)",(float) DoubleRounder.round(porcentajeAlumnosRegistrados,2)));

        adminDashDto.setPorcentajePedidosImpresos(new ChartDataDto<>("Apuntes impresos (%)",calcularPorcentaje(cantPedidosPendientes,cantPedidosImpresos)));

        adminDashDto.setPorcentajePedidosCobrados(new ChartDataDto<>("Apuntes cobrados (%)",calcularPorcentaje(cantPedidosImpresos,cantPedidosCobrados)));

        return adminDashDto;

    }

    private Float sumarValores(Float valor1, Float valor2){
        return rounder(valor1.doubleValue()+valor2.doubleValue());
    }

    private Float rounder(Double valor){
        return (float) DoubleRounder.round(valor,2);
    }

    private List<ChartDataDto<String, Float>> getGananciasMensualesPorAño(List<PedidoDto> pedidoDtos){

        List<ChartDataDto<String, Float>> gananciasMensualesPorAño = new ArrayList<>();

        List<String> meses = Arrays.asList("En.","Feb.","Mar.","Ab.","May.","Jun.","Jul.","Ag.","Sept.","Oct.","Nov.","Dic.");

        for(String mes : meses){
            gananciasMensualesPorAño.add(new ChartDataDto<>(mes,0f));
        }

        for (PedidoDto pedido : pedidoDtos){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                calendar.setTime(sdf.parse(pedido.getFecha_pedido()));

                switch (calendar.getTime().getMonth()){
                    case 0:{
                        gananciasMensualesPorAño.get(0).setValor(sumarValores(gananciasMensualesPorAño.get(0).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                    case 1:{
                        gananciasMensualesPorAño.get(1).setValor(sumarValores(gananciasMensualesPorAño.get(1).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                    case 2:{
                        gananciasMensualesPorAño.get(2).setValor(sumarValores(gananciasMensualesPorAño.get(2).getValor(),pedido.getPrecio_pedido()));;
                        break;
                    }
                    case 3:{
                        gananciasMensualesPorAño.get(3).setValor(sumarValores(gananciasMensualesPorAño.get(3).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                    case 4:{
                        gananciasMensualesPorAño.get(4).setValor(sumarValores(gananciasMensualesPorAño.get(4).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                    case 5:{
                        gananciasMensualesPorAño.get(5).setValor(sumarValores(gananciasMensualesPorAño.get(5).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                    case 6:{
                        gananciasMensualesPorAño.get(6).setValor(sumarValores(gananciasMensualesPorAño.get(6).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                    case 7:{
                        gananciasMensualesPorAño.get(7).setValor(sumarValores(gananciasMensualesPorAño.get(7).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                    case 8:{
                        gananciasMensualesPorAño.get(8).setValor(sumarValores(gananciasMensualesPorAño.get(8).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                    case 9:{
                        gananciasMensualesPorAño.get(9).setValor(sumarValores(gananciasMensualesPorAño.get(9).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                    case 10:{
                        gananciasMensualesPorAño.get(10).setValor(sumarValores(gananciasMensualesPorAño.get(10).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                    case 11:{
                        gananciasMensualesPorAño.get(11).setValor(sumarValores(gananciasMensualesPorAño.get(11).getValor(),pedido.getPrecio_pedido()));
                        break;
                    }
                }
            } catch (Exception e){
                throw new MyHttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Error");
            }
        }

        return gananciasMensualesPorAño;

    }

    private List<ChartDataDto<String, Integer>> setRanking(List<String> valores){

        List<ChartDataDto<String, Integer>> ranking = new ArrayList<>();

        for(String valor : valores){

            boolean flag = false;
            for (ChartDataDto<String, Integer> chartDataDto : ranking){
                if (StringUtils.equals(valor,chartDataDto.getTexto())){
                    flag = true;
                }
            }
            if (!flag){
                ChartDataDto<String, Integer> chartDataDto = new ChartDataDto<>(valor,Collections.frequency(valores,valor));
                ranking.add(chartDataDto);
            }

        }

        Collections.sort(ranking, Collections.reverseOrder());

        return ranking;

    }

    private Float calcularPorcentaje(Number valor1, Number valor2){
        return rounder(100 - (valor1.doubleValue()/(valor1.doubleValue()+valor2.doubleValue()))*100);
    }

    private Float calcularGananciaTotal(List<PedidoDto> pedidoDtos){

        Float gananciaTotal = 0f;

        for (PedidoDto pedidoDto : pedidoDtos){
            gananciaTotal = gananciaTotal + pedidoDto.getPrecio_pedido();
        }

        return gananciaTotal;
    }

}
