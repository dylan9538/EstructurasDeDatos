DYLAN TORRES
BRYAN PEREZ

Instrucciones de compilación:

INICIA EL PC EN LINUX

Descargar el proyecto y descomprimirlo
Abrir un terminal

Ejecuta los siguientes comandos en el  en el orden siguiente:
1. cd Downloads/

2. cd MonteCarloOpcionCompra
/
3. frascati compile src proyecto 
(Se generará un archivo .jar que va a representar toda la implementación de la solución)
4. frascati run proyectoRMI -libpath proyecto.jar -s run -m run




Se comenzará a desplegar la interfaz de usuario
en donde siguen las instrucciones de uso: 

- Se pueden cargar datos de entrada, los cuales
se se encuentran en la carpeta "data" con el nombre "Datos De Prueba.txt"
(Que esta dentro del proyecto). Inmediatamente después de cargados (
haciendo click en el botón) arroja los resultados de las 
simulaciones en la pantalla del terminal y
guarda un archivo generado.txt con los mismos en la carpeta "data" del proyecto.



- Se pueden ingresar datos por medio de la interfaz de usuario
para hacer la mostrar los resultados en la misma.
Al presionar el boton "calcular", arroja los dos resultados necesarios
que son el S(T) promedio y el rango de la prima
(representado por el Limite Inferior y el Limite
Superios ) en los Labels de la pantalla con el siguiente formato :

	[Limite Inferior, Limite Inferior]

RECORDATORIO :
-LOS DATOS DE ENTRADA Y SALIDA ESTAN EN LA CARPETA "DATA"
-"se muestran todos los decimales para mayor exactitud"
- el composite esta en el src 
- el deployment esta en el word donde se explica la solución