DOCUMENTO DE CONTEXTUALIZACIÓN DEL SISTEMA DE GESTIÓN DOCUMENTAL INTELIGENTE (SGDI)
1. Descripción general del sistema
El Sistema de Gestión Documental Inteligente (SGDI) es una solución de software orientada a la administración de documentos institucionales, que permite registrar, consultar, validar y clasificar información documental mediante procesos tradicionales y servicios de inteligencia artificial.
El sistema está diseñado para apoyar las actividades de funcionarios y administradores, centralizando la información documental y facilitando su organización mediante categorías, metadatos y mecanismos de clasificación.
Una de sus características principales es la posibilidad de consumir un servicio externo de inteligencia artificial, inicialmente Gemini, para analizar información de un documento y sugerir una categoría documental.
El SGDI contempla también un procedimiento alternativo de clasificación manual cuando no se utiliza el servicio de inteligencia artificial.
La solución se implementará como una aplicación web basada en Spring Boot, conectada a una base de datos relacional MySQL, con una interfaz web y componentes de integración externos.
Su desarrollo debe realizarse progresivamente, respetando los artefactos de análisis y diseño elaborados previamente.
2. Objetivo general
Desarrollar un sistema web de gestión documental que permita registrar, consultar, validar y clasificar documentos institucionales mediante una arquitectura modular, incorporando servicios de inteligencia artificial para apoyar la clasificación y mecanismos de persistencia, administración y notificación.
3. Actores del sistema
El diagrama de contexto identifica dos actores principales.
3.1. Funcionario
Es el usuario encargado de interactuar con las funcionalidades documentales del SGDI.
Sus responsabilidades representadas en los diagramas incluyen:
•	Cargar información de documentos.
•	Consultar documentos.
•	Validar información documental.
•	Solicitar o utilizar sugerencias de clasificación.
•	Participar en el proceso de clasificación documental.
El funcionario se comunica con el sistema mediante solicitudes HTTP desde la aplicación web.
3.2. Administrador
Es el actor responsable de las funciones administrativas del SGDI.
El diagrama de contexto le asigna las siguientes responsabilidades:
•	Administrar usuarios.
•	Gestionar permisos.
•	Gestionar categorías documentales.
•	Administrar historiales.
La existencia de estos dos actores implica que el sistema debe contemplar una diferenciación de responsabilidades y permisos.
Sin embargo, los diagramas no especifican todavía la matriz completa de autorizaciones ni los permisos individuales asociados a cada rol. Esas reglas deberán establecerse antes de implementar el control de acceso.
4. Requerimientos funcionales identificados
A partir de los diagramas se identifican los siguientes requerimientos funcionales preliminares.
RF-01. Inicio de sesión. El sistema debe disponer de un proceso de inicio de sesión para permitir el acceso de los usuarios.
RF-02. Registro de información documental. El funcionario debe poder ingresar la información asociada a un documento.
RF-03. Validación de información. El sistema debe validar los datos asociados al documento antes de continuar con su procesamiento.
RF-04. Persistencia documental. El sistema debe guardar la información documental en la base de datos.
RF-05. Clasificación asistida por IA. El sistema debe permitir solicitar una sugerencia de clasificación documental a un proveedor externo de inteligencia artificial.
RF-06. Recepción de resultados de IA. El sistema debe recibir y procesar la respuesta del servicio de inteligencia artificial.
RF-07. Clasificación manual. El sistema debe contemplar una alternativa de clasificación manual cuando no se consume la API de IA.
RF-08. Consulta documental. El funcionario debe poder consultar información de documentos registrados.
RF-09. Administración de usuarios y permisos. El administrador debe poder gestionar usuarios y sus permisos, según las reglas de autorización que se definan.
RF-10. Administración de categorías. El administrador debe poder gestionar categorías documentales.
RF-11. Gestión de historiales. El administrador debe disponer de funciones de administración de historiales, cuyo alcance deberá detallarse.
RF-12. Notificaciones por correo. El sistema contempla un componente capaz de enviar notificaciones mediante un servicio externo de correo electrónico.
Observación: los diagramas identifican estas capacidades, pero no detallan completamente sus operaciones, validaciones, condiciones de ejecución ni criterios de aceptación. Por tanto, constituyen una base de requerimientos que debe complementarse con especificaciones de casos de uso.
5. Interpretación del diagrama de flujo
El diagrama de flujo representa el proceso general de registro y clasificación documental.
5.1. Inicio de sesión
El proceso comienza cuando el usuario intenta iniciar sesión.
El diagrama contempla una opción de reintento, lo que indica que el proceso de autenticación debe permitir gestionar intentos que no culminen exitosamente.
No se especifican todavía las condiciones de bloqueo, recuperación de contraseña o número máximo de intentos.
5.2. Carga de información documental
Después del inicio de sesión, el usuario accede a la actividad denominada «Cargar información de documento».
En esta etapa se suministran los datos necesarios para registrar o procesar un documento.
El diagrama no distingue expresamente entre cargar metadatos y cargar un archivo físico. Por tanto, la implementación de archivos PDF debe definirse como una especificación adicional y no asumirse únicamente a partir de esta actividad.
5.3. Validación de datos
La información pasa a la actividad «Validar datos asociados al documento».
Esta etapa representa la necesidad de verificar la información recibida antes de almacenarla.
En la implementación, estas validaciones podrán distribuirse entre los DTO, los servicios de aplicación, las reglas del dominio y las restricciones de la base de datos, según corresponda.
El diagrama representa un intercambio entre la carga y la validación de información, incluyendo el retorno de una respuesta.
5.4. Almacenamiento inicial
Una vez procesada la validación, el flujo continúa hacia «Guardar datos en Base de datos».
Esta actividad indica que la información documental se almacena antes de realizar la decisión de consumir o no la API de inteligencia artificial.
Este orden es relevante: el registro documental y la clasificación asistida son responsabilidades relacionadas, pero conceptualmente diferenciables.
5.5. Decisión sobre el consumo de IA
El proceso incorpora una decisión denominada «Consumir API de IA».
Existen dos alternativas:
Ruta de consumo de IA: el sistema solicita una sugerencia de clasificación del documento y un resumen, y posteriormente recibe la respuesta.
Ruta sin consumo de IA: el proceso continúa hacia la actividad «Clasificar Manualmente».
El diagrama no especifica qué condición determina la elección entre ambas rutas. Puede tratarse de una decisión del usuario, una configuración o una regla de negocio, pero ninguna de estas posibilidades debe asumirse sin confirmación.
5.6. Procesamiento de la respuesta de IA
Cuando se utiliza la ruta asistida, se realiza una solicitud de sugerencia de clasificación y resumen.
Posteriormente, el sistema recibe la respuesta y ejecuta una actividad de almacenamiento en la base de datos.
Por tanto, el flujo muestra un segundo momento de persistencia relacionado con el resultado del procesamiento.
No se detalla todavía qué información exacta debe almacenarse, si la sugerencia requiere aprobación humana o cómo se gestionan errores del proveedor.
5.7. Clasificación manual
La ruta alternativa finaliza en la actividad «Clasificar Manualmente».
Esto confirma que el proceso documental no depende exclusivamente de la disponibilidad de inteligencia artificial.
No obstante, el diagrama no muestra expresamente una actividad de almacenamiento después de la clasificación manual. Esa omisión debe aclararse para garantizar que el resultado manual quede registrado.
5.8. Finalización del proceso
El diagrama presenta dos eventos finales, correspondientes a las rutas de clasificación asistida y manual.
Para su implementación se recomienda especificar los resultados esperados de ambas rutas y las condiciones que permiten considerar el proceso terminado.
6. Interpretación del diagrama de contexto
El diagrama de contexto presenta al SGDI como el sistema central que interactúa con actores humanos y servicios tecnológicos externos.
6.1. SGDI como sistema central
El SGDI recibe solicitudes HTTP de los funcionarios y administradores.
Su responsabilidad es coordinar las operaciones documentales y comunicarse con los servicios necesarios para ejecutarlas.
6.2. Proveedor de inteligencia artificial
El diagrama identifica un componente externo denominado «Proveedor de IA [API]».
Su función es analizar contenidos y sugerir una categoría documental.
La comunicación desde el SGDI se representa mediante una solicitud de sugerencia HTTP.
En el desarrollo se utilizará inicialmente Gemini como proveedor, pero el sistema debe evitar una dependencia rígida de esa tecnología.
6.3. Servicio de correo electrónico
El SGDI se conecta con un servicio externo de correo mediante SMTP.
El objetivo es gestionar el envío de notificaciones.
Los diagramas no especifican qué eventos generan las notificaciones ni quiénes deben recibirlas. Estas decisiones deberán documentarse como reglas funcionales.
6.4. Base de datos
El diagrama representa una base de datos MySQL encargada del almacenamiento y recuperación de metadatos documentales y otros datos del sistema.
La comunicación se realiza mediante operaciones SQL.
En la implementación Spring Boot se utilizará Spring Data JPA para gestionar la persistencia de entidades.
El diagrama representa MySQL como un contenedor de almacenamiento, pero no define por sí solo las tablas, columnas, restricciones y relaciones. La fuente de verdad para esos detalles será el modelo entidad–relación y su script SQL aprobado.
7. Interpretación del diagrama de componentes
El diagrama de componentes describe cómo se organiza internamente la solución y cómo se relacionan sus responsabilidades.
Se identifican los siguientes componentes.
7.1. Aplicación web
Tecnologías indicadas: HTML, CSS, JavaScript y solicitudes HTTP.
Representa la interfaz mediante la cual los usuarios interactúan con el SGDI.
Sus responsabilidades son capturar información, presentar formularios, mostrar resultados y enviar solicitudes al servidor.
Para la implementación se utilizará Thymeleaf como motor de plantillas integrado con Spring MVC.
No se utilizará React en esta etapa.
7.2. Controlador Documento
Tecnología indicada: Spring Boot Controller.
Recibe las solicitudes HTTP y coordina la entrada de datos hacia los servicios.
El diagrama le asigna la validación de solicitudes HTTP y la generación de DTO.
En la implementación se distinguirán dos tipos de controladores:
•	Controladores REST, responsables de exponer servicios HTTP con respuestas JSON.
•	Controladores MVC, responsables de gestionar las vistas HTML con Thymeleaf.
Ambos deben reutilizar los mismos servicios de aplicación.
7.3. Procesar documento
Tecnología indicada: Spring Boot Service.
Es el componente que coordina el caso de uso documental.
El diagrama lo presenta como el punto central de interacción entre el controlador, el documento, la persistencia, la clasificación y las notificaciones.
Sus responsabilidades pueden incluir coordinar validaciones, gestionar operaciones documentales y solicitar servicios externos mediante contratos.
No debe concentrar indiscriminadamente todas las responsabilidades. A medida que crezca el proyecto, las operaciones podrán separarse en servicios específicos.
7.4. Documento
Tecnología indicada: Spring Boot Entity.
Representa el concepto documental y sus datos.
El diagrama lo asocia con el estado y las reglas de negocio.
En la implementación inicial, las entidades JPA deberán corresponder con las tablas aprobadas en el script SQL.
Debe evitarse inventar atributos o relaciones que no estén documentados.
7.5. Repositorio de Documentos
Rol arquitectónico indicado: puerto de salida para persistencia de datos.
El diagrama lo representa como el contrato utilizado por el servicio para guardar y recuperar información.
Su finalidad es separar las necesidades de persistencia de la implementación tecnológica.
7.6. DAO Documento
Rol arquitectónico indicado: adaptador de salida basado en Spring Boot, driver y JDBC.
Este componente implementa el contrato de persistencia.
Su responsabilidad es realizar las operaciones necesarias para interactuar con la base de datos.
Decisión de implementación: se utilizará Spring Data JPA. Por ello, la implementación concreta puede realizarse mediante un adaptador que utilice un repositorio JPA, evitando escribir manualmente un DAO JDBC si no es necesario.
Debe distinguirse el contrato de persistencia de su implementación concreta.
7.7. Clasificador IA
Rol arquitectónico indicado: puerto de salida hacia el proveedor de inteligencia artificial.
Define la capacidad que necesita el SGDI para solicitar una clasificación documental.
Su función es establecer un contrato independiente del proveedor.
7.8. Clasificador Gemini
Rol arquitectónico indicado: adaptador de salida para la API Gemini.
Implementa el contrato de clasificación.
Este componente será responsable de comunicarse con Gemini, enviar la información requerida y procesar la respuesta.
La lógica central del SGDI no debe depender directamente de las clases o formatos particulares del proveedor.
7.9. Notificador Correo
Rol arquitectónico indicado: puerto de salida para notificaciones electrónicas.
Define la capacidad de enviar mensajes de correo sin comprometer al servicio de aplicación con una implementación específica.
7.10. Adaptador de correo
Rol arquitectónico indicado: adaptador de salida SMTP.
Implementa el contrato de notificación mediante un servicio de correo electrónico.
Esta separación permitiría cambiar posteriormente de proveedor o mecanismo de envío sin alterar el caso de uso principal.
8. Arquitectura de software seleccionada
El SGDI utilizará una arquitectura en capas con elementos de puertos y adaptadores.
Esta decisión permite organizar las responsabilidades de forma comprensible y mantener las integraciones externas desacopladas de la lógica de aplicación.
8.1. Capa de presentación
Recibe las interacciones del usuario.
Incluye controladores MVC, controladores REST y vistas Thymeleaf.
8.2. Capa de aplicación
Coordina los casos de uso del sistema.
Contiene los servicios que ejecutan operaciones documentales y utilizan contratos para acceder a recursos externos.
8.3. Capa de dominio
Representa los conceptos fundamentales y las reglas propias del SGDI.
Incluye entidades y, cuando corresponda, objetos de valor y reglas del negocio.
8.4. Capa de persistencia
Implementa el acceso a MySQL mediante Spring Data JPA.
Debe mantener la correspondencia con el esquema SQL aprobado.
8.5. Capa de integración
Contiene los contratos y adaptadores necesarios para comunicarse con Gemini, SMTP y futuras tecnologías.
La finalidad es que los servicios dependan de abstracciones y no de implementaciones externas concretas.
9. Estructura de paquetes propuesta
La siguiente estructura representa la organización inicial prevista para el código Java.
src/main/
├── java/com/sgdi/
│   ├── SgdiBackendApplication.java
│   │
│   ├── controller/
│   │   ├── mvc/
│   │   └── rest/
│   │
│   ├── application/
│   │   └── service/
│   │
│   ├── domain/
│   │   └── model/
│   │
│   ├── persistence/
│   │   └── repository/
│   │
│   ├── dto/
│   │
│   └── integration/
│       ├── port/
│       └── adapter/
│
└── resources/
    ├── templates/
    ├── static/
    └── application.properties

