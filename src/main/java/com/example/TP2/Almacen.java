package com.example.TP2;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import com.example.TP2.models.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class Almacen {

    private final Map<Long, Producto> productos = new HashMap<>();
    private final AtomicLong secuenciaId = new AtomicLong(1);

    @PostConstruct
    public void cargarDatosIniciales() {
        guardar(new Producto(null, "Mouse inalambrico", "Perifericos", 8500.0, 25));
        guardar(new Producto(null, "Teclado mecanico", "Perifericos", 32000.0, 15));
        guardar(new Producto(null, "Monitor 24 pulgadas", "Monitores", 145000.0, 8));
        guardar(new Producto(null, "Notebook 15 pulgadas", "Notebooks", 950000.0, 5));
        guardar(new Producto(null, "Auriculares bluetooth", "Audio", 21000.0, 20));
        guardar(new Producto(null, "Webcam HD", "Perifericos", 18000.0, 12));
        guardar(new Producto(null, "Disco SSD 480GB", "Almacenamiento", 35000.0, 18));
        guardar(new Producto(null, "Memoria RAM 8GB", "Componentes", 22000.0, 30));
    }

    public List<Producto> obtenerTodos() {
        return new ArrayList<>(productos.values());
    }

    public Producto guardar(Producto producto) {
        long id = secuenciaId.getAndIncrement();
        producto.setId(id);
        productos.put(id, producto);
        return producto;
    }

    public void eliminar(Long id) {
        productos.remove(id);
    }

    public Producto buscarPorId(Long id) {
        return productos.get(id);
    }
}
