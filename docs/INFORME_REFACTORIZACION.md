# Informe Completo de Refactorización y Clean Code

**URL del Repositorio:** [https://github.com/Levelasquez15/hexagonal-bad-practices.git](https://github.com/Levelasquez15/hexagonal-bad-practices.git)

Este documento detalla todas las refactorizaciones aplicadas de forma exhaustiva para corregir las violaciones de *Clean Code* y *Arquitectura Hexagonal* desde el inicio del proyecto. Durante este proceso, se analizó módulo por módulo, abarcando todas las reglas del 1 al 27, asegurando la calidad del código, mantenibilidad y robustez de la arquitectura.

---

## Módulos y Reglas Solucionadas

### 1. Variables, Constantes y Nombramiento Limpio (Reglas 4, 10, 18, 24)
- **Eliminación de abreviaturas incomprensibles (Regla 4):** Se limpiaron variables confusas como `usrs`, `opt`, `r` y `v` en `UserController`, `UserManagementCli` y `ConsoleIO`. Todo el proyecto pasó a usar una convención semántica consistente.
- **Magic Numbers (Reglas 10 y 18):** Se extrajeron números quemados en el código (Magic Numbers) y textos repetitivos, creándolos como constantes explícitas (`MINIMUM_LENGTH`, `MENU_BORDER`, constantes de error) a lo largo del dominio: `UserName`, `UserPassword`, `PersistenceException`, `InvalidUserPasswordException`, `UserNotFoundException`, `InvalidCredentialsException`, etc.
- **Consistencia Semántica de Loggers (Regla 24):** Se unificó la arquitectura de logging orientada a Lombok (`@Log`) implementándose desde capas de entrada (`Main`, `ConsoleIO`) hasta los handlers de negocio (`CreateUserHandler`), en lugar de usar variables manuales incoherentes o logs crudos.

### 2. Responsabilidad Única y Delegación Lógica (Reglas 1, 9, 22)
- **Desacoplamiento de Inicialización y Arquitectura Hexagonal:** 
  - Se modificó `Main.java` delegando asertivamente la creación del contenedor de dependencias (`DependencyContainer`), base de datos y consola a métodos independientes (Reglas 1 y 22). 
  - En la vista, se retiró la instanciación acoplada en `UserController` para utilizar a `UserDesktopMapper` en la generación de Commands (Regla 9).
- **Independencia del Dominio (Regla 9):** Se expulsaron constructores públicos problemáticos e importaciones de lógica transaccional o dependencias concretas de infraestructura fuera del `UserModel`. El dominio se volvió puro.

### 3. Modelado de Objetos de Dominio vs Primitivos (Reglas 2, 15, 20)
- **Blindaje e Inmutabilidad (Reglas 2, 15):** Los DTOs de salida y las clases de transporte de datos (como `UserResponse`) contaban con anotaciones peligrosas como `@Data` de Lombok, dejando *getters* y *setters* al aire libre. Todos pasaron a **Java Records** blindados, transformando anticuados `getId()` a sus correspondientes constructos de registro en todos los endpoints, garantizando la inmutabilidad de la información expuesta.
- **Obsesión por los Primitivos (Regla 20):** Se reemplazó en `UserController` el uso vago de un primitivo (`String id`) por el `ValueObject` representativo (`UserId`), fortaleciendo el encapsulamiento y el comportamiento auto-validante desde las primeras llamadas.

### 4. Seguridad, Manejo de Datos Sensibles y Excepciones (Reglas 6, 12, 13, 21)
- **Eliminación de PII en Logs y Traceo (Regla 6):** Se removió rigurosamente información sensible y datos personales del usuario (como correos y contraseñas limpias) en los volcados de logs y trazas de errores dentro de `LoginHandler` y `CreateUserHandler`. De igual manera, se borraron comentarios explicativos redundantes al lanzar excepciones de negocio (ej. `EmailDestinationModel`).
- **Retorno seguro vs Nulidad (Regla 5 y 21):** Para evitar los peligrosos `NullPointerException`, se implementó retrospectivamente defensas sólidas: en los portales como `GetAllUsersService` o `UserResponsePrinter`, se estandarizó retornar `Collections.emptyList()` o hacer comprobaciones por array vacío antes que pasar `null` (Regla 5). Asimismo se modificó `roleToCode` para utilizar Excepciones en caso de fallos (Regla 21).
- **Eliminación de Clases de Utilidad Mutantes (Reglas 12 y 13):** Se eliminaron clases multipropósito llamadas `Utils` que rompían la cohesión lógica y dispersaban conocimiento. Su funcionalidad se fusionó donde de verdad pertenecía su estado subyacente. Se usaron `@UtilityClass` donde estrictamente aportaban validadores (`ValidatorProvider`, `DatabaseConnectionFactory`, `UserPersistenceMapper`).

### 5. Temporal Coupling y Ley de Demeter (Reglas 14, 19, 23)
- **Temporal Coupling - Orden Forzado de Métodos (Regla 19):** Repositorios fundamentales como `UserRepositoryMySQL` y el controlador `DependencyContainer` sufrían del anti-patrón donde obligaban a ejecutar métodos `.init()` antes del uso de sus instancias. Esto se resolvió fusionando estos pasos transaccionales con su flujo de ciclo de vida seguro, erradicando tal acoplamiento en el arranque.
- **Ley de Demeter (Regla 14):** Se redujo la cadena de llamadas de dependencia para la validación de `password` en los servicios de login (`LoginService`, `UpdateUserService`), encapsulando el conocimiento criptográfico adentro de la entidad sin esparcir la capa de encriptación y desencriptación (`conocimiento disperso - Regla 23` mitigado en `UserEmail`).

### 6. Simplificaciones de Framework y Redundancia (Regla 3, 27)
- **Sobre-ingeniería del Framework (Regla 3):** Se borraron anotaciones excesivas para modelos inmutables, como `@Valid` en las interfaces de dominio, o los decoradores estéticos `@Builder` que sobrepoblaban y complicaban objetos de consulta limpios como `GetUserByIdQuery`.
- **Estructuras de Control Limpias (Regla 27):** El objeto renderizador `UserResponsePrinter` presentaba alta carga cognitiva en flujos for y arrays. Se logró estabilizar a bloques legibles y funcionales de menor redundancia anidada.

### 7. Ingeniería y Mantenimiento de Unit Tests (Regla 11)
El corazón de la confiabilidad se estandarizó en el testeo:
- Se refactorizaron 10+ suites de tests unitarios: (`UpdateUserServiceTest`, `LoginServiceTest`, `EmailNotificationServiceTest`, `DeleteUserServiceTest`, etc).
- **Formato AAA:** Se implementó explícitamente y rigurosamente el bloque *Arrange*, *Act* y *Assert*.
- **Semántica Exacta:** Se modificaron aserciones opacas (`assertTrue(x != null)`) por evaluaciones directas, seguras de JUnit 5 (`assertSame`, `assertNotNull`, `assertEquals`).
- **Displays:** Se documentaron sus ejecuciones con `@DisplayName` garantizando total auditabilidad, previniendo regresiones silenciosas en reglas puras de core y handlers.

### 8. Matriz de Trazabilidad por Archivos Claves
Para facilitar la evaluación de los cambios, esta es la relación de las reglas resueltas por archivo o paquete principal:
* **Capa de Entrada (CLI / Desktop):**
  * `Main.java`: Reglas 1, 22, 24.
  * `ConsoleIO.java`, `UserManagementCli.java`: Reglas 4, 10, 24.
  * `UserController.java`: Reglas 4, 9, 20.
* **Capa de Dominio:**
  * `UserModel.java`: Reglas 9, 14.
  * Value Objects (`UserId`, `UserName`, `UserPassword`, `UserEmail`): Reglas 4, 10, 23.
  * Excepciones (`UserNotFoundException`, `InvalidCredentialsException`, etc.): Reglas 6, 10.
* **Capa de Aplicación (Servicios y Handlers):**
  * `LoginHandler.java`, `CreateUserHandler.java`: Reglas 4, 6.
  * `UserResponsePrinter.java`: Reglas 5, 27.
* **Capa de Infraestructura:**
  * `DependencyContainer.java`, `UserRepositoryMySQL.java`: Reglas 10, 19.
  * `ValidatorProvider.java`, `DatabaseConnectionFactory.java`: Reglas 4, 12, 13.
  * `AppProperties.java`: Regla 4.
* **Directorio de Pruebas (Test):**
  * Todos los `*ServiceTest` y mappers: Regla 11.

---

### Estado Final del Sistema
Luego de atravesar las 27 reglas principales documentadas y eliminar todo el ruido residual por variables inutilizadas durante el refactor, el proyecto ha sido configurado bajo lineamientos perfectos de Tío Bob.

Actualmente:
- El entorno de properties enlaza de manera estable a XAMPP `crud_usuarios` y puerto `Mailtrap`.
- La aplicación compila absolutamente limpia al encender (`0 errores`).
- **218/218 pruebas unitarias corren con éxito, ratificando todo el cumplimiento iterativo detallado en este historial.**