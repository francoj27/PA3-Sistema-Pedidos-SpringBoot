# Sistema de Pedidos — Spring Boot | PA3

**Proyecto académico — Desarrollo de Aplicaciones Empresariales Avanzado — ISIL**

Aplicación empresarial desarrollada con Java 21, Spring Boot 4.1.1, Spring MVC, Spring Data JPA, Spring JDBC, MySQL y Spring Security.

El proyecto integra un servicio externo de inventario implementado en Jakarta EE y desplegado en WildFly.

## 1. Descripción del proyecto

El Sistema de Pedidos permite registrar pedidos, consultar productos disponibles y generar reportes estadísticos de ventas.

La aplicación utiliza una arquitectura por capas que separa las responsabilidades de presentación, controladores, servicios, repositorios y acceso a datos.

La Práctica Académica 3 (PA3) amplía las funcionalidades existentes mediante consultas avanzadas con Spring JDBC, reportes de clientes frecuentes, una interfaz web de reportes y pruebas automatizadas.

El sistema mantiene las operaciones existentes implementadas con Spring Data JPA y conserva la comunicación HTTP con el servicio externo de inventario.

## 2. Objetivos

### Objetivo general

Desarrollar y ampliar una aplicación empresarial utilizando Spring Boot, Spring MVC, Spring JDBC y MySQL, aplicando buenas prácticas de arquitectura, seguridad y pruebas automatizadas.

### Objetivos específicos

- Implementar persistencia de pedidos mediante Spring Data JPA y MySQL.
- Utilizar Spring JDBC para consultas avanzadas y reportes.
- Aplicar consultas SQL parametrizadas.
- Generar estadísticas mediante funciones de agregación.
- Implementar reportes de clientes frecuentes con GROUP BY y HAVING.
- Exponer información mediante endpoints REST.
- Desarrollar una interfaz web utilizando JSP.
- Implementar autenticación y autorización mediante Spring Security.
- Incorporar pruebas automatizadas con JUnit 5 y MockMvc.
- Mantener la integración con un servicio externo Jakarta EE.

## 3. Tecnologías utilizadas

| Tecnología | Aplicación |
|------------|------------|
| Java 21 | Lenguaje de programación |
| Spring Boot 4.1.1 | Framework principal |
| Spring MVC | Controladores e interfaz web |
| Spring Data JPA | Persistencia de pedidos |
| Spring JDBC | Consultas SQL y reportes |
| JdbcTemplate | Consultas y agregaciones |
| NamedParameterJdbcTemplate | Consultas parametrizadas |
| MySQL 8 | Base de datos principal |
| H2 | Base de datos para pruebas |
| JSP y JSTL | Vistas web |
| Spring Security | Autenticación y autorización |
| Spring HATEOAS | Representaciones HAL |
| JUnit 5 | Pruebas automatizadas |
| Mockito | Simulación de dependencias |
| MockMvc | Pruebas de controladores |
| Maven | Gestión de dependencias |
| WildFly | Servidor del servicio de inventario |
| Jakarta EE | Implementación del inventario externo |

## 4. Arquitectura del sistema

La aplicación sigue una arquitectura por capas.

```text
             USUARIO / NAVEGADOR
                     |
                     v
              SPRING SECURITY
                     |
                     v
               SPRING MVC
                     |
          +----------+-----------+
          |                      |
          v                      v
  PedidoController     PedidoReporteWebController
  PedidoApiController  PedidoReporteController
          |                      |
          v                      v
     PedidoService       PedidoReporteService
          |                      |
          v                      v
   PedidoServiceImpl    PedidoReporteServiceImpl
          |                      |
          |                      v
          |             PedidoReporteJdbcRepository
          |                      |
          |              +-------+-------+
          |              |               |
          |              v               v
          |         JdbcTemplate    NamedParameter
          |                         JdbcTemplate
          |              |               |
          |              +-------+-------+
          |                      |
          v                      v
    PedidoRepository          MySQL
    Spring Data JPA          pedidosdb
          |
          v
        MySQL
       pedidosdb

   Integración externa:

   PedidoServiceImpl
          |
          v
     InventarioClient
          |
          v
    InventarioRestClient
          |
          v
       HTTP / JSON
          |
          v
    inventario-service
     Jakarta EE / WildFly
          |
          v
       SQL Server
```

