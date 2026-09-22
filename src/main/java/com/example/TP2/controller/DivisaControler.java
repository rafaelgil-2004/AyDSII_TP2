package com.example.TP2.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.TP2.service.DivisaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/divisas")
@RequiredArgsConstructor
@Tag(name = "Divisas", description = "Conversion de divisas usando la API externa Frankfurter ")
public class DivisaControler {

    private final DivisaService divisaService;

    @GetMapping("/convertir")
    @Operation(summary = "Convierte un monto entre dos monedas")
    public ResponseEntity<Map<String, Object>> convertir(
        @Parameter(description = "Monto a convertir", example = "100")
        @RequestParam("monto") double monto,
        @Parameter(description = "Codigo ISO de 3 letras de la moneda de origen", example = "USD")
        @RequestParam("origen") String origen,
        @Parameter(description = "Codigo ISO de 3 letras de la moneda de destino", example = "ARS")
        @RequestParam("destino") String destino) {

        Map<String, Object> resultado = divisaService.convertir(monto, origen, destino);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", HttpStatus.OK.value());
        respuesta.put("message", "Conversion realizada con exito");
        respuesta.put("data", resultado);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/consultar")
    @Operation(summary = "Consulta la cotizacion actual y la guarda en el historial")
    public ResponseEntity<Map<String, Object>> consultar(
            @RequestParam("monto") double monto,
            @RequestParam("origen") String origen,
            @RequestParam("destino") String destino) {
            
        Map<String, Object> resultado = divisaService.consultarYGuardar(monto, origen, destino);
            
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", HttpStatus.OK.value());
        respuesta.put("messege", "Consulta guardada con exito");
        respuesta.put("data", resultado);
        return ResponseEntity.ok(respuesta);
    }
    
    @GetMapping("/historial")
    @Operation(summary = "Devuelve el historial de conversiones para un par de monedas")
    public ResponseEntity<Map<String, Object>> historial(
            @RequestParam("origen") String origen,
            @RequestParam("destino") String destino) {
            
        List<Map<String, Object>> resultado = divisaService.obtenerHistorial(origen, destino);
            
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", HttpStatus.OK.value());
        respuesta.put("messege", "Historial obtenido con exito");
        respuesta.put("data", resultado);
        return ResponseEntity.ok(respuesta);
    }
}
