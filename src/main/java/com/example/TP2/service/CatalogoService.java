package com.example.TP2.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.TP2.Almacen;
import com.example.TP2.exception.DatosInvalidosException;
import com.example.TP2.exception.RecursoNoEncontradoException;
import com.example.TP2.models.Producto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class CatalogoService {
    private final Almacen almacen;

    public List<Producto> obtenerTodos(){
        return almacen.obtenerTodos();
    }

    public List<Producto> buscar(String categoria, Double precioMin, Double precioMax) {
        return almacen.obtenerTodos().stream()
                .filter(p -> categoria == null || p.getCategoria().equalsIgnoreCase(categoria))
                .filter(p -> precioMin == null || p.getPrecio() >= precioMin)
                .filter(p -> precioMax == null || p.getPrecio() <= precioMax)
                .toList();
    }

    public List<Producto> ordenar(String criterio, String orden) {
        Comparator<Producto> comparator = switch (criterio) {
            case "precio" -> Comparator.comparingDouble(Producto::getPrecio);
            case "nombre" -> Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER);
            default -> throw new DatosInvalidosException("El criterio debe ser 'precio' o 'nombre'");
        };

        if ("desc".equalsIgnoreCase(orden)) {
            comparator = comparator.reversed();
        } else if (orden != null && !"asc".equalsIgnoreCase(orden)) {
            throw new DatosInvalidosException("El orden debe ser 'asc' o 'desc'");
        }

        return almacen.obtenerTodos().stream().sorted(comparator).toList();
    }

    public Producto agregar(Producto producto) {
        // El id lo asigna siempre el Almacen (ver Almacen.guardar), nunca el cliente.
        return almacen.guardar(producto);
    }

    public Producto modificarStock(Long id, int cantidad) {
        Producto producto = buscarPorId(id);
        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new DatosInvalidosException("El stock no puede quedar por debajo de 0");
        }
        producto.setStock(nuevoStock);
        return producto;
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        almacen.eliminar(id);
    }

    private Producto buscarPorId(Long id) {
        Producto producto = almacen.buscarPorId(id);
        if (producto == null) {
            throw new RecursoNoEncontradoException("No existe un producto con id " + id);
        }
        return producto;
    }
}
