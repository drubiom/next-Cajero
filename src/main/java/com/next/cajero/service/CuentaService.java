package com.next.cajero.service;

import com.next.cajero.entities.Movimiento;

import java.util.List;

public interface CuentaService {

    boolean sacarDinero(String numTarjeta, Long cantidad);

    boolean ingresarDinero(String numTarjeta, Long cantidad);

    List<Movimiento> solicitaMovimientos(String numTarjeta);
}
