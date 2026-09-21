package com.example.TP2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestControllerAdvice 
public class GlobalExceptionHandler {

    private static final Pattern INDEX_PATTERN = Pattern.compile("\\[(\\d+)]");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    
        // true: validacion de lista(e1)
        // false: validacion de un unico elemento
        boolean esListaConIndice = fieldErrors.stream().anyMatch(fe -> INDEX_PATTERN.matcher(fe.getField()).find());
    
        Object data;
        String mensaje;
    
        if (esListaConIndice) {
            List<Object> errores = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                String field = fieldError.getField();
                Matcher matcher = INDEX_PATTERN.matcher(field);
                String campo = field.contains(".") ? field.substring(field.lastIndexOf('.') + 1) : field;
            
                Map<String, Object> detalle = new LinkedHashMap<>();
                if (matcher.find()) {
                    detalle.put("posicion", Integer.parseInt(matcher.group(1)));
                }
                detalle.put("campo", campo);
                detalle.put("motivo", fieldError.getDefaultMessage());
                errores.add(detalle);
            }
            data = errores;
            mensaje = "Uno o mas elementos no cumplen con las validaciones requeridas";
        } else {
            Map<String, String> errores = new LinkedHashMap<>();
            for (FieldError fieldError : fieldErrors) {
                errores.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            data = errores;
            mensaje = "Error de validacion";
        }
    
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("message", mensaje);
        respuesta.put("data", data);
        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(DatosInvalidosException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RecursoNoEncontradoException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ServicioExternoException.class)
    public ResponseEntity<Map<String, Object>> handleExternalService(ServicioExternoException ex) {
        return construirRespuesta(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error interno inesperado");
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(HttpStatus status, String mensaje) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", status.value());
        respuesta.put("message", mensaje);
        respuesta.put("data", null);
        return ResponseEntity.status(status).body(respuesta);
    }
}
