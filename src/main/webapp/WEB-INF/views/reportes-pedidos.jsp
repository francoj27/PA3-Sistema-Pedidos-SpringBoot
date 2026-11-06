<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Reportes de Pedidos - PA3 ISIL</title>

    <c:url var="cssUrl" value="/assets/css/app.css"/>
    <link rel="stylesheet" href="${cssUrl}">
</head>

<body>
<div class="contenedor">

    <header class="cabecera">
        <div>
            <p class="eyebrow">
                PA3 · Spring Boot · Spring MVC · Spring JDBC · MySQL
            </p>

            <h1>Reportes de Pedidos</h1>

            <p class="subtitulo">
                Consultas avanzadas y estadísticas de ventas
                mediante Spring JDBC.
            </p>
        </div>

        <c:url var="pedidosUrl" value="/pedidos"/>
        <a href="${pedidosUrl}">Volver a pedidos</a>
    </header>

    <!-- RESUMEN GENERAL -->

    <section class="tarjeta">

        <h2>Resumen general de pedidos</h2>

        <div class="tabla-scroll">
            <table>
                <thead>
                <tr>
                    <th>Total pedidos</th>
                    <th>Total unidades</th>
                    <th>Importe acumulado</th>
                    <th>Ticket promedio</th>
                </tr>
                </thead>

                <tbody>
                <tr>
                    <td>
                        <c:out value="${resumen.totalPedidos}"/>
                    </td>

                    <td>
                        <c:out value="${resumen.totalUnidades}"/>
                    </td>

                    <td>
                        S/
                        <fmt:formatNumber
                                value="${resumen.importeAcumulado}"
                                minFractionDigits="2"
                                maxFractionDigits="2"/>
                    </td>

                    <td>
                        S/
                        <fmt:formatNumber
                                value="${resumen.ticketPromedio}"
                                minFractionDigits="2"
                                maxFractionDigits="2"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

    </section>

    <!-- BUSQUEDA AVANZADA -->

    <section class="tarjeta">

        <h2>Búsqueda avanzada de pedidos</h2>

        <p>
            Filtra los pedidos por nombre del cliente
            y monto mínimo de compra.
        </p>

        <c:url var="reportesUrl" value="/reportes/pedidos"/>

        <form method="get"
              action="${reportesUrl}"
              class="form-grid">

            <label>
                Cliente

                <input
                        type="text"
                        name="cliente"
                        required
                        maxlength="120"
                        placeholder="Ej. Juan"
                        value="${fn:escapeXml(clienteIngresado)}">
            </label>

            <label>
                Total mínimo (S/)

                <input
                        type="number"
                        name="totalMinimo"
                        min="0"
                        step="0.01"
                        value="${fn:escapeXml(totalMinimoIngresado)}"
                        placeholder="Ej. 100">
            </label>

            <button type="submit">
                Buscar pedidos
            </button>

        </form>

        <c:if test="${not empty errorBusqueda}">
            <div class="alerta error">
                <c:out value="${errorBusqueda}"/>
            </div>
        </c:if>

        <c:if test="${busquedaRealizada}">

            <h3>Resultados de la búsqueda</h3>

            <div class="tabla-scroll">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Cliente</th>
                        <th>Código</th>
                        <th>Producto</th>
                        <th>Cantidad</th>
                        <th>Total</th>
                        <th>Fecha</th>
                    </tr>
                    </thead>

                    <tbody>

                    <c:forEach
                            var="pedido"
                            items="${pedidosFiltrados}">

                        <tr>
                            <td>
                                <c:out value="${pedido.id}"/>
                            </td>

                            <td>
                                <c:out value="${pedido.cliente}"/>
                            </td>

                            <td>
                                <c:out value="${pedido.productoCodigo}"/>
                            </td>

                            <td>
                                <c:out value="${pedido.productoNombre}"/>
                            </td>

                            <td>
                                <c:out value="${pedido.cantidad}"/>
                            </td>

                            <td>
                                S/
                                <fmt:formatNumber
                                        value="${pedido.total}"
                                        minFractionDigits="2"
                                        maxFractionDigits="2"/>
                            </td>

                            <td>
                                <c:out value="${pedido.fecha}"/>
                            </td>
                        </tr>

                    </c:forEach>

                    <c:if test="${empty pedidosFiltrados}">
                        <tr>
                            <td colspan="7" class="sin-datos">
                                No se encontraron pedidos
                                con los filtros ingresados.
                            </td>
                        </tr>
                    </c:if>

                    </tbody>
                </table>
            </div>

        </c:if>

    </section>

    <!-- CLIENTES FRECUENTES -->

    <section class="tarjeta">

        <h2>Reporte de clientes frecuentes</h2>

        <p>
            Estadísticas agrupadas por cliente mediante
            GROUP BY y HAVING en Spring JDBC.
        </p>

        <form method="get"
              action="${reportesUrl}"
              class="form-grid">

            <label>
                Cantidad mínima de pedidos

                <input
                        type="number"
                        name="minPedidos"
                        min="1"
                        required
                        value="${fn:escapeXml(minPedidosIngresado)}">
            </label>

            <button type="submit">
                Consultar clientes
            </button>

        </form>

        <c:if test="${not empty errorClientes}">
            <div class="alerta error">
                <c:out value="${errorClientes}"/>
            </div>
        </c:if>

        <div class="tabla-scroll">
            <table>
                <thead>
                <tr>
                    <th>Cliente</th>
                    <th>Total pedidos</th>
                    <th>Total unidades</th>
                    <th>Importe acumulado</th>
                    <th>Ticket promedio</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach
                        var="cliente"
                        items="${clientesFrecuentes}">

                    <tr>
                        <td>
                            <c:out value="${cliente.cliente}"/>
                        </td>

                        <td>
                            <c:out value="${cliente.totalPedidos}"/>
                        </td>

                        <td>
                            <c:out value="${cliente.totalUnidades}"/>
                        </td>

                        <td>
                            S/
                            <fmt:formatNumber
                                    value="${cliente.importeAcumulado}"
                                    minFractionDigits="2"
                                    maxFractionDigits="2"/>
                        </td>

                        <td>
                            S/
                            <fmt:formatNumber
                                    value="${cliente.ticketPromedio}"
                                    minFractionDigits="2"
                                    maxFractionDigits="2"/>
                        </td>
                    </tr>

                </c:forEach>

                <c:if test="${empty clientesFrecuentes}">
                    <tr>
                        <td colspan="5" class="sin-datos">
                            No se encontraron clientes
                            con la cantidad mínima indicada.
                        </td>
                    </tr>
                </c:if>

                </tbody>
            </table>
        </div>

    </section>

</div>
</body>
</html>
