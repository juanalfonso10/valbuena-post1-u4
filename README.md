# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción
Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño de Software. Un único proyecto Spring Boot (compras-comportamiento) que resuelve cuatro necesidades reales del backend de ComprasUDES, el sistema interno de solicitudes de compra corporativas: aprobación por niveles jerárquicos, ejecución reversible de solicitudes aprobadas, notificaciones ante cambios de estado y reglas de transición según el estado actual de la solicitud.

## Cómo ejecutar

    $ mvn clean package
    $ mvn spring-boot:run
    $ mvn test

## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos
**Patrón aplicado:** Chain of Responsibility.
**Justificación:** El síntoma central es una petición que avanza por una secuencia de decisores independientes hasta que alguien la resuelve. Este patrón enruta la petición permitiendo configurar la cadena dinámicamente. 
**Alternativa descartada:** Command. El problema de encaminar la solicitud por niveles reconfigurables no requiere encapsular acciones reversibles ni historiales, que es el propósito de Command.

### Necesidad 2 — Ejecución reversible de solicitudes
**Patrón aplicado:** Command.
**Justificación:** Reservar presupuesto y generar orden de compra son operaciones discretas que deben poder ejecutarse, deshacerse independientemente y registrarse en un historial.
**Alternativa descartada:** Chain of Responsibility. Aquí no hay una secuencia de decisores delegando una petición; son operaciones explícitas que un actor ejecuta y revierte.

### Necesidad 3 — Notificaciones ante cambio de estado
**Patrón aplicado:** Observer.
**Justificación:** Se requiere que módulos externos ajenos (correo, contabilidad, auditoría) reaccionen automáticamente a los cambios de la solicitud sin acoplar el código que produce el cambio. Observer aplica un modelo publicar-suscribir ideal para esto.
**Alternativa descartada:** State (Necesidad 4). El problema aquí no es cambiar las operaciones permitidas sobre la solicitud misma según su estado interno (dominio de State), sino notificar a módulos de bajo nivel que solo deben enterarse del evento sin interferir en la solicitud.

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
