package com.next.cajero.controller;

import com.next.cajero.DAO.MovimientoDAO;
import com.next.cajero.entities.Movimiento;
import com.next.cajero.service.CuentaService;
import com.next.cajero.service.TarjetaService;
import com.next.cajero.validate.ValidadorTarjeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CajeroController {
    @Autowired
    ValidadorTarjeta validadorTarjeta;

    @Autowired
    TarjetaService tarjetaService;

    @Autowired
    CuentaService cuentaService;
    @GetMapping(value = "movimientos")
    public ResponseEntity getMovimientos(
            @RequestParam(value = "numTarjeta") String numTarjeta,
            @RequestParam(value = "pin") String pin) {
        try{
            //Tarjeta activada?
            if(validadorTarjeta.activa(numTarjeta)){
                //Pin correcto?
                if(validadorTarjeta.pinCorrecto(numTarjeta, pin)) {
                    List<Movimiento> movimientos = cuentaService.solicitaMovimientos(numTarjeta);
                    List <MovimientoDAO> result = new ArrayList<>();
                    for (Movimiento movimiento : movimientos) {
                        result.add(new MovimientoDAO(movimiento));
                    }
                    if (!result.isEmpty())
                        return new ResponseEntity(result, HttpStatus.OK);
                }else{
                    return new ResponseEntity("Pin incorrecto", HttpStatus.OK);
                }
            }else{
                return new ResponseEntity("Tarjeta no activada", HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity("Tarjeta no activada", HttpStatus.OK);
    }

    @GetMapping(value = "activar")
    public ResponseEntity activarTarjeta(
            @RequestParam(value = "numTarjeta") String numTarjeta,
            @RequestParam(value = "pin") String pin) {
        try{
            //Tarjeta activada?
            if(!validadorTarjeta.activa(numTarjeta)){
                if(validadorTarjeta.pinCorrecto(numTarjeta, pin)) {
                    if(tarjetaService.activar(numTarjeta)) {
                        return new ResponseEntity("Tarjeta activada", HttpStatus.OK);
                    }else {
                        return new ResponseEntity("Error al activar tarjeta", HttpStatus.OK);
                    }
                }else{
                    return new ResponseEntity("Pin incorrecto", HttpStatus.OK);
                }
            }else{
                return new ResponseEntity("Tarjeta ya activada", HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "cambiarPin")
    public ResponseEntity cambiarPin(
            @RequestParam(value = "numTarjeta") String numTarjeta,
            @RequestParam(value = "pin") String pin,
            @RequestParam(value = "nuevoPin") String nuevoPin) {
        try{
            //Tarjeta activada?
            if(validadorTarjeta.activa(numTarjeta)){
                if(validadorTarjeta.pinCorrecto(numTarjeta, pin)) {
                    if(tarjetaService.cambiarPin(numTarjeta, nuevoPin)){
                        return new ResponseEntity("Pin cambiado", HttpStatus.OK);
                    }else{
                        return new ResponseEntity("Error al cambiar pin", HttpStatus.OK);
                    }

                }else{
                    return new ResponseEntity("Pin incorrecto", HttpStatus.OK);
                }
            }else{
                return new ResponseEntity("Tarjeta no activada", HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "sacarDinero")
    public ResponseEntity sacarDinero(
            @RequestParam(value = "numTarjeta") String numTarjeta,
            @RequestParam(value = "pin") String pin,
            @RequestParam(value = "cantidad") Long cantidad) {
        try{
            //Tarjeta activada?
            if(validadorTarjeta.activa(numTarjeta)){
                if(validadorTarjeta.pinCorrecto(numTarjeta, pin)) {
                    if(cuentaService.sacarDinero(numTarjeta ,cantidad)){
                        return new ResponseEntity("Dinero Retirado", HttpStatus.OK);
                    }else{
                        return new ResponseEntity("Error al retirar dinero", HttpStatus.OK);
                    }
                }else{
                    return new ResponseEntity("Pin incorrecto", HttpStatus.OK);
                }
            }else{
                return new ResponseEntity("Tarjeta no activada", HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "ingresarDinero")
    public ResponseEntity ingresarDinero(
            @RequestParam(value = "numTarjeta") String numTarjeta,
            @RequestParam(value = "pin") String pin,
            @RequestParam(value = "cantidad") Long cantidad) {
        try{
            //Tarjeta activada?
            if(validadorTarjeta.activa(numTarjeta)){
                if(validadorTarjeta.pinCorrecto(numTarjeta, pin)) {
                    if(cuentaService.ingresarDinero(numTarjeta ,cantidad)){
                        return new ResponseEntity("Dinero Ingresado", HttpStatus.OK);
                    }else{
                        return new ResponseEntity("Error al ingresar dinero", HttpStatus.OK);
                    }
                }else{
                    return new ResponseEntity("Pin incorrecto", HttpStatus.OK);
                }
            }else{
                return new ResponseEntity("Tarjeta no activada", HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
