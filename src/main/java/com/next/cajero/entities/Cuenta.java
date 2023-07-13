package com.next.cajero.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.List;

@Entity
@Data
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String iban;
    private DecimalFormat saldo;
    @ManyToOne
    private Banco banco;
    @ManyToMany(mappedBy = "cuentas")
    private List<Usuario> users;
}
