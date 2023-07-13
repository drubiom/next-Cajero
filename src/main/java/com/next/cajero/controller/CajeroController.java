package com.next.cajero.controller;

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

@RestController
public class CajeroController {
    @Autowired
    ValidadorTarjeta validadorTarjeta;

    @Autowired
    TarjetaService tarjetaService;

    @GetMapping(value = "movimientos")
    public ResponseEntity getMovimientos(
            @RequestParam(value = "numTarjeta") String numTarjeta,
            @RequestParam(value = "pin") String pin) {
        try{
            //Tarjeta activada?
            if(!validadorTarjeta.activa(numTarjeta)){
                return new ResponseEntity("Tarjeta no activada", HttpStatus.OK);
            }
            //Pin correcto?

            return new ResponseEntity("", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "activar")
    public ResponseEntity activarTarjeta(
            @RequestParam(value = "numTarjeta") String numTarjeta,
            @RequestParam(value = "pin") String pin) {
        try{
            //Tarjeta activada?
            if(!validadorTarjeta.activa(numTarjeta)){
                if(validadorTarjeta.pinCorrecto(numTarjeta, pin)) {
                    tarjetaService.activar(numTarjeta);
                    return new ResponseEntity("Tarjeta activada", HttpStatus.OK);
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
                    tarjetaService.cambiarPin(numTarjeta, nuevoPin);
                    return new ResponseEntity("Pin cambiado", HttpStatus.OK);
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
}
