# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción
Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño de Software. Un único proyecto Spring Boot (compras-comportamiento) que resuelve cuatro necesidades reales del backend de ComprasUDES.

## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos
**Patrón aplicado:** Chain of Responsibility.
**Justificación:** El síntoma de diseño central es que una solicitud debe recorrer una secuencia de decisores donde cada uno evalúa si tiene la autoridad por monto para resolverla o si debe delegarla al siguiente. Chain of Responsibility permite configurar esta cadena dinámicamente como una lista enlazada. Esto resuelve el problema de la categoría "INTERNACIONAL", ya que podemos insertar el eslabón del RevisorCumplimiento al inicio de la cadena en tiempo de ejecución sin modificar el ControladorSolicitudes ni los demás eslabones.
**Alternativa descartada:** Se descartó el patrón Command porque el problema aquí no es encapsular la aprobación como un objeto para encolarla, auditarla o deshacerla más adelante (reversibilidad). El objetivo es enrutar la petición hasta encontrar quién actúa y devolver un resultado inmediato.

### Necesidad 2 — Ejecución reversible de solicitudes
**Patrón aplicado:** Command.
**Justificación:** El problema exige que reservar presupuesto y generar orden de compra sean operaciones ejecutables, reversibles (deshacer) de forma independiente y consultables en un historial. El patrón Command encapsula cada operación en un objeto con los métodos execute() y undo(), permitiendo que un historial (Invoker) gestione la pila de operaciones.
**Alternativa descartada:** Se descartó Chain of Responsibility porque aquí no hay un flujo condicional de decisores evaluando si aprueban o delegan. Son operaciones discretas que el mismo usuario decide ejecutar, y la cadena de responsabilidad no ofrece un mecanismo natural para deshacer acciones previas ni para mantener un historial consultable.
