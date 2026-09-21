package com.example.TP2.repository;

import com.example.TP2.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//JpaRepository<Entidad, tipo de dato de la PK>
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.email = :email")
    Integer existsByEmail(@Param("email") String email);
}