### Responsabilidades

**Controller:** recibe las solicitudes HTTP y determina la respuesta.

**Service:** implementa las reglas de negocio y validaciones.

**Repository:** encapsula las operaciones de acceso a datos.

**Spring Data JPA:** administra la persistencia de pedidos.

**Spring JDBC:** ejecuta consultas SQL para búsquedas y estadísticas.

**Spring Security:** controla la autenticación, autorización y protección CSRF.

**InventarioClient:** permite la comunicación con el servicio externo de inventario.

## 5. Estructura del proyecto

```text
sistema-pedidos-springboot/
|
|-- pom.xml
|-- README.md
|-- mvnw
|-- mvnw.cmd
|-- postman/
|
|-- src/
    |
    |-- main/
    |   |
    |   |-- java/com/isil/edu/pe/spedidos/
    |   |   |
    |   |   |-- client/
    |   |   |
    |   |   |-- config/
    |   |   |   |-- InventarioHttpConfig.java
    |   |   |   |-- SecurityConfig.java
    |   |   |
    |   |   |-- controller/
    |   |   |   |-- dto/
    |   |   |   |-- HomeController.java
    |   |   |   |-- PedidoController.java
    |   |   |   |-- PedidoApiController.java
    |   |   |   |-- PedidoReporteController.java
    |   |   |   |-- PedidoReporteWebController.java
    |   |   |
    |   |   |-- entity/
    |   |   |   |-- Pedido.java
    |   |   |
    |   |   |-- exception/
    |   |   |   |-- PedidoException.java
    |   |   |
    |   |   |-- repository/
    |   |   |   |-- mapper/
    |   |   |   |-- projection/
    |   |   |   |   |-- ClienteFrecuenteProjection.java
    |   |   |   |   |-- ProductoVendidoProjection.java
    |   |   |   |   |-- ResumenPedidosProjection.java
    |   |   |   |
    |   |   |   |-- PedidoRepository.java
    |   |   |   |-- PedidoReporteJdbcRepository.java
    |   |   |
    |   |   |-- service/
    |   |   |   |-- domain/
    |   |   |   |-- impl/
    |   |   |   |   |-- PedidoServiceImpl.java
    |   |   |   |   |-- PedidoReporteServiceImpl.java
    |   |   |   |
    |   |   |   |-- PedidoService.java
    |   |   |   |-- PedidoReporteService.java
    |   |   |
    |   |   |-- SistemaPedidosSpringbootApplication.java
    |   |
    |   |-- resources/
    |   |   |-- application.properties
    |   |
    |   |-- webapp/
    |       |-- assets/
    |       |   |-- css/
    |       |
    |       |-- WEB-INF/
    |           |-- views/
    |               |-- pedidos.jsp
    |               |-- reportes-pedidos.jsp
    |               |-- error.jsp
    |
    |-- test/
        |
        |-- java/com/isil/edu/pe/spedidos/
        |   |
        |   |-- controller/
        |   |   |-- PedidoApiControllerTest.java
        |   |   |-- PedidoReporteControllerIntegrationTest.java
        |   |   |-- PedidoSecurityIntegrationTest.java
        |   |
        |   |-- repository/
        |   |   |-- PedidoReporteJdbcRepositoryTest.java
        |   |
        |   |-- service/impl/
        |   |   |-- PedidoServiceImplTest.java
        |   |
        |   |-- SistemaPedidosSpringbootApplicationTests.java
        |
        |-- resources/
            |-- application-test.properties
```

## 6. Configuración de la base de datos

### MySQL

La aplicación utiliza MySQL para almacenar los pedidos registrados.

Configuración utilizada en el entorno local de desarrollo:

- Base de datos: `pedidosdb`
- Usuario de aplicación: `spedidos_app`
- Puerto: `3306`

Ejemplo de creación de la base de datos:

