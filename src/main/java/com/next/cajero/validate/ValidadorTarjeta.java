package com.next.cajero.validate;

import com.next.cajero.entities.Tarjeta;
import com.next.cajero.service.TarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
public class ValidadorTarjeta {
    @Autowired
    private TarjetaService tarjetaService;
    public boolean activa(String numero){
        Optional<Tarjeta> tarjeta = tarjetaService.findByNumero(numero);
        if(tarjeta.isPresent()){
            if(tarjeta.get().isActiva())
                return true;
        }
        return false;
    }

    public boolean pinCorrecto(String numero, String pin){
        Optional<Tarjeta> tarjeta = tarjetaService.findByNumero(numero);
        if(tarjeta.isPresent()){
            String pinEncriptado = Base64.getEncoder().encodeToString(pin.getBytes(StandardCharsets.UTF_8));
            Tarjeta temporal = tarjeta.get();
            if(temporal.getPin().equals(pinEncriptado))
                return true;
        }
        return false;
    }
}
