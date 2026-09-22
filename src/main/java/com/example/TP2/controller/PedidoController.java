package com.example.TP2.controller;

import com.example.TP2.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Consulta de pedidos con filtros combinables ")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("/buscar")
    @Operation(summary = "Busca pedidos combinando filtros opcionales ")
    public ResponseEntity<Map<String, Object>> buscar(
            @Parameter(description = "Id del cliente") @RequestParam(required = false) Integer clienteId,
            @Parameter(description = "Nombre de la categoria") @RequestParam(required = false) String categoria,
            @Parameter(description = "Formato yyyy-MM-dd")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @Parameter(description = "Formato yyyy-MM-dd")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @Parameter(description = "PENDIENTE, ENVIADO, ENTREGADO o CANCELADO")
            @RequestParam(required = false) String estado) {

        List<Map<String, Object>> pedidos = pedidoService.buscar(clienteId, categoria, fechaDesde, fechaHasta, estado);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", HttpStatus.OK.value());
        respuesta.put("messege", "Consulta realizada correctamente");
        respuesta.put("data", pedidos);
        return ResponseEntity.ok(respuesta);
    }
}