package com.example.TP2.service;

import com.example.TP2.exception.DatosInvalidosException;
import com.example.TP2.models.ClienteDTO;
import com.example.TP2.entity.Cliente;
import com.example.TP2.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    
    public Cliente altaSimple(ClienteDTO dto) {
        return clienteRepository.save(mapearAEntidad(dto));
    }

    
    public Cliente altaValidada(ClienteDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail()) > 0) {
            throw new DatosInvalidosException("El email ya esta registrado");
        }
        return clienteRepository.save(mapearAEntidad(dto));
    }

    private Cliente mapearAEntidad(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setFechaRegistro(LocalDateTime.now());
        return cliente;
    }
}