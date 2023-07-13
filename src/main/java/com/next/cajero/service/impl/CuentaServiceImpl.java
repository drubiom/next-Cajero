package com.next.cajero.service.impl;

import com.next.cajero.entities.Cuenta;
import com.next.cajero.entities.Tarjeta;
import com.next.cajero.repository.CuentaRepository;
import com.next.cajero.repository.TarjetaRepository;
import com.next.cajero.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CuentaServiceImpl implements CuentaService {
    @Autowired
    TarjetaRepository tarjetaRepository;

    @Autowired
    CuentaRepository cuentaRepository;

    public boolean sacarDinero(String numero, Long cantidad){

        Optional<Tarjeta> tarjeta = tarjetaRepository.findByNumero(numero);
        if (tarjeta.isPresent()) {
            Tarjeta recuperada = tarjeta.get();
            Cuenta cuenta = recuperada.getCuenta();
            //Comprobar si cantidad a sacar supera limite
            if(recuperada.getTipo().equals("D")){
                //Debito
                if(cuenta.getSaldo()<cantidad){
                    return false;
                }
            }else{
                //Crédito
                if(recuperada.getLimite()<cantidad){
                    return false;
                }
            }
            //comprobar si tiene comision
            if(cuenta.getBanco().getComision() != 0){
                //Tiene comision
                Long comision = cantidad*cuenta.getBanco().getComision();
                Long cantidadTotal = cantidad+comision;
                cuenta.setSaldo(cuenta.getSaldo()-cantidadTotal);
            }else{
                //No tiene comision
                cuenta.setSaldo(cuenta.getSaldo()-cantidad);
            }
            cuentaRepository.save(cuenta);
            return true;
        }
        return false;
    }
}
