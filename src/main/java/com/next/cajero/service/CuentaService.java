package com.next.cajero.service;

public interface CuentaService {

    boolean sacarDinero(String numTarjeta, Long cantidad);
}
