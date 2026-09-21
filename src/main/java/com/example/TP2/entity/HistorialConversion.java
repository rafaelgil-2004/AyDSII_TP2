package com.example.TP2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_conversiones")
@Data
@NoArgsConstructor
public class HistorialConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "moneda_origen", nullable = false, length = 3)
    private String monedaOrigen;

    @Column(name = "moneda_destino", nullable = false, length = 3)
    private String monedaDestino;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(name = "monto_convertido", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoConvertido;

    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal tasa;

    @Column(name = "fecha_consulta", nullable = false)
    private LocalDateTime fechaConsulta;
}