# API REST de SGDI

## Alcance y versión

Este documento describe únicamente las rutas REST declaradas actualmente en `DocumentoController`. La versión declarada en el `pom.xml` es Spring Boot **4.1.2**.

No se encontró una dependencia ni una configuración de Springdoc/OpenAPI o Swagger en el proyecto. Por tanto, este archivo documenta el contrato visible en el código; no existe actualmente una especificación OpenAPI servida por la aplicación.

## `POST /api/documentos`

Registra metadatos de un documento. No carga ni almacena el archivo físico.

### Entrada

El cuerpo debe ser JSON con un objeto `CrearDocumentoRequest`. No se envían `id_documento` ni `fecha_carga`: el identificador es generado por MySQL y la fecha se asigna en el servidor usando UTC.

| Campo JSON | Tipo | Requerido | Validación |
|---|---|---|---|
| `id_categoria` | `integer` | Sí | Mayor que cero y debe identificar una categoría existente. |
| `id_usuario` | `integer` | Sí | Mayor que cero y debe identificar un usuario existente. |
| `id_tipo_documento` | `integer` | Sí | Mayor que cero y debe identificar un tipo documental existente. |
| `nombre` | `string` | Sí | No vacío/blanco; máximo 50 caracteres. |
| `descripcion` | `string` | Sí | No vacío/blanco; máximo 200 caracteres. |
| `path` | `string` | Sí | No vacío/blanco; máximo 200 caracteres. Es solo el metadato de ruta, no implica carga de archivo. |
| `tamanio` | `integer` | Sí | Cero o mayor, expresado en bytes. |

### Respuesta

**`201 Created`** — devuelve el objeto `DocumentoResponse` descrito abajo e incluye en la cabecera `Location` la URI de consulta del documento recién creado (`/api/documentos/{id_documento}`).

### Respuestas de error

| Código | Condición implementada |
|---|---|
| `400 Bad Request` | JSON no válido, conversión fallida o incumplimiento de las validaciones del DTO. |
| `404 Not Found` | `id_categoria`, `id_usuario` o `id_tipo_documento` no corresponde a un registro existente. La respuesta no tiene cuerpo personalizado. |

### Ejemplo ilustrativo de creación

Los valores de ejemplo son ilustrativos y no corresponden a una ejecución verificada.

Solicitud:

```http
POST /api/documentos
Content-Type: application/json
Accept: application/json
```

```json
{
  "id_categoria": 2,
  "id_usuario": 3,
  "id_tipo_documento": 4,
  "nombre": "Acta",
  "descripcion": "Acta de reunión",
  "path": "/uploads/acta.pdf",
  "tamanio": 1024
}
```

Posible respuesta `201 Created`:

```http
Location: http://localhost:8080/api/documentos/7
```

```json
{
  "id_documento": 7,
  "id_categoria": 2,
  "id_usuario": 3,
  "id_tipo_documento": 4,
  "nombre": "Acta",
  "descripcion": "Acta de reunión",
  "path": "/uploads/acta.pdf",
  "tamanio": 1024,
  "fecha_carga": "2026-10-29T12:00:00"
}
```

## `GET /api/documentos/{id_documento}`

Consulta un documento por su identificador.

### Parámetros

| Nombre | Ubicación | Tipo | Requerido | Descripción |
|---|---|---|---|---|
| `id_documento` | Path | `integer` (`Integer` en Java) | Sí | Identificador del documento. El controlador responde `400` si el valor convertido es menor o igual a cero. |

No recibe DTO de entrada ni cuerpo de solicitud.

### Respuesta de éxito

**`200 OK`** — el documento existe. El cuerpo es un objeto `DocumentoResponse` serializado como JSON:

| Campo JSON | Tipo Java | Descripción |
|---|---|---|
| `id_documento` | `Integer` | Identificador del documento. |
| `id_categoria` | `Integer` | Identificador de la categoría asociada. |
| `id_usuario` | `Integer` | Identificador del usuario asociado. |
| `id_tipo_documento` | `Integer` | Identificador del tipo documental asociado. |
| `nombre` | `String` | Nombre del documento. |
| `descripcion` | `String` | Descripción del documento. |
| `path` | `String` | Ruta registrada para el documento. |
| `tamanio` | `Long` | Tamaño registrado en bytes. |
| `fecha_carga` | `LocalDateTime` | Fecha y hora de carga. |

El DTO se declara en `DocumentoResponse`. Los nombres de cuatro campos se fijan explícitamente con `@JsonProperty`; los restantes usan los nombres de sus componentes del record.

### Respuestas de error

| Código | Condición implementada |
|---|---|
| `400 Bad Request` | El identificador de ruta no se puede convertir a `Integer`, según el comportamiento de conversión de Spring MVC, o el controlador recibe un valor menor o igual a cero. |
| `404 Not Found` | El servicio no encuentra un documento con ese identificador; `ResponseEntity.of(Optional.empty())` produce esta respuesta sin cuerpo. |

El controlador no declara un DTO de error ni personaliza el cuerpo de estas respuestas. No se encontró un `@ControllerAdvice`, `@RestControllerAdvice` ni otro manejador global de excepciones.

### Ejemplos ilustrativos

Los siguientes ejemplos ilustran la ruta y la forma esperada de la respuesta; **no son capturas de una ejecución verificada**.

Solicitud:

```http
GET /api/documentos/7
Accept: application/json
```

Posible respuesta `200 OK` (valores ilustrativos):

```json
{
  "id_documento": 7,
  "id_categoria": 2,
  "id_usuario": 3,
  "id_tipo_documento": 4,
  "nombre": "Acta",
  "descripcion": "Acta de reunión",
  "path": "/uploads/acta.pdf",
  "tamanio": 1024,
  "fecha_carga": "2026-10-29T12:00:00"
}
```

Ejemplos de rutas que corresponden a errores documentados en el código:

```http
GET /api/documentos/0
```

El controlador construye `400 Bad Request` para este identificador.

```http
GET /api/documentos/99
```

Si el documento no existe, el controlador construye `404 Not Found`.

### Evidencia disponible y estado de verificación

`DocumentoControllerTest` contiene expectativas MockMvc para:

- `201` en una solicitud válida, con cabecera `Location` y cuerpo JSON.
- `400` ante campos no válidos, sin invocar el servicio.
- `404` ante una referencia inexistente.
- `200` al consultar el identificador `7`, incluyendo aserciones JSON para `id_documento`, `id_categoria` y `path`.
- `404` para el identificador `99` cuando el servicio devuelve `Optional.empty()`.
- `400` para `0`.
- `400` para el segmento no numérico `no-es-numero`.

`ConsultarDocumentoServiceTest` comprueba el mapeo de relaciones, la fecha generada y que una categoría inexistente impide persistir. `DocumentoMvcControllerTest` comprueba el formulario, el flujo de registro/redirección y la validación del formulario.

La ejecución Maven dirigida de las pruebas REST, de servicio y MVC reportó **13 pruebas aprobadas y 0 fallidas**. Esta ejecución no incluye una verificación de persistencia contra MySQL real. La suite completa no pudo terminar porque `SgdiApplicationTests` requiere una URL JDBC válida, ausente en el entorno de ejecución. Los ejemplos de este documento siguen siendo ilustrativos y no respuestas observadas.

## Rutas fuera del contrato REST

Las rutas `/documentos`, `/documentos/nuevo`, `POST /documentos` y `/documentos/{id_documento}` pertenecen al controlador MVC: gestionan vistas Thymeleaf y no forman parte de los endpoints REST documentados aquí. No se encontraron otros controladores REST en el código fuente actual.
