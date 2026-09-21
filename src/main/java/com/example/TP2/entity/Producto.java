package com.example.TP2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    private String descripcion;

    //precision cantidad de numeros que tienen el numero en total
    //scale cauntos numeros despues de la coma puede tener
    //sql: precio DECIMAL(10,2)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    //para indicar la relaciones(constrains) de la BD
    @ManyToOne(fetch = FetchType.LAZY)//no trer informacion de categoria hasta que se hago un getCategoria() | eager la trae en el momento
    @JoinColumn(name = "categoria_id")//para indicar la clave foranea
    private Categoria categoria;
}
