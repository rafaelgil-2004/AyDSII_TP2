package com.example.TP2.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.example.TP2.entity.HistorialConversion;
import com.example.TP2.exception.DatosInvalidosException;
import com.example.TP2.exception.ServicioExternoException;
import com.example.TP2.repository.HistorialConversionRepository;

@Service 
public class DivisaService {

    private static final Pattern CODIGO_MONEDA = Pattern.compile("^[A-Za-z]{3}$");

    private final RestClient restClient;

    private final HistorialConversionRepository historialRepository;

    public DivisaService(@Qualifier("frankfurterRestClient") RestClient restClient,
                      HistorialConversionRepository historialRepository) {
        this.restClient = restClient;
        this.historialRepository = historialRepository;
    }


    @SuppressWarnings("unchecked")
    public Map<String, Object> convertir(double monto, String origen, String destino) {
        validarDatos(monto, origen, destino);

        String origenNorm = origen.toUpperCase();
        String destinoNorm = destino.toUpperCase();

        Map<String, Object> respuestaExterna;
        try {
            respuestaExterna = restClient.get()
                    .uri("/latest?amount={monto}&from={origen}&to={destino}", monto, origenNorm, destinoNorm)
                    .retrieve() 
                    .body(Map.class); 
        } catch (HttpClientErrorException ex) {
            throw new DatosInvalidosException("La moneda solicitada no es valida o no existe: " + ex.getStatusText());
        } catch (HttpServerErrorException | ResourceAccessException ex) {
            throw new ServicioExternoException("No se pudo obtener la cotizacion del servicio externo de divisas");
        } catch (Exception ex) {
            throw new ServicioExternoException("Error inesperado al consultar el servicio de divisas");
        }

        Map<String, Object> rates = respuestaExterna == null ? null : (Map<String, Object>) respuestaExterna.get("rates");
        if (rates == null || !rates.containsKey(destinoNorm)) {
            throw new ServicioExternoException("El servicio de divisas no devolvio una tasa de cambio valida");
        }
        double montoConvertido = ((Number) rates.get(destinoNorm)).doubleValue();
        double tasaCambio = montoConvertido / monto;

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("montoOriginal", monto);
        resultado.put("monedaOrigen", origenNorm);
        resultado.put("monedaDestino", destinoNorm);
        resultado.put("tasaCambio", tasaCambio);
        resultado.put("montoConvertido", montoConvertido);
        resultado.put("fecha", respuestaExterna.get("date"));
        return resultado;
    }

    public Map<String, Object> consultarYGuardar(double monto, String origen, String destino) {
        Map<String, Object> resultado = convertir(monto, origen, destino);

        HistorialConversion historial = new HistorialConversion();
        historial.setMonedaOrigen((String) resultado.get("monedaOrigen"));
        historial.setMonedaDestino((String) resultado.get("monedaDestino"));
        historial.setMonto(BigDecimal.valueOf((double) resultado.get("montoOriginal")));
        historial.setMontoConvertido(BigDecimal.valueOf((double) resultado.get("montoConvertido")));
        historial.setTasa(BigDecimal.valueOf((double) resultado.get("tasaCambio")));
        historial.setFechaConsulta(LocalDateTime.now());

        historialRepository.save(historial);
        return resultado; 
    }

    public List<Map<String, Object>> obtenerHistorial(String origen, String destino) {
        List<HistorialConversion> historial = historialRepository
                .buscarHistorial(origen.toUpperCase(), destino.toUpperCase());

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (HistorialConversion h : historial) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("fecha", h.getFechaConsulta());
            item.put("tasaCambio", h.getTasa());
            resultado.add(item);
        }
        return resultado;
    }

    private void validarDatos(double monto, String origen, String destino) {
        if (monto <= 0) {
            throw new DatosInvalidosException("El monto debe ser mayor que 0");
        }
        if (origen == null || !CODIGO_MONEDA.matcher(origen).matches()) {
            throw new DatosInvalidosException("El codigo de moneda de origen debe tener 3 letras");
        }
        if (destino == null || !CODIGO_MONEDA.matcher(destino).matches()) {
            throw new DatosInvalidosException("El codigo de moneda de destino debe tener 3 letras");
        }
    }
}


