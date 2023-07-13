package com.next.cajero.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long numero;
    private String tipo;
    private String pin;
    private double limite;
    private boolean activa;
    @ManyToOne
    private Cuenta cuenta;

}