```sql
CREATE DATABASE IF NOT EXISTS pedidosdb
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

El usuario de aplicación debe contar con los permisos necesarios sobre esta base de datos.

### Configuración de conexión

El archivo `application.properties` utiliza propiedades de conexión que pueden externalizarse mediante variables de entorno.

Ejemplo:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/pedidosdb?serverTimezone=America/Lima}
spring.datasource.username=${DB_USERNAME:spedidos_app}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

Este bloque es una referencia de configuración. No es necesario reemplazar el archivo `application.properties` existente para utilizar este README.

La contraseña de MySQL debe configurarse localmente y no publicarse en el repositorio.

### H2 para pruebas

Las pruebas automatizadas utilizan H2 en memoria mediante el perfil `test`.

Esto permite ejecutar las pruebas sin depender de una instancia externa de MySQL.

## 7. Integración con inventario-service

El Sistema de Pedidos consume un servicio externo de inventario desarrollado con Jakarta EE y desplegado en WildFly.

Dirección local del servicio:

`http://localhost:8082/inventario-service/api/inventario`

Endpoint del catálogo:

`http://localhost:8082/inventario-service/api/inventario/productos`

La integración permite:

- Consultar productos disponibles.
- Obtener precios.
- Consultar existencias.
- Solicitar la reserva de stock al registrar pedidos.

El servicio de inventario utiliza SQL Server como sistema de persistencia externo.

### Consideración transaccional

Las transacciones locales de MySQL no abarcan automáticamente las operaciones HTTP realizadas contra inventario-service.

Por tanto, una reserva de stock confirmada externamente no se revierte automáticamente mediante un rollback local.

## 8. Funcionalidades principales

### 8.1. Registro de pedidos

Permite registrar pedidos indicando:

- Nombre del cliente.
- Producto seleccionado.
- Cantidad solicitada.

El sistema obtiene el precio del producto desde inventario-service y calcula el importe total.

Los pedidos se almacenan en MySQL mediante Spring Data JPA.

### 8.2. Listado de pedidos

La aplicación presenta los pedidos registrados mediante una interfaz JSP.

Se muestran:

- Identificador.
- Cliente.
- Código del producto.
- Nombre del producto.
- Precio unitario.
- Cantidad.
- Importe total.
- Fecha.

### 8.3. Resumen general

Se implementó un reporte que calcula:

- Total de pedidos.
- Total de unidades vendidas.
- Importe acumulado.
- Ticket promedio.

Se utiliza `JdbcTemplate` para ejecutar la consulta SQL de agregación.

### 8.4. Productos más vendidos

El reporte agrupa los pedidos por producto y calcula las unidades vendidas y el importe acumulado.

Utiliza `GROUP BY` y funciones de agregación SQL.

La API correspondiente incluye una representación HAL con enlaces HATEOAS.

## 9. PA3 — Búsqueda avanzada de pedidos

Se incorporó una búsqueda avanzada utilizando `NamedParameterJdbcTemplate`.

Permite filtrar pedidos por:

1. Coincidencia parcial del nombre del cliente.
2. Importe total mínimo.

Los resultados se ordenan por identificador descendente.

### Consulta SQL

```sql
SELECT
    id,
    cliente,
    producto_codigo,
    producto_nombre,
    precio_unitario,
    cantidad,
    total,
    fecha
FROM pedido
WHERE LOWER(cliente) LIKE LOWER(:cliente)
  AND total >= :totalMinimo
ORDER BY id DESC;
```

La consulta utiliza parámetros con nombre para evitar la concatenación directa de valores ingresados por el usuario.

### Endpoint REST

```http
GET /api/v1/pedidos/busqueda-avanzada?cliente=Juan&totalMinimo=100
```

La ruta debe utilizarse con el contexto `/sistema-pedidos`.

Ejemplo de URL completa:

`http://localhost:8081/sistema-pedidos/api/v1/pedidos/busqueda-avanzada?cliente=Juan&totalMinimo=100`

### Validaciones

- El nombre del cliente es obligatorio.
- El importe mínimo no puede ser negativo.
- Los parámetros inválidos producen una respuesta de error HTTP 400 según el manejo implementado en el controlador.

## 10. PA3 — Reporte de clientes frecuentes

Se implementó un reporte estadístico utilizando Spring JDBC.

El objetivo es identificar clientes que registran una cantidad mínima de pedidos.

