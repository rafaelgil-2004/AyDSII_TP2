package com.example.TP2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TP2.models.Producto;
import com.example.TP2.service.CatalogoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController 
@RequestMapping("/api/catalogo")
@RequiredArgsConstructor
@Tag(name = "Catalogo", description = "Gestion de productos")
public class CatalogControler {
    private final CatalogoService catalogoService;

    @GetMapping
    @Operation(summary = "Lista todos los productos del catalogo")
    public ResponseEntity<Map<String, Object>> obtenerTodos() {
        return respuestaOk("Productos obtenidos con exito", catalogoService.obtenerTodos());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca productos por categoria y/o rango de precio")
    public ResponseEntity<Map<String, Object>> buscar(
            @Parameter(description = "Categoria a filtrar") @RequestParam(required = false) String categoria,
            @Parameter(description = "Precio minimo") @RequestParam(required = false) Double precioMin,
            @Parameter(description = "Precio maximo") @RequestParam(required = false) Double precioMax) {

        List<Producto> productos = catalogoService.buscar(categoria, precioMin, precioMax);
        return respuestaOk("Busqueda realizada con exito", productos);
    }

    @GetMapping("/ordenar")
    @Operation(summary = "Ordena los productos por precio o nombre")
    public ResponseEntity<Map<String, Object>> ordenar(
            @Parameter(description = "Criterio: precio o nombre", example = "precio") @RequestParam String criterio,
            @Parameter(description = "Orden: asc o desc", example = "desc")
            @RequestParam(required = false, defaultValue = "asc") String orden) {

        List<Producto> productos = catalogoService.ordenar(criterio, orden);
        return respuestaOk("Productos ordenados con exito", productos);
    }

    @PostMapping
    @Operation(summary = "Agrega un nuevo producto al catalogo")
    public ResponseEntity<Map<String, Object>> agregar(@Valid @RequestBody Producto producto) {
        Producto creado = catalogoService.agregar(producto);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", HttpStatus.CREATED.value());
        respuesta.put("message", "Producto agregado con exito");
        respuesta.put("data", creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "Modifica el stock de un producto")
    public ResponseEntity<Map<String, Object>> modificarStock(
            @PathVariable Long id,
            @Parameter(description = "Cantidad a sumar (negativa para restar)", example = "-5")
            @RequestParam int cantidad) {

        Producto producto = catalogoService.modificarStock(id, cantidad);
        return respuestaOk("Stock actualizado con exito", producto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un producto del catalogo")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        catalogoService.eliminar(id);
        return respuestaOk("Producto eliminado con exito", null);
    }

    private ResponseEntity<Map<String, Object>> respuestaOk(String mensaje, Object data) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", HttpStatus.OK.value());
        respuesta.put("message", mensaje);
        respuesta.put("data", data);
        return ResponseEntity.ok(respuesta);
    }
}
