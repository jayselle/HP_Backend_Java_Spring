package com.jayselle.copynet.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AdminDashDto {

    private ChartDataDto<String, Integer> cantAlumnosRegistrados;

    private ChartDataDto<String, Float> gananciaTotal;

    private ChartDataDto<String, Integer> cantPedidosCobrados;

    private ChartDataDto<String, Integer> cantApuntesRegistrados;

    private List<ChartDataDto> dataCard;

    private List<ChartDataDto<String, Float>> gananciasMensualesPorAno;

    private ChartDataDto<String, String> mesConMayorGanancia;

    private ChartDataDto<String, Float> ingresoPromedioPorMes;

    private ChartDataDto<String, Float> gananciaAnualTotal;

    private ChartDataDto<String, String> apunteMasRequerido;

    private List<ChartDataDto<String, Integer>> rankingDeApuntes;

    private ChartDataDto<String, String> catedraMasRequerida;

    private List<ChartDataDto<String, Integer>> rankingDeCatedras;

    private ChartDataDto<String, String> departamentoMasRequerido;

    private List<ChartDataDto<String, Integer>> rankingDeDepartamentos;

    private List<ChartDataDto<String, Integer>> cantEstados;

    private ChartDataDto<String, Float> porcentajeAlumnosRegistrados;

    private ChartDataDto<String, Float> porcentajePedidosImpresos;

    private ChartDataDto<String, Float> porcentajePedidosCobrados;

}
