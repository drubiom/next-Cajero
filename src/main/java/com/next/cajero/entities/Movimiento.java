package com.next.cajero.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.Date;

@Entity
@Data
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private Long cantidad;
    private Date fecha;
    @ManyToOne
    private Cuenta cuenta;
}
