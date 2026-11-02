# SGDI — Sistema de Gestión Documental Inteligente

## Descripción y objetivo

SGDI es una aplicación web para centralizar la administración de documentos institucionales y sus metadatos. El objetivo general descrito en [`docs/contexto.md`](docs/contexto.md) contempla registrar, consultar, validar y clasificar documentos, con apoyo futuro de servicios de inteligencia artificial.

La implementación actual está en una etapa inicial: permite consultar documentos por identificador mediante REST y MVC, y listar los documentos en la interfaz MVC. El registro de documentos y las demás capacidades descritas como objetivo no deben confundirse con funcionalidades ya disponibles.

## Tecnologías y versiones

Versiones identificadas en los archivos del proyecto:

| Tecnología | Versión o referencia |
|---|---|
| Java | 21 (versión objetivo declarada en `pom.xml`) |
| Spring Boot | 4.1.2 (parent de Maven) |
| Maven | 3.10.0, gestionado por Maven Wrapper 3.3.4 |
| MySQL | 8.0.16 o superior, según el encabezado del esquema aprobado |
| Spring MVC, Spring Data JPA, Thymeleaf y Bean Validation | Versiones administradas por Spring Boot; no fijadas individualmente en `pom.xml` |

El proyecto también usa el controlador JDBC de MySQL y Spring Boot DevTools. Sus versiones son administradas por el parent de Spring Boot.

## Arquitectura y paquetes

La aplicación sigue una organización por responsabilidades:

- `com.usc.sgdi`: clase de arranque `SgdiApplication`.
- `com.usc.sgdi.controller.rest`: endpoints HTTP/JSON.
- `com.usc.sgdi.controller.mvc`: rutas web que devuelven vistas Thymeleaf.
- `com.usc.sgdi.application.service`: consulta de documentos y coordinación de persistencia.
- `com.usc.sgdi.dto`: modelo de respuesta para la consulta REST.
- `com.usc.sgdi.domain.model`: entidades JPA relacionadas con las tablas del esquema.
- `com.usc.sgdi.persistence.repository`: repositorios Spring Data JPA.
- `src/main/resources/templates/documentos`: vistas `listado`, `detalle` y `no-encontrado`.
- `src/main/resources/static`: recursos CSS y JavaScript de la interfaz.

El flujo de consulta implementado es controlador → `ConsultarDocumentoService` → `DocumentoRepository` → base de datos. El servicio transforma la entidad `Documento` en `DocumentoResponse` para la salida REST y la vista de detalle.

## Requisitos

- JDK 21.
- MySQL 8.0.16 o superior.
- PowerShell en Windows, o un shell compatible para ejecutar el Maven Wrapper.
- Acceso a la base de datos configurada mediante las variables de entorno indicadas abajo.

## Configuración de MySQL

El esquema aprobado se encuentra en [`database/schema.sql`](database/schema.sql). Define la base de datos `sgdi` y las tablas `usuarios`, `roles`, `tipos_documento`, `usuarios_roles`, `categorias`, `proveedor_ia`, `notificaciones`, `documentos` y `clasificacion_ia`.

1. Inicia una instancia MySQL compatible.
2. Ejecuta `database/schema.sql` con una cuenta que pueda crear/inspeccionar la base de datos y sus tablas. El archivo incluye también consultas de verificación.
3. Configura las variables de conexión antes de iniciar la aplicación.

La configuración de Hibernate usa `ddl-auto=validate`: la aplicación valida el esquema existente y no está configurada para crearlo o actualizarlo. El proyecto configura la zona horaria JDBC de Hibernate como UTC y desactiva `open-in-view`.

## Variables de entorno

[`src/main/resources/application.properties`](src/main/resources/application.properties) requiere estas variables. No se incluyen valores ni credenciales en este README:

| Variable | Uso |
|---|---|
| `SGDI_DB_URL` | URL JDBC de la base de datos `sgdi` |
| `SGDI_DB_USERNAME` | Usuario MySQL de la aplicación |
| `SGDI_DB_PASSWORD` | Contraseña MySQL de la aplicación |

Configúralas en el entorno de ejecución o mediante un gestor de secretos. No guardes credenciales reales en el repositorio.

## Ejecución con Maven Wrapper

Desde la raíz del proyecto, configura las tres variables anteriores y ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

Para compilar y ejecutar las pruebas:

```powershell
.\mvnw.cmd test
```

