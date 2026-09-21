package com.example.TP2.service;

import com.example.TP2.entity.DetallePedido;
import com.example.TP2.entity.Pedido;
import com.example.TP2.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    //para que hivernate no cierre la conexion
    @Transactional(readOnly = true)
    public List<Map<String, Object>> buscar(Integer clienteId, String categoria, LocalDate fechaDesde, LocalDate fechaHasta, String estado) {
        List<Pedido> pedidos = pedidoRepository.buscarConFiltros(clienteId, categoria, fechaDesde, fechaHasta, estado);

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            resultado.add(mapearPedido(pedido));
        }
        return resultado;
    }

    private Map<String, Object> mapearPedido(Pedido pedido) {
        List<Map<String, Object>> productos = new ArrayList<>();
        BigDecimal totalPedido = BigDecimal.ZERO;

        for (DetallePedido detalle : pedido.getDetalles()) {
            BigDecimal subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
            totalPedido = totalPedido.add(subtotal);

            Map<String, Object> productoMap = new LinkedHashMap<>();
            productoMap.put("nombre", detalle.getProducto().getNombre());
            productoMap.put("categoria", detalle.getProducto().getCategoria() != null ? detalle.getProducto().getCategoria().getNombre() : null);
            productoMap.put("cantidad", detalle.getCantidad());
            productoMap.put("subtotal", subtotal);
            productos.add(productoMap);
        }

        Map<String, Object> pedidoMap = new LinkedHashMap<>();
        pedidoMap.put("pedidoId", pedido.getId());
        pedidoMap.put("cliente", pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido());
        pedidoMap.put("fecha", pedido.getFechaPedido());
        pedidoMap.put("estado", pedido.getEstado());
        pedidoMap.put("totalPedido", totalPedido);
        pedidoMap.put("productos", productos);
        return pedidoMap;
    }
}