package com.next.cajero.DAO;

import com.next.cajero.entities.Cuenta;
import com.next.cajero.entities.Movimiento;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MovimientoDAO {

    private Long id;
    private String tipo;
    private Long cantidad;
    private Date fecha;

    public MovimientoDAO(Movimiento mov){
        this.id = mov.getId();
        tipo = mov.getTipo();
        cantidad = mov.getCantidad();
        fecha = mov.getFecha();
    }

}