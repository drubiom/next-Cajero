package com.next.cajero.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    private String tipo;
    private String pin;
    private Long limite;
    private boolean activa;
    @ManyToOne
    private Cuenta cuenta;

}
