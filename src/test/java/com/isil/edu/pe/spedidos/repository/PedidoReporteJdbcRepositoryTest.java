package com.isil.edu.pe.spedidos.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.isil.edu.pe.spedidos.entity.Pedido;
import com.isil.edu.pe.spedidos.repository.projection.ClienteFrecuenteProjection;
import com.isil.edu.pe.spedidos.repository.projection.ProductoVendidoProjection;
import com.isil.edu.pe.spedidos.repository.projection.ResumenPedidosProjection;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PedidoReporteJdbcRepositoryTest {

    @Autowired
    private PedidoReporteJdbcRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {

        jdbcTemplate.update("DELETE FROM pedido");

        insertarPedido(
                "Ana Torres",
                "LAP-001",
                "Laptop",
                new BigDecimal("2500.00"),
                2,
                new BigDecimal("5000.00")
        );

        insertarPedido(
                "Carlos Perez",
                "MOU-001",
                "Mouse",
                new BigDecimal("50.00"),
                3,
                new BigDecimal("150.00")
        );

        insertarPedido(
                "Ana Torres",
                "MOU-001",
                "Mouse",
                new BigDecimal("50.00"),
                2,
                new BigDecimal("100.00")
        );
    }

    // ==========================================
    // PRUEBA ORIGINAL 1: RESUMEN GENERAL
    // ==========================================

    @Test
    @DisplayName("Return consolidated summary when orders exist")
    void returnConsolidatedSummaryFromPedidoReporteJdbcRepositoryWhenOrdersExist() {

        ResumenPedidosProjection resumen =
                repository.obtenerResumen();

        assertEquals(3L, resumen.totalPedidos());

        assertEquals(7L, resumen.totalUnidades());

        assertEquals(
                0,
                new BigDecimal("5250.00")
                        .compareTo(resumen.importeAcumulado())
        );
    }

    // ==========================================
    // PRUEBA ORIGINAL 2: BUSQUEDA POR CLIENTE
    // ==========================================

    @Test
    @DisplayName("Return matching orders by customer name")
    void returnMatchingOrdersFromPedidoReporteJdbcRepositoryWhenCustomerNameIsProvided() {

        String cliente = "Ana";

        List<Pedido> pedidos =
                repository.buscarPorCliente(cliente);

        assertEquals(2, pedidos.size());

        assertTrue(
                pedidos.stream().allMatch(
                        pedido -> "Ana Torres".equals(
                                pedido.getCliente()
                        )
                )
        );
    }

    // ==========================================
    // PRUEBA ORIGINAL 3: PRODUCTOS MAS VENDIDOS
    // ==========================================

    @Test
    @DisplayName("Return products ordered by sold units")
    void returnProductsOrderedBySoldUnitsFromPedidoReporteJdbcRepository() {

        List<ProductoVendidoProjection> productos =
                repository.listarProductosMasVendidos();

        assertEquals(2, productos.size());

        assertEquals(
                "MOU-001",
                productos.get(0).productoCodigo()
        );

        assertEquals(
                5L,
                productos.get(0).totalUnidades()
        );

        assertEquals(
                "LAP-001",
                productos.get(1).productoCodigo()
        );
    }

    // ==========================================
    // PA3 - PRUEBA NUEVA 1
    // BUSQUEDA AVANZADA CON SPRING JDBC
    // ==========================================

    @Test
    @DisplayName("PA3 - Filtrar pedidos por cliente y monto minimo")
    void buscarPedidosPorClienteYTotalMinimo_DebeFiltrarCorrectamente() {

        // Arrange
        String cliente = "ana";

        BigDecimal totalMinimo =
                new BigDecimal("200.00");

        // Act
        List<Pedido> pedidos =
                repository.buscarPedidosPorClienteYTotalMinimo(
                        cliente,
                        totalMinimo
                );

        // Assert
        assertEquals(1, pedidos.size());

        Pedido pedido = pedidos.get(0);

        assertEquals("Ana Torres", pedido.getCliente());

        assertEquals("LAP-001", pedido.getProductoCodigo());

        assertEquals(
                0,
                new BigDecimal("5000.00")
                        .compareTo(pedido.getTotal())
        );
    }

    // ==========================================
    // PA3 - PRUEBA NUEVA 2
    // CLIENTES FRECUENTES CON GROUP BY Y HAVING
    // ==========================================

    @Test
    @DisplayName("PA3 - Agrupar clientes frecuentes con minimo de pedidos")
    void listarClientesFrecuentes_DebeAgruparYFiltrarCorrectamente() {

        // Arrange
        int minimoPedidos = 2;

        // Act
        List<ClienteFrecuenteProjection> clientes =
                repository.listarClientesFrecuentes(
                        minimoPedidos
                );

        // Assert
        assertEquals(1, clientes.size());

        ClienteFrecuenteProjection cliente =
                clientes.get(0);

        assertEquals("Ana Torres", cliente.cliente());

        assertEquals(2L, cliente.totalPedidos());

        assertEquals(4L, cliente.totalUnidades());

        assertEquals(
                0,
                new BigDecimal("5100.00")
                        .compareTo(cliente.importeAcumulado())
        );

        assertEquals(
                0,
                new BigDecimal("2550.00")
                        .compareTo(cliente.ticketPromedio())
        );
    }

    // ==========================================
    // METODO AUXILIAR ORIGINAL
    // ==========================================

    private void insertarPedido(
            String cliente,
            String productoCodigo,
            String productoNombre,
            BigDecimal precioUnitario,
            int cantidad,
            BigDecimal total) {

        jdbcTemplate.update(
                """
                INSERT INTO pedido (
                    cliente,
                    producto_codigo,
                    producto_nombre,
                    precio_unitario,
                    cantidad,
                    total,
                    fecha
                ) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                """,
                cliente,
                productoCodigo,
                productoNombre,
                precioUnitario,
                cantidad,
                total
        );
    }
}
