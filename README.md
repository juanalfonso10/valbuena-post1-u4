# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción
Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño de Software. Un único proyecto Spring Boot (compras-comportamiento) que resuelve cuatro necesidades reales del backend de ComprasUDES, el sistema interno de solicitudes de compra corporativas: aprobación por niveles jerárquicos, ejecución reversible de solicitudes aprobadas, notificaciones ante cambios de estado y reglas de transición según el estado actual de la solicitud.

## Cómo ejecutar

```bash
mvn clean package
mvn test
mvn spring-boot:run
```

Endpoint: `POST http://localhost:8080/api/solicitudes/evaluar` (200 si se aprueba, 422 si se rechaza).

## Estructura del proyecto

```
src/main/java/com/universidad/compras/
├── modelo/Solicitud.java                       (dado)
├── aprobacion/                                 Necesidad 1 — Chain of Responsibility
│   ├── ServicioAprobacion, ResultadoAprobacion, ControladorSolicitudes   (dados, sin modificar)
│   ├── NivelAprobacionHandler.java             eslabón abstracto
│   ├── CumplimientoNormativoHandler.java       1.º: solo actúa si la categoría es INTERNACIONAL
│   ├── SupervisorHandler.java                  2.º: hasta $2.000.000
│   ├── GerenteAreaHandler.java                 3.º: hasta $10.000.000
│   ├── DirectorFinancieroHandler.java          4.º: sin límite
│   └── ServicioAprobacionImpl.java             arma la cadena e implementa ServicioAprobacion
├── ejecucion/                                  Necesidad 2 — Command
│   ├── PresupuestoService, OrdenCompraService  (dados, sin modificar)
│   ├── OperacionCompraCommand.java             ejecutar / deshacer / getNombre
│   ├── ReservarPresupuestoCommand.java
│   ├── GenerarOrdenCompraCommand.java
│   └── GestorOperacionesEjecucion.java         pila de deshacer + historial completo
├── notificacion/                               Necesidad 3 — Observer
│   ├── ClientesNotificacion                    (dado, sin modificar)
│   ├── SolicitudEstadoObserver.java
│   ├── Correo / ContabilidadDashboard / AuditoriaNotificacionObserver.java
│   └── GestorNotificacionesEstado.java         sujeto: único punto por el que cambia el estado
└── estado/                                     Necesidad 4 — State
    ├── EstadoSolicitud.java
    ├── EstadoPendiente, EnAprobacion, Aprobada, Rechazada, Ejecutada, Cancelada
    └── SolicitudContexto.java
```

## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos
**Patrón aplicado:** Chain of Responsibility.
**Justificación:** El síntoma central es una petición que avanza por una secuencia de decisores independientes hasta que alguien la resuelve. Este patrón enruta la petición permitiendo configurar la cadena dinámicamente. 
**Alternativa descartada:** Command. El problema de encaminar la solicitud por niveles reconfigurables no requiere encapsular acciones reversibles ni historiales, que es el propósito de Command.


**Cómo se arma la cadena:** `ServicioAprobacionImpl` la construye una sola vez: Cumplimiento → Supervisor → Gerente → Director. `CumplimientoNormativoHandler` va siempre primero, pero solo resuelve si la categoría es INTERNACIONAL; cualquier otra solicitud pasa intacta al nivel por monto. Así, `ControladorSolicitudes` (que no se modificó) no sabe cuántos niveles hay, en qué orden se consultan ni que una categoría agrega un nivel. Agregar, quitar o reordenar un nivel es cambiar una línea en `ServicioAprobacionImpl`, sin tocar los demás handlers. Cada nivel que resuelve registra su nombre en `nivelResolutor`.

### Necesidad 2 — Ejecución reversible de solicitudes
**Patrón aplicado:** Command.
**Justificación:** Reservar presupuesto y generar orden de compra son operaciones discretas que deben poder ejecutarse, deshacerse independientemente y registrarse en un historial.
**Alternativa descartada:** Chain of Responsibility. Aquí no hay una secuencia de decisores delegando una petición; son operaciones explícitas que un actor ejecuta y revierte.


**Deshacer y historial:** `GestorOperacionesEjecucion` guarda cada comando en una pila (para deshacer en orden inverso) y en una lista de historial que nunca se vacía, así que cualquier operación puede inspeccionarse después, no solo la última. Deshacer la generación de la orden cancela esa orden y **restaura el estado anterior** de la solicitud (de EJECUTADA a APROBADA) sin liberar el presupuesto ya reservado. `EjecucionSolicitudTest` lo verifica con espías que heredan de los servicios dados, sin modificarlos.

### Necesidad 3 — Notificaciones ante cambio de estado
**Patrón aplicado:** Observer.
**Justificación:** Se requiere que módulos externos ajenos (correo, contabilidad, auditoría) reaccionen automáticamente a los cambios de la solicitud sin acoplar el código que produce el cambio. Observer aplica un modelo publicar-suscribir ideal para esto.
**Alternativa descartada:** State (Necesidad 4). El problema aquí no es cambiar las operaciones permitidas sobre la solicitud misma según su estado interno (dominio de State), sino notificar a módulos de bajo nivel que solo deben enterarse del evento sin interferir en la solicitud.


**Conexión con las demás necesidades:** `GestorNotificacionesEstado` es el único punto por el que cambia el estado de una `Solicitud`, y está conectado a los tres lugares donde ocurre ese cambio:
- `ServicioAprobacionImpl.evaluar` (Necesidad 1): al resolver, la solicitud pasa a APROBADA o RECHAZADA.
- `GenerarOrdenCompraCommand` (Necesidad 2): al ejecutar pasa a EJECUTADA y, al deshacer, vuelve al estado anterior.
- `SolicitudContexto.cambiarEstado` (Necesidad 4): toda transición válida notifica; una operación inválida no cambia el estado y no notifica.

Ninguno de esos puntos conoce el correo, el tablero o la auditoría. Los tests suscriben un cuarto observador (una lambda) sin modificar el gestor y verifican que reacciona en los tres flujos.

### Necesidad 4 — Reglas de transición según el estado
**Patrón aplicado:** State.
**Justificación:** Las operaciones válidas sobre una solicitud dependen de su estado actual. State encapsula el comportamiento de cada estado en su propia clase, eliminando if/else dispersos y permitiendo que el objeto haga sus propias transiciones.
**Alternativa descartada:** Strategy. Aunque estructuralmente similar, Strategy implica que un cliente externo elige e inyecta el algoritmo. Aquí, el objeto determina su propio comportamiento válido según la fase de su ciclo de vida y avanza por sí solo a otros estados, algo que Strategy no contempla.

### Reflexión — otros tres patrones (opcional)
1. **Recorrer secuencialmente sin exponer estructura:** Iterator. Permite acceder a los elementos sin revelar si es una lista o un mapa.
2. **Esqueleto de impresión compartido con cuerpo variable:** Template Method. Define el esqueleto en la superclase y difiere la implementación del cuerpo a las subclases.
3. **Guardar y restaurar instantáneas sin violar encapsulamiento:** Memento. Captura el estado interno (snapshot) sin exponer los atributos de la Solicitud.

## Herramientas utilizadas
- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code / IntelliJ IDEA, Git, GitHub

## Conclusiones
La implementación de estos patrones demuestra cómo resolver problemas de interacción aislando responsabilidades. Distinguir entre patrones con estructuras similares (como State y Strategy) fue fundamental para no forzar soluciones: entender quién controla el cambio (el propio objeto en State, un agente externo en Strategy) es clave para un diseño correcto y sostenible.
