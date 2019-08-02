## Heartbeat

Se me ocurrió hacer este ejercicio después de probar el funcionamiento del keepalived. 

Supongamos que una aplicación se ejecuta en dos nodos como activo y pasivo (maestro y esclavo), y sólo el nodo maestro debe ejecutar las tareas en un esquema de alta disponibilidad, en caso que el nodo maestro deje de estar disponible, el nodo esclavo debe cambiar su estado a activo (maestro) y comenzar a ejecutar las tareas.

### Funcionamiento

Cuando la aplicación inicia, se establece el modo maestro o esclavo dependiendo de la disponibilidad de la otra instancia. La aplicación se comunica enviando pedidos REST, que en principio sólo los envía el esclavo (latidos). Si el maestro no contesta una cantidad de veces, el nodo esclavo pasa a estado maestro. 

Finalmente, dependiendo del modo o estado actual de la aplicación (maestro o esclavo) se puede parametrizar una acción a realizar sobre las tareas.  

### Validaciones

El intervalo entre pedidos, el tiempo máximo de respuesta y la cantidad de respuestas erróneas son configurables, con rangos de valores mínimos y máximos. 