database/
└── sgdi.sql
Esta estructura es una propuesta inicial y podrá evolucionar cuando se definan todos los casos de uso.
Responsabilidades
controller/mvc: gestiona solicitudes web y devuelve vistas Thymeleaf.
controller/rest: recibe solicitudes de API y devuelve respuestas HTTP, principalmente JSON.
application/service: coordina los casos de uso, sin incluir detalles innecesarios de infraestructura.
domain/model: representa las entidades y reglas del negocio.
persistence/repository: contiene los componentes de acceso a datos y las implementaciones de persistencia.
dto: define objetos para recibir y devolver datos entre las interfaces y la aplicación.
integration/port: contiene interfaces para servicios externos y, si se adopta la separación completa de persistencia, también los contratos correspondientes.
integration/adapter: contiene implementaciones concretas de los puertos, como Gemini y SMTP.
resources/templates: almacena las vistas HTML de Thymeleaf.
resources/static: almacena CSS, JavaScript e imágenes de la interfaz.
application.properties: contiene la configuración de Spring Boot, conexión a base de datos y parámetros externos.
Si se implementa formalmente el puerto de persistencia representado en el diagrama de componentes, deberá ubicarse en un paquete de contratos independiente de su adaptador JPA. Esta separación debe reflejarse en el código y no quedar solamente en el diagrama.
10. Tecnologías del proyecto
Tecnología	Responsabilidad
Java 21	Lenguaje de programación del backend
Spring Boot	Framework principal del sistema
Maven	Gestión de dependencias y construcción
Spring Web	Desarrollo de controladores y servicios HTTP
Spring MVC	Gestión de solicitudes y vistas web
Thymeleaf	Renderizado de interfaces HTML
Spring Data JPA	Persistencia y acceso a datos
MySQL	Base de datos relacional
Bean Validation	Validación declarativa de datos
Gemini API	Servicio externo de clasificación asistida por IA
SMTP	Integración para envío de correos
Postman	Pruebas de API REST
Git y GitHub	Control de versiones
Docker Compose	Despliegue y orquestación local en una etapa posterior
La integración con Gemini y SMTP deberá realizarse mediante adaptadores independientes.
Las credenciales de base de datos, claves API y contraseñas de servicios externos no deben incorporarse directamente en el código fuente ni publicarse en el repositorio.
11. Funcionamiento integral previsto
El funcionamiento general del SGDI puede describirse de la siguiente manera:
1.	El funcionario accede a la aplicación web e inicia sesión.
2.	El sistema gestiona la solicitud de autenticación.
3.	El funcionario introduce la información del documento.
4.	El controlador recibe la solicitud y los datos correspondientes.
5.	El servicio de aplicación coordina la validación y el procesamiento.
6.	La información documental se almacena mediante el contrato y el adaptador de persistencia.
7.	El proceso determina si se utilizará clasificación asistida por IA.
8.	Si se utiliza IA, el servicio solicita una clasificación mediante el puerto correspondiente.
9.	El adaptador Gemini consume la API externa y devuelve el resultado.
10.	El sistema procesa la respuesta y almacena los resultados que correspondan según las reglas aprobadas.
11.	Si no se utiliza IA, se sigue el procedimiento de clasificación manual.
12.	Cuando un caso de uso lo requiera, el sistema podrá enviar notificaciones mediante el puerto y adaptador de correo.
13.	El funcionario podrá consultar la información documental desde la aplicación.
Este flujo integra las responsabilidades mostradas en los diagramas. No obstante, las condiciones de autorización, activación de notificaciones, tratamiento de errores, confirmación de clasificaciones y persistencia de resultados manuales requieren especificaciones adicionales.
12. Primer caso de uso para implementar
CU-01: Registrar y consultar los metadatos de un documento.
Para desarrollar el SGDI de manera progresiva, se propone iniciar con un caso de uso que permita comprobar la arquitectura y la conexión a MySQL.
Descripción
Un funcionario registra la información de un documento mediante los campos obligatorios definidos en el modelo de datos.
El sistema valida la solicitud, almacena el registro y permite consultarlo posteriormente mediante su identificador.
Alcance inicial
Se implementarán:
•	Las entidades necesarias según el SQL aprobado.
•	El repositorio de persistencia.
•	El servicio de aplicación.
•	Los DTO de entrada y salida.
•	Los controladores REST.
•	Las validaciones correspondientes.
•	Las pruebas de funcionamiento con Postman.
En esta primera etapa no se implementarán la carga física de PDF, la clasificación con Gemini ni el envío de notificaciones.
Tampoco debe asumirse que el registro documental puede realizarse sin datos relacionados. Si el modelo requiere referencias a usuarios, categorías u otras entidades, deberán satisfacerse las claves foráneas existentes.
Criterios de aceptación propuestos
•	Una solicitud válida de registro devuelve HTTP 201.
•	Una consulta de documento existente devuelve HTTP 200.
•	Una consulta de identificador inexistente devuelve HTTP 404.
•	Una solicitud con datos inválidos devuelve HTTP 400.
•	La información registrada persiste correctamente en MySQL.
Los campos concretos, las rutas de los endpoints y las validaciones deberán derivarse del script SQL y del contrato API aprobado.
13. Estrategia de desarrollo asistido por IA
El SGDI será desarrollado progresivamente mediante herramientas de inteligencia artificial, utilizando un enfoque de desarrollo guiado por especificaciones.
El proceso será:
Especificar → generar → revisar → ejecutar → probar → aprobar → versionar.
Las herramientas de IA no deberán modificar decisiones arquitectónicas ni introducir funcionalidades sin autorización.
Etapa 1. Base de datos
Revisar el DER, resolver ambigüedades, generar el script SQL y ejecutarlo en MySQL Workbench.
Etapa 2. Arquitectura
Presentar a GitHub Copilot el contexto funcional, los diagramas, el script SQL y las decisiones tecnológicas.
Copilot deberá analizar primero el proyecto y proponer un plan sin generar código.
Etapa 3. Conectividad
Configurar Spring Boot y MySQL mediante las dependencias necesarias y application.properties.
Etapa 4. Primer caso de uso
Implementar progresivamente las entidades, contratos, repositorios, servicios, DTO y controladores REST necesarios.
Etapa 5. Pruebas
Verificar los endpoints mediante Postman y comprobar la persistencia directamente en MySQL.
Etapa 6. Interfaz web
Construir las vistas Thymeleaf y los controladores MVC reutilizando los servicios existentes.
Etapa 7. Inteligencia artificial
Implementar el puerto de clasificación y su adaptador Gemini.
Etapa 8. Notificaciones
Implementar el contrato de notificación y su adaptador SMTP.
Etapa 9. Integración y despliegue
Realizar pruebas integrales, revisar seguridad y preparar el despliegue mediante Docker Compose.
14. Principios y restricciones obligatorias para la IA
Todo asistente de inteligencia artificial que participe en el desarrollo debe respetar las siguientes instrucciones:
1.	Utilizar los diagramas y artefactos aprobados como referencia funcional y arquitectónica.
2.	Utilizar el script SQL aprobado como fuente de verdad para las tablas, columnas y relaciones.
3.	No inventar entidades, atributos, endpoints, roles o reglas de negocio.
4.	Preguntar cuando existan ambigüedades o decisiones pendientes.
5.	Mantener separadas las responsabilidades de presentación, aplicación, dominio, persistencia e integración.
6.	No colocar consultas SQL directamente en los controladores.
7.	Evitar duplicar la lógica de negocio entre MVC y REST.
8.	Aplicar inyección de dependencias por constructor.
9.	Utilizar DTO para definir los contratos de entrada y salida.
10.	No exponer información sensible, como contraseñas o sus representaciones hash.
11.	Mantener las integraciones externas desacopladas mediante interfaces.
12.	No almacenar credenciales ni claves API directamente en archivos versionados.
13.	No modificar automáticamente el esquema SQL aprobado.
14.	Implementar únicamente el caso de uso solicitado en cada etapa.
15.	Presentar primero un plan de cambios cuando se solicite.
16.	Explicar los archivos creados o modificados.
17.	Incorporar validaciones y manejo adecuado de errores.
18.	Preparar pruebas verificables antes de avanzar.
19.	No considerar una funcionalidad terminada únicamente porque el código compile.
20.	Respetar las decisiones y contratos del Módulo 2.
15. Información pendiente de definición
Los diagramas aportan una visión general del sistema, pero no especifican todos los detalles necesarios para implementar sus funcionalidades.
Antes de desarrollar cada módulo deben aclararse, cuando corresponda, los siguientes aspectos:
•	Campos obligatorios y reglas de validación de cada entidad.
•	Contratos y rutas de los endpoints REST.
•	Matriz de permisos de funcionarios y administradores.
•	Método concreto de autenticación.
•	Reglas para seleccionar clasificación manual o asistida.
•	Tratamiento de errores y tiempos de espera de la API de IA.
•	Estructura de la respuesta del clasificador.
•	Significado y escala del valor de confianza.
•	Procedimiento para aceptar, corregir o rechazar una sugerencia.
•	Persistencia de clasificaciones manuales.
•	Condiciones para generar notificaciones.
•	Almacenamiento y protección de archivos documentales.
•	Requisitos de auditoría, trazabilidad y seguridad.
Estos aspectos no deben ser resueltos unilateralmente por Copilot. Deben documentarse como decisiones de diseño o reglas de negocio y ser aprobados antes de implementarse.
16. Instrucción final para el asistente de IA
A partir de este documento, debes asumir el rol de arquitecto y desarrollador senior especializado en Java 21, Spring Boot, MySQL y desarrollo de software asistido por inteligencia artificial.
Tu responsabilidad es ayudar a construir el SGDI de manera incremental, respetando sus requerimientos, diagramas, arquitectura y tecnologías.
Antes de generar código, debes inspeccionar los archivos existentes, identificar las especificaciones aplicables y explicar cómo implementarás el caso de uso solicitado.
No debes asumir que conoces los artefactos del Módulo 2 si no se encuentran disponibles en el proyecto. Debes solicitar los documentos faltantes o indicar las limitaciones.
No debes implementar todo el sistema simultáneamente.
Cada etapa debe producir componentes comprensibles, verificables, probados y coherentes con la arquitectura.
Primera instrucción de trabajo: analiza este contexto, revisa el proyecto Spring Boot y el script SQL disponible, identifica los componentes necesarios para el primer caso de uso y presenta un plan incremental. No generes código hasta recibir autorización.

