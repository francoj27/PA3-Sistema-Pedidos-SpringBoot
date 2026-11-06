package com.isil.edu.pe.spedidos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.isil.edu.pe.spedidos.controller.dto.PedidoResponse;
import com.isil.edu.pe.spedidos.controller.dto.ProductoVendidoDto;
import com.isil.edu.pe.spedidos.controller.dto.ResumenPedidosDto;
import com.isil.edu.pe.spedidos.repository.projection.ClienteFrecuenteProjection;
import com.isil.edu.pe.spedidos.service.PedidoReporteService;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para consultas y reportes de pedidos.
 *
 * Conserva los endpoints originales del proyecto
 * e incorpora las consultas avanzadas de la PA3.
 */
@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoReporteController {

    private final PedidoReporteService reporteService;

    public PedidoReporteController(
            PedidoReporteService reporteService) {

        this.reporteService = reporteService;
    }

    // ==========================================
    // 1. ORIGINAL: BUSCAR PEDIDOS POR CLIENTE
    // ==========================================

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PedidoResponse> listarPedidosPorCliente(
            @RequestParam String cliente) {

        return reporteService.buscarPorCliente(cliente)
                .stream()
                .map(PedidoResponse::from)
                .toList();
    }

    // ==========================================
    // 2. ORIGINAL: RESUMEN GENERAL DE PEDIDOS
    // ==========================================

    @GetMapping(
            value = "/estadisticas/resumen",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResumenPedidosDto> obtenerResumen() {

        return ResponseEntity.ok(
                ResumenPedidosDto.from(
                        reporteService.obtenerResumen()
                )
        );
    }

    // ==========================================
    // 3. ORIGINAL: PRODUCTOS MAS VENDIDOS (HAL)
    // ==========================================

    @GetMapping(
            value = "/estadisticas/productos",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public ResponseEntity<CollectionModel<ProductoVendidoDto>>
    listarProductosMasVendidos() {

        List<ProductoVendidoDto> productos =
                reporteService.listarProductosMasVendidos()
                        .stream()
                        .map(ProductoVendidoDto::from)
                        .toList();

        CollectionModel<ProductoVendidoDto> coleccion =
                CollectionModel.of(
                        productos,

                        linkTo(PedidoReporteController.class)
                                .slash("estadisticas/productos")
                                .withSelfRel(),

                        linkTo(PedidoReporteController.class)
                                .slash("estadisticas/resumen")
                                .withRel("estadisticas-resumen"),

                        linkPedidos().withRel("pedidos")
                );

        return ResponseEntity.ok(coleccion);
    }

    // ==========================================
    // 4. PA3: BUSQUEDA AVANZADA DE PEDIDOS
    // ==========================================

    @GetMapping(
            value = "/busqueda-avanzada",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<PedidoResponse>>
    buscarPedidosPorClienteYTotalMinimo(
            @RequestParam String cliente,
            @RequestParam BigDecimal totalMinimo) {

        List<PedidoResponse> pedidos =
                reporteService
                        .buscarPedidosPorClienteYTotalMinimo(
                                cliente,
                                totalMinimo
                        )
                        .stream()
                        .map(PedidoResponse::from)
                        .toList();

        return ResponseEntity.ok(pedidos);
    }

    // ==========================================
    // 5. PA3: REPORTE DE CLIENTES FRECUENTES
    // ==========================================
    // Ruta requerida:
    // GET /api/v1/pedidos/clientes-frecuentes?minPedidos=2

    @GetMapping(
            value = "/clientes-frecuentes",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ClienteFrecuenteProjection>>
    listarClientesFrecuentes(
            @RequestParam(
                    name = "minPedidos",
                    defaultValue = "1"
            )
            int minimoPedidos) {

        return ResponseEntity.ok(
                reporteService.listarClientesFrecuentes(
                        minimoPedidos
                )
        );
    }

    // ==========================================
    // 6. ENLACE ORIGINAL HATEOAS
    // ==========================================

    private Link linkPedidos() {

        return Link.of(
                linkTo(PedidoReporteController.class)
                        .toUri() + "{?cliente}"
        );
    }
}
