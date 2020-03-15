package com.jayselle.copynet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChartDataDto<T,V extends Comparable> implements Comparable<ChartDataDto<T,V>>{

    private T texto;
    private V valor;

    @Override
    public int compareTo(ChartDataDto<T, V> o) {
        return Integer.compare((int)getValor(),(int)o.getValor());
    }
}