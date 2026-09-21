package com.example.TP2.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa un producto en el catálogo")

public class Producto {

    @NotBlank 
    @Schema (description = "Identificador del producto", example = "100")
    private Long id;

    @NotBlank 
    @Schema (description = "Nombre del producto", example = "teclado")
    private String nombre;

    @NotBlank 
    @Schema (description = "Categoria a la que pertenece", example = "Periferico")
    private String categoria;

    @Positive 
    @Schema (description = "Valor del Producto", example = "100000")
    private double precio;

    @PositiveOrZero 
    @Schema (description = "Cantidad en posecion", example = "50")
    private int stock;

}
