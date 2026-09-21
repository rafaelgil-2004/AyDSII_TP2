package com.example.TP2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity//mapear como persistente
//tabla que representa
@Table(name = "clientes")//sin esto va a tomar que la tabla es CLiente no clientes como esta en la BD
@Data
@NoArgsConstructor
public class Cliente {

    @Id //clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //para indicar que el id lo genera la bse de datos(el auto_increment)
    private Integer id;

    @Column(nullable = false, length = 100)//estas cosas salen de la definicion en la BD
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    //@collum solo se usa cuando es necesario indicar algo relacionado con la columna
    private String telefono;

    @Column(name = "fecha_registro")//igual que con el table
    private LocalDateTime fechaRegistro;
}