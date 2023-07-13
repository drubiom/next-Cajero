package com.next.cajero.service.impl;

import com.next.cajero.entities.Tarjeta;
import com.next.cajero.repository.TarjetaRepository;
import com.next.cajero.service.TarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class TarjetaServiceImpl implements TarjetaService {
    @Autowired
    TarjetaRepository tarjetaRepository;

    @Override
    public Optional<Tarjeta> findByNumero(String numero) {
        return tarjetaRepository.findByNumero(numero);
    }

    @Override
    public boolean activar(String numero) {
        Optional <Tarjeta> tarjeta = tarjetaRepository.findByNumero(numero);
        if (tarjeta.isPresent()) {
            Tarjeta recuperada = tarjeta.get();
            recuperada.setActiva(true);
            tarjetaRepository.save(recuperada);
            return true;
        }
        return false;
    }

    @Override
    public boolean cambiarPin(String numero, String nuevoPin) {
        Optional <Tarjeta> tarjeta = tarjetaRepository.findByNumero(numero);
        if (tarjeta.isPresent()) {
            String pinEncriptado = Base64.getEncoder().encodeToString(nuevoPin.getBytes(StandardCharsets.UTF_8));
            Tarjeta recuperada = tarjeta.get();
            recuperada.setPin(pinEncriptado);
            tarjetaRepository.save(recuperada);
            return true;
        }
        return false;
    }
}
