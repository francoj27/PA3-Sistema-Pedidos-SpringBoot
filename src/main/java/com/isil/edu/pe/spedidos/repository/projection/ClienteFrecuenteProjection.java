package com.isil.edu.pe.spedidos.repository.projection;

import java.math.BigDecimal;

/**
 * Proyección para el reporte de clientes frecuentes.
 * Contiene los indicadores consolidados por cliente.
 */
public record ClienteFrecuenteProjection(
        String cliente,
        Long totalPedidos,
        Long totalUnidades,
        BigDecimal importeAcumulado,
        BigDecimal ticketPromedio
) {
}
