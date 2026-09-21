package com.example.TP2.repository;

import com.example.TP2.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN p.detalles d " +
           "LEFT JOIN d.producto pr " +
           "LEFT JOIN pr.categoria cat " +
           "WHERE (:clienteId IS NULL OR p.cliente.id = :clienteId) " +
           "AND (:categoria IS NULL OR cat.nombre = :categoria) " +
           "AND (:fechaDesde IS NULL OR p.fechaPedido >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR p.fechaPedido <= :fechaHasta) " +
           "AND (:estado IS NULL OR p.estado = :estado)")

    List<Pedido> buscarConFiltros(
            @Param("clienteId") Integer clienteId,
            @Param("categoria") String categoria,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("estado") String estado);
}
