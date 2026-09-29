package com.example.TP2.service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.TP2.exception.DatosInvalidosException;
import com.example.TP2.models.VentaDTO;


@Service 
public class VentaService {
    public Map<String, Object> calcularEstadisticas(List<VentaDTO> ventas){
        listaNoVacia(ventas);

        double totalFacturado = ventas.stream().mapToDouble(VentaDTO::getImporte).sum();
        int cantidadVentas = ventas.size();
        double ticketPromedio = totalFacturado/cantidadVentas;
        VentaDTO ventaMayor = ventas.stream().max(Comparator.comparingDouble(VentaDTO::getImporte)).orElseThrow();

        VentaDTO ventaMenor = ventas.stream().min(Comparator.comparingDouble(VentaDTO::getImporte)).orElseThrow();
        Map<String, Integer> cantidadPorProducto = ventas.stream().collect(Collectors.groupingBy(VentaDTO::getProducto, Collectors.summingInt(VentaDTO::getCantidad)));

        String productoMasVendido = cantidadPorProducto.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    
        Map<String, Object> estadisticas = new LinkedHashMap<>();
        estadisticas.put("totalFacturado", totalFacturado);
        estadisticas.put("cantidadVentas", cantidadVentas);
        estadisticas.put("ticketPromedio", ticketPromedio);
        estadisticas.put("ventaMayor", ventaMayor);
        estadisticas.put("ventaMenor", ventaMenor);
        estadisticas.put("productoMasVendido", productoMasVendido);
        return estadisticas;
    }

    public Map<String, Object> aplicarDescuento(List<VentaDTO> ventas, double porcentaje){
        listaNoVacia(ventas);

        if (porcentaje < 0 || porcentaje > 100) {
            throw new DatosInvalidosException("El porcentaje de descuento debe ser un valor entre 0 y 100");
        }

        List<Map<String, Object>> ventasConDescuento = ventas.stream()
                .map(v -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("producto", v.getProducto());
                    item.put("cantidad", v.getCantidad());
                    item.put("precioUnitario", v.getPrecioUnitario());
                    item.put("montoConDescuento", v.getImporte() * (1 - porcentaje / 100.0));
                    return item;
                })
                .collect(Collectors.toList());
        
        double totalConDescuento = ventasConDescuento.stream()
                .mapToDouble(item -> (double) item.get("montoConDescuento"))
                .sum();
        
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("ventas", ventasConDescuento);
        resultado.put("totalConDescuento", totalConDescuento);
        return resultado;
    }

    private void listaNoVacia(List<VentaDTO> ventas) {
        if (ventas == null || ventas.isEmpty()) {
            throw new DatosInvalidosException("La lista de ventas no puede estar vacia");
        }
    }
}


