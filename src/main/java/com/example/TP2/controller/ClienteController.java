package com.example.TP2.controller;

import com.example.TP2.models.ClienteDTO;
import com.example.TP2.entity.Cliente;
import com.example.TP2.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Alta de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "alta de cliente sin validaciones")
    public ResponseEntity<Map<String, Object>> altaSimple(@RequestBody ClienteDTO dto) {
        return respuesta(clienteService.altaSimple(dto));
    }

    @PostMapping("/validado")
    @Operation(summary = "alta de cliente con validacion")
    public ResponseEntity<Map<String, Object>> altaValidada(@Valid @RequestBody ClienteDTO dto) {
        return respuesta(clienteService.altaValidada(dto));
    }

    private ResponseEntity<Map<String, Object>> respuesta(Cliente cliente) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", HttpStatus.CREATED.value());
        respuesta.put("message", "Cliente creado con exito");
        respuesta.put("data", cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}