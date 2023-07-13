package com.next.cajero.service.impl;

import com.next.cajero.entities.Cuenta;
import com.next.cajero.entities.Movimiento;
import com.next.cajero.entities.Tarjeta;
import com.next.cajero.repository.CuentaRepository;
import com.next.cajero.repository.MovimientoRepository;
import com.next.cajero.repository.TarjetaRepository;
import com.next.cajero.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CuentaServiceImpl implements CuentaService {
    @Autowired
    TarjetaRepository tarjetaRepository;

    @Autowired
    CuentaRepository cuentaRepository;

    @Autowired
    MovimientoRepository movimientoRepository;

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
                Movimiento mov = new Movimiento();
                mov.setTipo("Comisión");
                mov.setFecha(new Date());
                mov.setCantidad(comision);
                mov.setCuenta(cuenta);
                movimientoRepository.save(mov);

            }else{
                //No tiene comision
                cuenta.setSaldo(cuenta.getSaldo()-cantidad);
            }
            Movimiento mov = new Movimiento();
            mov.setTipo("Retirada");
            mov.setFecha(new Date());
            mov.setCantidad(cantidad);
            mov.setCuenta(cuenta);
            movimientoRepository.save(mov);
            cuentaRepository.save(cuenta);
            return true;
        }
        return false;
    }

    public boolean ingresarDinero(String numero, Long cantidad){

        Optional<Tarjeta> tarjeta = tarjetaRepository.findByNumero(numero);
        if (tarjeta.isPresent()) {
            Tarjeta recuperada = tarjeta.get();
            Cuenta cuenta = recuperada.getCuenta();

            //comprobar si es banco externo
            if(cuenta.getBanco().getComision() != 0){
                return false;
            }else{
                cuenta.setSaldo(cuenta.getSaldo()+cantidad);
            }
            Movimiento mov = new Movimiento();
            mov.setTipo("Ingreso");
            mov.setFecha(new Date());
            mov.setCantidad(cantidad);
            mov.setCuenta(cuenta);
            movimientoRepository.save(mov);
            cuentaRepository.save(cuenta);
            return true;
        }
        return false;
    }

    @Override
    public List<Movimiento> solicitaMovimientos(String numTarjeta) {
        Optional<Tarjeta> tarjeta = tarjetaRepository.findByNumero(numTarjeta);
        if (tarjeta.isPresent()) {
            Tarjeta recuperada = tarjeta.get();
            Cuenta cuenta = recuperada.getCuenta();
            Optional<List<Movimiento>> movimientos = movimientoRepository.findByCuenta(cuenta);
            if (movimientos.isPresent()) {
                List<Movimiento> result = movimientos.get();
                return result;
            }
        }
        return null;
    }

    @Override
    public boolean transferencia(String numTarjeta, String iban, Long cantidad) {
        Optional<Tarjeta> tarjeta = tarjetaRepository.findByNumero(numTarjeta);
        if (tarjeta.isPresent()) {
            Tarjeta recuperada = tarjeta.get();
            Cuenta cuenta = recuperada.getCuenta();
            //TODO Validar IBAN
            if(cuenta.getBanco().getComision() != 0){
                //Tiene comision
                Long comision = cantidad*cuenta.getBanco().getComision();
                Long cantidadTotal = cantidad+comision;
                cuenta.setSaldo(cuenta.getSaldo()-cantidadTotal);
                Movimiento mov = new Movimiento();
                mov.setTipo("Comisión");
                mov.setFecha(new Date());
                mov.setCantidad(comision);
                mov.setCuenta(cuenta);
                movimientoRepository.save(mov);

            }else{
                //No tiene comision
                cuenta.setSaldo(cuenta.getSaldo()-cantidad);
            }
            //TODO ingresar en la cuenta de destino el dinero
            Movimiento mov = new Movimiento();
            mov.setTipo("Transferencia");
            mov.setFecha(new Date());
            mov.setCantidad(cantidad);
            mov.setCuenta(cuenta);
            movimientoRepository.save(mov);
            cuentaRepository.save(cuenta);
            return true;
        }
        return false;
    }
}
