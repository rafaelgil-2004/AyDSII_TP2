package com.example.TP2.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.TP2.models.Venta;
import com.example.TP2.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Procesamiento de ventas (Ej 1)")
public class VentasControler {
    private final VentaService ventaService;

    public VentasControler (VentaService vS){
        ventaService = vS;
    }

    @PostMapping("/estadisticas")
    @Operation(summary = "Calcula estadisticas de un lote de ventas")
    public ResponseEntity<Map<String, Object>> calcularEstadisticas(@Valid @RequestBody  List<Venta> ventas) {
        Map<String, Object> estadisticas = ventaService.calcularEstadisticas(ventas);
        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("status", HttpStatus.OK.value());
        respuesta.put("message", "Estadisticas calculadas con exito");
        respuesta.put("data", estadisticas);
        return ResponseEntity.ok(respuesta); 
    }

    @PostMapping("/aplicar-descuento")
    @Operation(summary = "Aplica un descuento a un lote de ventas")
    public ResponseEntity<Map<String, Object>> aplicarDescuento(@Valid @RequestBody List<Venta> ventas,@Parameter(description = "Porcentaje de descuento a aplicar (entre 0 y 100)", example = "10")@RequestParam("porcentaje") double porcentaje) {
        Map<String, Object> resultado = ventaService.aplicarDescuento(ventas, porcentaje);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", HttpStatus.OK.value());
        respuesta.put("message", "Descuento aplicado con exito");
        respuesta.put("data", resultado);
        return ResponseEntity.ok(respuesta);
    }
}