### Consulta SQL

```sql
SELECT
    cliente,
    COUNT(*) AS total_pedidos,
    COALESCE(SUM(cantidad), 0) AS total_unidades,
    COALESCE(SUM(total), 0) AS importe_acumulado,
    COALESCE(AVG(total), 0) AS ticket_promedio
FROM pedido
GROUP BY cliente
HAVING COUNT(*) >= :minimoPedidos
ORDER BY
    total_pedidos DESC,
    importe_acumulado DESC,
    cliente ASC;
```

### Indicadores

| Indicador | Descripción |
|-----------|-------------|
| Cliente | Nombre del comprador |
| Total de pedidos | Cantidad de pedidos realizados |
| Total de unidades | Suma de productos comprados |
| Importe acumulado | Suma de importes |
| Ticket promedio | Promedio del importe por pedido |

### Validación

La cantidad mínima de pedidos debe ser mayor o igual a uno.

### Implementación

Se utiliza:

- `NamedParameterJdbcTemplate`
- `GROUP BY`
- `HAVING`
- `COUNT`
- `SUM`
- `AVG`
- `ClienteFrecuenteProjection`

## 11. PA3 — Interfaz web de reportes

Se incorporó una nueva vista JSP:

`src/main/webapp/WEB-INF/views/reportes-pedidos.jsp`

La interfaz permite consultar los reportes desde el navegador sin necesidad de utilizar Postman.

### Acceso

`http://localhost:8081/sistema-pedidos/reportes/pedidos`

### Secciones disponibles

**Resumen general de pedidos**

Presenta el total de pedidos, unidades vendidas, importe acumulado y ticket promedio.

**Búsqueda avanzada**

Incluye un formulario con:

- Cliente.
- Importe mínimo.
- Botón Buscar pedidos.

Los resultados se muestran en una tabla.

**Clientes frecuentes**

Incluye un formulario para indicar la cantidad mínima de pedidos y consultar los clientes que cumplen la condición.

La vista es gestionada mediante `PedidoReporteWebController`, que delega las operaciones a `PedidoReporteService`.

## 12. Endpoints REST

Las rutas siguientes son relativas al contexto `/sistema-pedidos`.

| Método | Endpoint | Funcionalidad |
|--------|----------|---------------|
| GET | `/pedidos` | Listado web de pedidos |
| POST | `/pedidos` | Registrar pedido |
| GET | `/reportes/pedidos` | Vista web de reportes |
| GET | `/api/v1/orders` | API de pedidos |
| GET | `/api/v1/orders/resumen` | Resumen de pedidos de la API original |
| GET | `/api/v1/pedidos?cliente=Ana` | Buscar pedidos por cliente |
| GET | `/api/v1/pedidos/estadisticas/resumen` | Resumen estadístico JDBC |
| GET | `/api/v1/pedidos/estadisticas/productos` | Productos más vendidos |
| GET | `/api/v1/pedidos/busqueda-avanzada?cliente=Juan&totalMinimo=100` | Búsqueda avanzada PA3 |

El reporte de clientes frecuentes también se encuentra disponible mediante la interfaz web y el controlador de reportes REST.

### Respuestas HTTP

- `200 OK`: consulta realizada correctamente.
- `400 Bad Request`: parámetros incorrectos.
- `403 Forbidden`: acceso prohibido por permisos o CSRF.
- `404 Not Found`: recurso inexistente.
- `500 Internal Server Error`: error inesperado.

## 13. Modelo de Madurez de Richardson

El proyecto conserva ejemplos académicos de diferentes niveles del Modelo de Madurez de Richardson.

### Nivel 1 — Recursos

Se identifica una colección de pedidos mediante una URI.

Ejemplo:

```http
GET /api/v1/pedidos?cliente=Ana
```

Devuelve un arreglo JSON sin enlaces hipermedia.

### Nivel 2 — Verbos HTTP

Se utilizan métodos HTTP y códigos de estado.

Ejemplo:

```http
GET /api/v1/pedidos/estadisticas/resumen
```

Devuelve una respuesta JSON con estado HTTP 200 cuando la consulta es exitosa.

### Nivel 3 — Hipermedia

