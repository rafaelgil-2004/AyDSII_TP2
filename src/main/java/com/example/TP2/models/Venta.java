package com.example.TP2.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @NotBlank(message = "El producto no puede estar vacio")
    private String producto;

    @Positive(message = "La cantidad debe ser un entero positivo")
    private int cantidad;

    @Positive(message = "El precio unitario debe ser positivo")
    private double precioUnitario;

    public double getImporte() {
        return cantidad * precioUnitario;
    }
}