En Linux o macOS se puede usar `./mvnw` con las variables exportadas en el shell.

## Endpoints REST existentes

| Método | Ruta | Comportamiento |
|---|---|---|
| `POST` | `/api/documentos` | Registra metadatos; devuelve `201 Created`, `Location` y el documento creado. |
| `GET` | `/api/documentos/{id_documento}` | Devuelve el documento en JSON. Responde `200` si existe, `404` si no existe y `400` para identificadores no positivos o no convertibles a entero. |

La creación recibe IDs de categoría, usuario y tipo documental, más nombre, descripción, ruta y tamaño. La fecha de carga es asignada por el servidor en UTC; no se carga el archivo físico. Las referencias inexistentes producen `404` y los datos que incumplen las validaciones producen `400`. La respuesta REST expone `id_documento`, `id_categoria`, `id_usuario`, `id_tipo_documento`, `nombre`, `descripcion`, `path`, `tamanio` y `fecha_carga`. No hay otros endpoints REST declarados en los controladores actuales. Consulta el [contrato detallado y los ejemplos ilustrativos](docs/api-rest.md); los ejemplos no representan una ejecución verificada.

## Rutas MVC

| Método | Ruta | Resultado |
|---|---|---|
| `GET` | `/documentos` | Lista documentos en `documentos/listado`. |
| `GET` | `/documentos/nuevo` | Muestra el formulario para registrar metadatos. |
| `POST` | `/documentos` | Valida y registra metadatos; redirige al detalle o vuelve al formulario con errores. |
| `GET` | `/documentos/{id_documento}` | Muestra `documentos/detalle` si existe o `documentos/no-encontrado` con estado HTTP `404`. |

El listado incluye un formulario que navega a la ruta de consulta por identificador existente; no añade un endpoint de búsqueda nuevo.

## Casos de uso implementados

- Listar los documentos disponibles en la vista MVC.
- Consultar un documento existente por identificador, por REST o MVC.
- Informar cuando el identificador consultado no corresponde a un documento.
- Registrar metadatos de documento por REST y mediante formulario MVC; las referencias a categoría, usuario y tipo documental deben existir.

El caso de uso `CU-01: Registrar y consultar los metadatos de un documento` ya cuenta con flujos de alta y consulta en el código. La verificación de persistencia contra MySQL y de los flujos completos debe realizarse en el entorno configurado.

## Pruebas: existentes, verificadas y pendientes

Hay pruebas automatizadas definidas en:

- `DocumentoControllerTest`: comprueba respuestas para documento existente (`200`), inexistente (`404`), identificador no positivo (`400`) y no numérico (`400`) mediante MockMvc.
- `DocumentoControllerTest`: contiene expectativas para alta `201`, validación `400` y referencia inexistente `404`.
- `ConsultarDocumentoServiceTest`: contiene pruebas unitarias del mapeo y el rechazo de una referencia inexistente.
- `DocumentoMvcControllerTest`: contiene expectativas para el formulario, registro/redirección y validación MVC.
- `SgdiApplicationTests`: comprueba la carga del contexto de Spring.

**Estado de verificación:** la ejecución Maven dirigida de las pruebas REST, de servicio y MVC reportó 13 pruebas aprobadas y 0 fallidas. La ejecución completa todavía falla en `SgdiApplicationTests` porque no hay una URL JDBC válida configurada en este entorno. La persistencia contra MySQL real queda pendiente de verificación; ejecuta `.\mvnw.cmd test` con la base configurada.

Pendiente de implementar o verificar en pruebas:

- Verificación de renderizado de vistas MVC mediante una prueba de integración y de la integración con MySQL.
- Verificación de persistencia real y lectura posterior en MySQL.
- Las pruebas de funcionamiento con Postman mencionadas como alcance propuesto en el contexto; no se encontró evidencia de una colección o ejecución en el proyecto.

## Funcionalidades futuras pendientes

Las siguientes capacidades aparecen en los documentos de contexto, pero no están implementadas en las rutas y servicios actuales:

- Carga física de archivos PDF.
- Inicio de sesión, autorización y gestión de usuarios/permisos.
- Administración de categorías e historiales.
- Clasificación asistida por IA, incluida la integración con Gemini, y clasificación manual.
- Envío de notificaciones por correo.

Los detalles funcionales, reglas de validación, autorización, tratamiento de errores e integraciones de estas capacidades requieren definición antes de implementarlas.