Se incorporan enlaces HAL/HATEOAS para facilitar la navegación entre recursos.

Ejemplo:

```http
GET /api/v1/pedidos/estadisticas/productos
```

La respuesta incorpora enlaces como:

- `self`
- `estadisticas-resumen`
- `pedidos`

Esta diferenciación tiene fines académicos. Los niveles del modelo son acumulativos.

## 14. Seguridad con Spring Security

El proyecto implementa autenticación mediante formulario web y sesiones HTTP.

La configuración se encuentra en:

`config/SecurityConfig.java`

### Usuarios de laboratorio

| Usuario | Rol |
|---------|-----|
| operador | OPERADOR |
| consulta | CONSULTA |

Las contraseñas se obtienen de la configuración local o de variables de entorno.

No deben publicarse contraseñas reales en GitHub.

### Permisos

El rol OPERADOR puede consultar y registrar pedidos.

El rol CONSULTA puede acceder a consultas y reportes, pero no registrar pedidos.

Los usuarios anónimos deben autenticarse antes de acceder a los recursos protegidos.

### Protección CSRF

Los formularios POST incluyen el token CSRF generado por Spring Security.

Esto protege las operaciones que modifican información frente a solicitudes no autorizadas.

### Contraseñas

Se utiliza `BCryptPasswordEncoder` para codificar las contraseñas administradas por el gestor de usuarios en memoria.

Esta implementación es académica y no incluye OAuth2, JWT ni usuarios persistidos.

## 15. Pruebas automatizadas

El proyecto utiliza JUnit 5, Mockito, Spring Boot Test, MockMvc y Spring Security Test.

### Clases de pruebas

| Clase | Objetivo |
|-------|----------|
| PedidoServiceImplTest | Validar lógica de negocio |
| PedidoApiControllerTest | Validar respuestas JSON |
| PedidoReporteJdbcRepositoryTest | Verificar consultas JDBC |
| PedidoReporteControllerIntegrationTest | Validar endpoints REST |
| PedidoSecurityIntegrationTest | Verificar autenticación, roles y CSRF |
| SistemaPedidosSpringbootApplicationTests | Verificar carga del contexto |

### Cinco pruebas adicionales de la PA3

**Prueba 1 — Búsqueda avanzada JDBC**

Comprueba el filtrado de pedidos por cliente e importe mínimo.

**Prueba 2 — Clientes frecuentes**

Comprueba la agrupación de pedidos por cliente y la aplicación de un mínimo de compras.

**Prueba 3 — Validación HTTP 400**

Verifica que una solicitud con parámetros inválidos produzca la respuesta HTTP correspondiente.

**Prueba 4 — Acceso autorizado a reportes JSP**

Comprueba que un usuario con rol CONSULTA pueda acceder a la vista de reportes.

**Prueba 5 — Protección de la API**

Verifica que un usuario anónimo no pueda acceder directamente al reporte protegido de clientes frecuentes.

### Resultados obtenidos

Durante las verificaciones de la PA3 se obtuvieron:

- 5 pruebas aprobadas en PedidoReporteJdbcRepositoryTest.
- 5 pruebas aprobadas en PedidoReporteControllerIntegrationTest.
- 6 pruebas aprobadas en PedidoSecurityIntegrationTest.
- Ejecución general de Maven con `BUILD SUCCESS`.

Las pruebas de integración utilizan H2 y el perfil `test`.

### Ejecutar las pruebas

En Windows:

```powershell
.\mvnw.cmd test
```

También puede ejecutarse desde IntelliJ IDEA:

`Maven > Lifecycle > test`

## 16. Requisitos de ejecución

Para ejecutar el proyecto se requiere:

- JDK 21.
- IntelliJ IDEA u otro IDE compatible.
- Maven o Maven Wrapper.
- MySQL Server 8.
- Base de datos `pedidosdb`.
- Servicio inventario-service desplegado en WildFly.
- Puerto 8081 disponible para Spring Boot.
- Puerto 8082 disponible para inventario-service.

## 17. Ejecución local

### Paso 1. Iniciar MySQL

Verificar que MySQL Server esté ejecutándose y que exista la base de datos `pedidosdb`.

