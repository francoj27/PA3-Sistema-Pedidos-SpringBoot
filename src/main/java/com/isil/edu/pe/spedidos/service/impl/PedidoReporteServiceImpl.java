package com.isil.edu.pe.spedidos.service.impl;

import com.isil.edu.pe.spedidos.entity.Pedido;
import com.isil.edu.pe.spedidos.repository.PedidoReporteJdbcRepository;
import com.isil.edu.pe.spedidos.repository.projection.ClienteFrecuenteProjection;
import com.isil.edu.pe.spedidos.repository.projection.ResumenPedidosProjection;
import com.isil.edu.pe.spedidos.service.PedidoReporteService;
import com.isil.edu.pe.spedidos.service.domain.ProductoVendido;
import com.isil.edu.pe.spedidos.service.domain.ResumenPedidos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PedidoReporteServiceImpl implements PedidoReporteService {

    private final PedidoReporteJdbcRepository jdbcRepository;

    public PedidoReporteServiceImpl(
            PedidoReporteJdbcRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    // ==========================================
    // 1. RESUMEN GENERAL DE PEDIDOS
    // ==========================================

    @Override
    public ResumenPedidos obtenerResumen() {

        ResumenPedidosProjection resumenPedidos =
                jdbcRepository.obtenerResumen();

        return new ResumenPedidos(
                resumenPedidos.totalPedidos(),
                resumenPedidos.totalUnidades(),
                resumenPedidos.importeAcumulado(),
                resumenPedidos.ticketPromedio()
        );
    }

    // ==========================================
    // 2. BUSCAR PEDIDOS POR CLIENTE
    // ==========================================

    @Override
    public List<Pedido> buscarPorCliente(String cliente) {

        if (cliente == null || cliente.isBlank()) {
            return List.of();
        }

        return jdbcRepository.buscarPorCliente(cliente);
    }

    // ==========================================
    // 3. PRODUCTOS MAS VENDIDOS
    // ==========================================

    @Override
    public List<ProductoVendido> listarProductosMasVendidos() {

        return jdbcRepository.listarProductosMasVendidos()
                .stream()
                .map(item ->
                        new ProductoVendido(
                                item.productoCodigo(),
                                item.productoNombre(),
                                item.totalPedidos(),
                                item.totalUnidades(),
                                item.importeAcumulado()
                        )
                )
                .toList();
    }

    // ==========================================
    // 4. PA3: BUSQUEDA AVANZADA DE PEDIDOS
    // ==========================================

    @Override
    public List<Pedido> buscarPedidosPorClienteYTotalMinimo(
            String cliente,
            BigDecimal totalMinimo) {

        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del cliente es obligatorio."
            );
        }

        if (totalMinimo == null ||
                totalMinimo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El total minimo debe ser mayor o igual a cero."
            );
        }

        return jdbcRepository.buscarPedidosPorClienteYTotalMinimo(
                cliente.trim(),
                totalMinimo
        );
    }

    // ==========================================
    // 5. PA3: REPORTE DE CLIENTES FRECUENTES
    // ==========================================

    @Override
    public List<ClienteFrecuenteProjection> listarClientesFrecuentes(
            int minimoPedidos) {

        if (minimoPedidos < 1) {
            throw new IllegalArgumentException(
                    "La cantidad minima de pedidos debe ser mayor a cero."
            );
        }

        return jdbcRepository.listarClientesFrecuentes(
                minimoPedidos
        );
    }
}
