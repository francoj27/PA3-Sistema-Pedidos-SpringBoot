package com.isil.edu.pe.spedidos.controller;

import com.isil.edu.pe.spedidos.service.PedidoReporteService;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PedidoReporteWebController {

    private final PedidoReporteService reporteService;

    public PedidoReporteWebController(
            PedidoReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/reportes/pedidos")
    public String mostrarReportes(
            @RequestParam(required = false) String cliente,
            @RequestParam(required = false) String totalMinimo,
            @RequestParam(required = false) String minPedidos,
            Model model) {

        model.addAttribute("clienteIngresado",
                cliente == null ? "" : cliente);

        model.addAttribute("totalMinimoIngresado",
                totalMinimo == null ? "" : totalMinimo);

        model.addAttribute("minPedidosIngresado",
                minPedidos == null ? "1" : minPedidos);

        model.addAttribute("pedidosFiltrados", List.of());
        model.addAttribute("clientesFrecuentes", List.of());

        // Resumen general
        model.addAttribute(
                "resumen",
                reporteService.obtenerResumen()
        );

        // Busqueda avanzada
        if (cliente != null && !cliente.isBlank()) {

            try {
                BigDecimal monto = new BigDecimal(
                        totalMinimo == null || totalMinimo.isBlank()
                                ? "0"
                                : totalMinimo
                );

                model.addAttribute(
                        "pedidosFiltrados",
                        reporteService
                                .buscarPedidosPorClienteYTotalMinimo(
                                        cliente,
                                        monto
                                )
                );

                model.addAttribute("busquedaRealizada", true);

            } catch (IllegalArgumentException ex) {
                model.addAttribute(
                        "errorBusqueda",
                        ex.getMessage()
                );
            }
        }

        // Reporte de clientes frecuentes
        try {
            int minimo = Integer.parseInt(
                    minPedidos == null || minPedidos.isBlank()
                            ? "1"
                            : minPedidos
            );

            model.addAttribute(
                    "clientesFrecuentes",
                    reporteService.listarClientesFrecuentes(minimo)
            );

        } catch (IllegalArgumentException ex) {
            model.addAttribute(
                    "errorClientes",
                    ex.getMessage()
            );
        }

        return "reportes-pedidos";
    }
}
