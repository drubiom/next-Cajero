package com.next.cajero.service;

import com.next.cajero.entities.Tarjeta;

import java.util.Optional;

public interface TarjetaService {
    Optional<Tarjeta> findByNumero(String numero);
    boolean activar(String numero);

    boolean cambiarPin(String numero, String nuevoPin);
}
