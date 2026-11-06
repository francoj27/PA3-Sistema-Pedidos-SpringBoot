package com.isil.edu.pe.spedidos.exception;

import com.isil.edu.pe.spedidos.controller.PedidoReporteController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Maneja los errores de validacion de los reportes REST de la PA3.
 *
 * Convierte parametros invalidos en respuestas HTTP 400
 * sin modificar los controladores originales de pedidos.
 */
@RestControllerAdvice(assignableTypes = PedidoReporteController.class)
public class PedidoReporteExceptionHandler {

    // ==========================================
    // 1. VALIDACIONES DE NEGOCIO
    // ==========================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>>
    manejarArgumentoInvalido(IllegalArgumentException ex) {

        return crearRespuestaError(ex.getMessage());
    }

    // ==========================================
    // 2. PARAMETROS OBLIGATORIOS AUSENTES
    // ==========================================

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>>
    manejarParametroFaltante(
            MissingServletRequestParameterException ex) {

        return crearRespuestaError(
                "El parametro '" + ex.getParameterName()
                        + "' es obligatorio."
        );
    }

    // ==========================================
    // 3. TIPOS DE DATOS INVALIDOS
    // ==========================================

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>>
    manejarTipoInvalido(
            MethodArgumentTypeMismatchException ex) {

        return crearRespuestaError(
                "El parametro '" + ex.getName()
                        + "' contiene un valor invalido."
        );
    }

    // ==========================================
    // 4. RESPUESTA JSON HTTP 400
    // ==========================================

    private ResponseEntity<Map<String, Object>>
    crearRespuestaError(String mensaje) {

        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Bad Request");
        respuesta.put("mensaje", mensaje);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }
}
