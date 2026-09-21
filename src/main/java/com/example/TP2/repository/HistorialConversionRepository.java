package com.example.TP2.repository;

import com.example.TP2.entity.HistorialConversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistorialConversionRepository extends JpaRepository<HistorialConversion, Integer> {

    @Query("SELECT h FROM HistorialConversion h " +
           "WHERE h.monedaOrigen = :origen " +
           "AND h.monedaDestino = :destino " +
           "ORDER BY h.fechaConsulta DESC")
    List<HistorialConversion> buscarHistorial(
            @Param("origen") String origen, 
            @Param("destino") String destino
    );
}