### Paso 2. Iniciar inventario-service

Ejecutar el servicio de inventario desde su proyecto Jakarta EE en WildFly.

Verificar:

`http://localhost:8082/inventario-service/api/inventario/productos`

### Paso 3. Configurar las credenciales

Verificar que `application.properties` o las variables de entorno contengan la configuración correcta de MySQL y los usuarios de laboratorio.

### Paso 4. Ejecutar Spring Boot

Desde IntelliJ IDEA, ejecutar:

`SistemaPedidosSpringbootApplication`

También puede utilizarse:

```powershell
.\mvnw.cmd spring-boot:run
```

### Paso 5. Iniciar sesión

Abrir:

`http://localhost:8081/sistema-pedidos/login`

Utilizar las credenciales de laboratorio configuradas localmente.

### Paso 6. Consultar pedidos

Abrir:

`http://localhost:8081/sistema-pedidos/pedidos`

### Paso 7. Consultar reportes PA3

Abrir:

`http://localhost:8081/sistema-pedidos/reportes/pedidos`

## 18. Evidencias de implementación PA3

Durante el desarrollo se recopilaron evidencias de:

1. Implementación de la búsqueda avanzada con Spring JDBC.
2. Implementación del reporte de clientes frecuentes.
3. Validaciones en la capa de servicios.
4. Controladores REST de reportes.
5. Consulta de pedidos mediante endpoints.
6. Reporte de clientes frecuentes en JSON.
7. Interfaz web JSP de reportes.
8. Búsqueda avanzada desde el navegador.
9. Validación de parámetros inválidos.
10. Pruebas automatizadas de repositorio.
11. Pruebas automatizadas del controlador REST.
12. Pruebas automatizadas de seguridad.
13. Ejecución general de Maven con BUILD SUCCESS.

Estas evidencias forman parte del informe académico de la PA3.

## 19. Limitaciones

El proyecto se desarrolla con fines académicos.

Entre sus limitaciones se encuentran:

- Usuarios administrados en memoria.
- Ausencia de autenticación JWT u OAuth2.
- Dependencia de un servicio externo de inventario.
- Ausencia de transacciones distribuidas entre MySQL y SQL Server.
- Configuración local orientada a laboratorio.
- Uso de `ddl-auto=update` para desarrollo.

Para un entorno de producción sería necesario fortalecer la gestión de credenciales, observabilidad, despliegue y consistencia de datos.

## 20. Conclusiones

La Práctica Académica 3 permitió ampliar el Sistema de Pedidos mediante el uso de Spring JDBC y consultas SQL avanzadas.

Se implementó una búsqueda parametrizada de pedidos y un reporte estadístico de clientes frecuentes, aplicando funciones de agregación, GROUP BY y HAVING.

Asimismo, se incorporó una interfaz JSP para visualizar los resultados y se mantuvo la separación de responsabilidades entre controladores, servicios y repositorios.

Las pruebas automatizadas permitieron verificar el comportamiento de los componentes, la validación de parámetros y los controles de seguridad.

La ejecución general de Maven finalizó satisfactoriamente con el resultado `BUILD SUCCESS`.

El proyecto demuestra la integración de Spring MVC, Spring Data JPA, Spring JDBC, MySQL, Spring Security y servicios externos Jakarta EE en una aplicación empresarial académica.

## 21. Referencias técnicas

- Spring Boot: https://docs.spring.io/spring-boot/
- Spring Framework: https://docs.spring.io/spring-framework/reference/
- Spring JDBC: https://docs.spring.io/spring-framework/reference/data-access/jdbc.html
- Spring Security: https://docs.spring.io/spring-security/reference/
- Spring HATEOAS: https://docs.spring.io/spring-hateoas/docs/current/reference/html/
- JUnit 5: https://junit.org/junit5/
- MySQL: https://dev.mysql.com/doc/
- Maven: https://maven.apache.org/

---

**Institución:** ISIL

**Curso:** Desarrollo de Aplicaciones Empresariales Avanzado

**Evaluación:** Práctica Académica 3 (PA3)

**Proyecto:** Sistema de Pedidos — Spring Boot

**Periodo académico:** 2026-II
