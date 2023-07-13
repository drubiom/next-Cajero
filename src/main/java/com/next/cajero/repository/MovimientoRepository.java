package com.next.cajero.repository;

import com.next.cajero.entities.Cuenta;
import com.next.cajero.entities.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    Optional<List<Movimiento>> findByCuenta(Cuenta cuenta);
}
