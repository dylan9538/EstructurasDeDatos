package co.edu.icesi.mundo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import co.edu.icesi.interfaz.VentanaPrincipal;

public class ReceptorDePeticiones {

	/**Nombre Constante: RUTA_ARCHIVOS_SALIDA 
	 * Descripci�n: Representa una ruta de una carpeta donde se van a guardar los archivos .txt (que contienen resultados)
	 * 				que va a generar el programa
	 * 				cuando el usuario le pida que haga calculos por medio de otro archivo .txt de entrada (Call o Lineas)
	 * 	 * **/
	
	public final static String RUTA_ARCHIVOS_SALIDA = "./data/";

	/**Nombre Constante: VALOR_Z 
	 * Descripci�n: Entero que sirve para hacer calculos estad�sticos con un Intervalo de Confianza de 95%
	 * 	
	 * * **/
	
	public final static double VALOR_Z = 1.96;

	/**Nombre Constante: PUERTO_SERVIDOR 
	 * Descripci�n: Entero que representa el puerto por donde el Servidor va a escuchar multiples mensajes
	 * 				del Randomico o los Clientes
	 * 	
	 * * **/
	
	public final static int PUERTO_SERVIDOR = 10800;

	/**Nombre Constante: VOLUMEN_PETICIONES 
	 * Descripci�n: Entero que representa la cantidad de Simulaciones que (por lo general) se le van a pedir a un Cliente
	 * 
	 * * **/
	
	public final static int VOLUMEN_PETICIONES = 150;

	private static ServidorEmisor servidor; 

	private static ServerSocket server;
	
	/**
	 * Nombre: ventana
	 * Instancia de la Interfaz Gr�fica
	 * **/
	
	private VentanaPrincipal ventana;

	/**
	 * Nombre: cantidadArchivosExportados
	 * Indica el n�mero de archivos .txt exportados desde que se ejecut� el Servidor
	 * **/
	
	private int cantidadArchivosExportados;

	private BufferedReader archivoActual;

	private ArrayList<Peticion> peticiones;

	private boolean semillaEnviada;

	private int cantidadEnviosHechos; 

	private int cantidadEnviosCompletos; 
	
	private int restanteEnvios; 
	
	/**
	 * Constructor de la Clase:
	 * Esta clase se encarga de:
	 * 			- Registrar todas las lineas que se reciben ya sean por medio de la interfaz o de un archivo .txt
	 * 			- Hacer los calculos finales (rango de la prima, desviaci�n, promedio, etc...) de un call cuando una petici�n
	 * 				ya haya recibido los M simulaciones de S(T) que necesitaba
	 * 			- Guardar los resultados que vaya generando o en un archivo .txt o pedir a la interfaz gr�fica que los despliegue
	 * **/			
	
	public ReceptorDePeticiones() {

		cantidadEnviosHechos = 0;

		semillaEnviada = false;

		peticiones = new ArrayList<Peticion>();

		cantidadArchivosExportados = 0;

		System.out.println("Servidor Iniciado...");

		ventana = new VentanaPrincipal(this);

		servidor = new ServidorEmisor();

		try {

			server = new ServerSocket(PUERTO_SERVIDOR);

			while (true) {

				Socket canalConUsuario = server.accept();

				HiloReceptor mihilo = new HiloReceptor(this, canalConUsuario, servidor);
				mihilo.start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Nombre: calcularSTPromedio
	 * Descripci�n: Calcula el promedio de un conjunto de M simulaciones de una Petici�n
	 * @param: simulacionesST, es la lista de las M simulaciones
	 * 
	 * **/
	
	public double calcularSTPromedio(ArrayList<Double> simulacionesST) {

		double promedio = 0.0;
		double sum = 0;

		for (int i = 0; i < simulacionesST.size(); i++) {

			sum = sum + simulacionesST.get(i);
		}

		promedio = sum / simulacionesST.size();

		return promedio;

	}
	
	/**
	 * Nombre: calcularDesviacionEstandar
	 * Descripci�n: Calcula la desviaci�n estandar de un conjunto de Datos, junto con su promedio
	 * @param: simulacionesST, es la lista de las M simulaciones de una Peticion
	 * 			promedio, es el ST promedio de las M simulaciones
	 * **/

	public double calcularDesviacionEstandar(ArrayList<Double> simulacionesST, double promedio) {

		double desviacion = 0.0;

		double sumAcu = 0;

		for (int i = 0; i < simulacionesST.size(); i++) {

			double resta = simulacionesST.get(i) - promedio;

			double resta2 = Math.pow(resta, 2);

			sumAcu += resta2;
		}

		double div = sumAcu / simulacionesST.size();

		desviacion = Math.pow(div, 0.5);

		return desviacion;

	}
	
	/**
	 * Nombre: calcularRangoPrima
	 * Descripci�n: Calcula de forma estad�stica la Prima Minima y la Prima M�xima de una Petici�n
	 * @param: ST Promedio, es el promedio de las M simulaciones de la Petici�n
	 * 			desviaci�n Estandar, es la desviaci�n de las M simluaciones,
	 * 			M, es el n�mero de Simulaciones que se le hicieron a la Petici�n
	 * 
	 * **/

	public double[] calcularRangoPrima(double STpromedio, double desviacionEstandar, int M) {

		double[] rangoPrima = new double[2];

		rangoPrima[0] = STpromedio - (VALOR_Z * desviacionEstandar) / (Math.pow(M, 0.5));

		rangoPrima[1] = STpromedio + (VALOR_Z * desviacionEstandar) / (Math.pow(M, 0.5));

		String limInferior = rangoPrima[0] + "";
		String limSuperior = rangoPrima[1] + "";

		if (limSuperior.length() > 6)
			limSuperior = limSuperior.substring(0, 6);

		if (limInferior.length() > 6)
			limInferior = limInferior.substring(0, 6);

		rangoPrima[0] = Double.parseDouble(limInferior);
		rangoPrima[1] = Double.parseDouble(limSuperior);

		return rangoPrima;
	}
	
	/**
	 * Nombre: guardarArchivo
	 * Descripci�n: Registra los N lineas que contiene el archivo como Peticiones para luego ser atendidas
	 * @param: File archivo, representa al Archivo .txt seleccionado por el usuario, el cual contiene los Call que se van a atender
	 * 
	 * **/

	public void guardarArchivo(File archivo) throws Exception {

		FileReader reader = new FileReader(archivo);
		archivoActual = new BufferedReader(reader);

		String linea = archivoActual.readLine();

		while (linea != null) {

			String[] partes = linea.split(",");

			String nombre = partes[0];
			int s = Integer.parseInt(partes[1]);
			double r = Double.parseDouble(partes[2]);
			int E = Integer.parseInt(partes[3]);
			double sigma = Double.parseDouble(partes[4]);
			int M = Integer.parseInt(partes[5]);
			int T = Integer.parseInt(partes[6]);
			long seed = Long.parseLong(partes[7]);

			guardarPeticionArchivo(M, s, r, E, sigma, T, nombre, seed);

			linea = archivoActual.readLine();
		}

		archivoActual.close();
		reader.close();

	}

	/**
	 * Nombre: darVentana
	 * Descripci�n: retorna el JFrame que representa la interfaz gr�fica
	 * @param: none
	 * 
	 * **/

	
	public VentanaPrincipal darVentana() {
		return ventana;
	};

	
	public static void main(String[] args) {

		new ReceptorDePeticiones();

	}
	
	/**
	 * Nombre: enviarAInterfaz
	 * Descripci�n: Actualiza la interfaz con los resultados obtenidos luego de procesar una Petici�n
	 * @param: promedo, minimoPrima, maximoPrima. Son los resultados finales que se van a desplegar en la interfaz
	 * 
	 * **/

	public void enviarAInterfaz(double promedio, double minimoPrima, double maximoPrima) {
		ventana.actualizarST(promedio);
		ventana.actualizarRangoPrima(minimoPrima, maximoPrima);

	}
	
	/**
	 * Nombre: guardarEnArchivo
	 * Descripci�n: Guardar una linea en un archivo .txt respesctivo, cuidandose que lo que estaba escrito anteriormente
	 * 				no se borre.
	 * @param: lineaSalida, es la liena que se va a escribir dentro del archivo
	 * 
	 * **/

	public void guardarEnArchivo(String lineaSalida) throws IOException {

		File arhivo2 = new File(RUTA_ARCHIVOS_SALIDA + "resultados No. " + cantidadArchivosExportados + ".txt");

		FileWriter fw = new FileWriter(arhivo2, true);

		fw.write(lineaSalida);
		System.out.println("Se va a exportar la linea: " + lineaSalida + "\n");

		fw.close();

	}
	
	/**
	 * Nombre: estaOcupado
	 * Descripci�n: Indica si el Servidor tiene alguna peticion pendiente por atender
	 * @param: None
	 * 
	 * **/

	public boolean estaOcupado() {

		if (peticiones.size() > 0)
			return true;

		else
			return false;

	}
	
	/**
	 * Nombre: extraerDatoInterfaz
	 * Descripci�n: Pide los datos que ingres� el Usuario por medio de los TextField y luego los concatena
	 * 				en un String.
	 * @param: None
	 * 
	 * **/

	public String extraerDatoInterfaz() {

		int S = ventana.darS();
		int E = ventana.darE();
		double r = ventana.darR();
		int T = ventana.darT();
		double sigma = ventana.darSigma();

		String cadenaPeticion = S + "," + r + "," + E + "," + sigma + "," + T;

		return cadenaPeticion;

	}
	
	/**
	 * Nombre: terminarCalculo
	 * Descripci�n: Se ejecuta cuando una Peticion ya ha recibido todas las simulaciones S(T) que necesitaba.
	 * 				Este metodo hace que el Receptor de Peticiones calcule el promedio, la desviaci�n, la prima de 
	 * 				la peticion que est�
	 * 				siendo procesada.
	 * 				Una vez todo calculado, o se muestra los resultados en la interfaz o se guarda en un archivo .txt
	 * 				(dependiendo del origen de la petici�n).
	 * 				Una vez atendida la petici�n, se borra de la lista de peticiones pendientes
	 * @param: None
	 * 
	 * **/

	public void terminarCalculo() {

		try {

			cambiarSemillaEnviada(false);
			
			Peticion admin = peticiones.get(0);
			
			peticiones.remove(0);
			double promedio = calcularSTPromedio(admin.darSimulaciones());

			double desviacionEstandar = calcularDesviacionEstandar(admin.darSimulaciones(), promedio);

			double[] rangoPrima = calcularRangoPrima(promedio, desviacionEstandar, admin.getM());

			admin.registrarTiempoFin();

			if (admin.getNombre().equalsIgnoreCase("")) {

				ventana.actualizarRangoPrima(rangoPrima[0], rangoPrima[1]);
				ventana.actualizarST(promedio);

			}

			else {

				long delta = admin.getTiempoFin() - admin.getTiempoInicio();

				String lineaSalida = admin.getNombre() + " , " + admin.getSeed() + " , " + admin.getM() + " , " + admin.getE() +
						" , "
						+ promedio + " , " + rangoPrima[0] + " , " + rangoPrima[1] + " , "  + delta;

				guardarEnArchivo(lineaSalida);

			}


		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Nombre: enviarSemilla
	 * Descripci�n: Pide al Emisor que envia la Semilla dada como entrada al Randomico por TCP
	 * @param: semilla, la semilla que se va a enviar
	 * 
	 * **/

	public void enviarSemilla(long semilla) throws IOException {

		servidor.enviarSemilla(semilla);
	}

	/**
	 * Nombre: guardarPeticionInterfaz
	 * Descripci�n: guarda la peticion de un Call generado desde la interfaz. A Este tipo de Peticiones siempre se les da
	 * 				prioridad y se guardan en el inicio de la Lista de Peticiones Pendientes 
	 * @param: M, s, r , e , sigma, t ,semilla. Son los datos que se requieren para procesar la linea y luego mostrar
	 * 			los resultados en la interfaz.
	 * 
	 * **/

	public void guardarPeticionInterfaz(int M, int s, double r, int e, double sigma, int t, long semilla) {

		Peticion nuevoK = new Peticion(this, M, s, r, e, sigma, t, semilla);

		peticiones.add(0, nuevoK);

	}
	
	/**
	 * Nombre: guardarPeticionArchivo
	 * Descripci�n: guarda en la lista de Peticiones pendientes una Linea o Call que debe ser procesada para luego
	 * 				ser exportada en un archivo .txt
	 * @param: M, s, r, e, sigma, t. Son los datos que se requieren para calcular la Prima y el S(T) del Call a registrar
	 * 
	 * **/

	public void guardarPeticionArchivo(int M, int s, double r, int e, double sigma, int t, String nombre, long seed) {

		Peticion nuevoK = new Peticion(this, M, s, r, e, sigma, t, seed);

		nuevoK.cambiarNombre(nombre);

		peticiones.add(nuevoK);

	}

	/**
	 * Nombre: getPeticiones
	 * Descripci�n: Retorna la Lista de Peticiones que se deben procesar
	 * @param: None
	 * 
	 * **/
	
	public ArrayList<Peticion> getPeticiones() {
		return peticiones;
	}

	/**
	 * Nombre: cambiarSemillaEnviada
	 * Descripci�n: Actualiza el estado del Receptor De Peticiones. Este metodo se aplica cuando una
	 * 				petici�n ya se termin� de procesar y se va a continuar con una siguiente (Si la hay), entonces
	 * 				el valor se vuelve false, indicando que no se ha enviado le semilla de la petici�n que sigue.
	 * 
	 * 				Tambien sirve cuandoel servidor ya le envi� la semilla al Randomico, entonces este metodo ayuda
	 * 				a impedir que se le envie la misma semilla de nuevo.
	 * @param nValor: el nuevo valor que va a tomar la variable
	 * 
	 * **/
	
	public void cambiarSemillaEnviada(boolean nValor) {
		this.semillaEnviada = nValor;

	}

	/**
	 * Nombre: semillaFueEnviada
	 * Descripci�n: Me indica si ya el Servidor le env�o la Semilla al Randomico para que este se inicialice
	 * @param
	 * 
	 * **/
	
	public boolean semillaFueEnviada() {
		return semillaEnviada;
	}
	
	/**
	 * Nombre: getFraccionM
	 * Descripci�n: Retorna el n�mero de S(T) que se le va a pedir a un cliente que calcule.
	 * @param: None
	 * 
	 * **/

	public int getFraccionM() {

		if (cantidadEnviosHechos < cantidadEnviosCompletos) {
			return VOLUMEN_PETICIONES;
		} else {
			return restanteEnvios;
		}

	}
	
	/**
	 * Nombre: configurarFactorEnv�os
	 * Descripci�n: Calcula el N�mero de Veces (representado con la variable cantidadEnviosCompletos)
	 *				que se va pedir a los Cliente que calculen una cantidad de S(T) representada por la constante VOLUMEN_PETICIONES
	 *				y calcula el restante (representado por la variable restanteEnvios) que se les va a pedir cuando
	 *				ese n�mero ese Envios  (cantidadEnviosHechos) se haya cumplido.
	 * @param: M, la cantidad de Simulaciones que requiere una Petici�n
	 * 
	 * **/

	public void configurarFactorEnvios(int M) {
		
		if (M != -1){
			
			cantidadEnviosHechos = 0;

			cantidadEnviosCompletos = (int) (M / VOLUMEN_PETICIONES);

			restanteEnvios = M % VOLUMEN_PETICIONES;
		}

	}
	
	/**
	 * Nombre: registrarEnvio
	 * Descripci�n: Incrementa la variable cantidadEnviosHechos
	 * @param� None
	 * 
	 * **/

	public void registrarEnvio() {
		cantidadEnviosHechos++;
	}
	
	/**
	 * Nombre: registrarSimulacion
	 * Descripci�n: Registra a la petici� Actual un S(T) recibido por parte de un Cliente
	 * @param None
	 * 
	 * **/

	public void registrarSimulacion(String simulacion) {

		if (!peticiones.isEmpty()) 			
		peticiones.get(0).guardarSimulacion(Double.parseDouble(simulacion));
			
	}

	/**
	 * Nombre: inicializarTiempoInicio
	 * Descripci�n: Registrar el Tiempo de Millisegundos en el que se comenz� a atender la Petici�n Actual.
	 * @param None
	 * 
	 * **/
	
	public void inicializarTiempoInicio() {

		peticiones.get(0).registrarTiempoInicio();

	}
	
	/**
	 * Nombre: darSemillaPeticionActual
	 * Descripci�n: Retorna la Semilla (seed) de la petici�n actual que se est� atendiendo.
	 * 				Si no hay peticiones pendientes, retorna -1
	 * @param: None
	 * 
	 * **/

	public long darSemillaPeticionActual() {

		if (!getPeticiones().isEmpty())
			return getPeticiones().get(0).getSeed();
		else
			return -1;
	}

	/**
	 * Nombre: darSPeticionActual
	 * Descripci�n: Retorna el valor de Mercado (s) de la petici�n actual que se est� atendiendo.
	 * 				Si no hay peticiones pendientes, retorna -1
	 * @param: None
	 * 
	 * **/
	
	public int darSPeticionActual() {
		
		if ( !getPeticiones().isEmpty() )
			return getPeticiones().get(0).getS();
		else
			return -1;
	}
	
	/**
	 * Nombre: darRPeticionActual
	 * Descripci�n: Retorna la Tasa De Interes (r) de la petici�n actual que se est� atendiendo.
	 * 				Si no hay peticiones pendientes, retorna -1
	 * @param: None
	 * 
	 * **/
	
	public double darRPeticionActual() {
		
		if ( !getPeticiones().isEmpty() )
			return getPeticiones().get(0).getR();
		else
			return -1;
	}
	
	/**
	 * Nombre: darEPeticionActual
	 * Descripci�n: Retorna el Valor a Termino (E) de la petici�n actual que se est� atendiendo
	 * 				Si no hay peticiones pendientes, retorna -1

	 * @param: None
	 * 
	 * **/

	public int darEPeticionActual() {
		
		if ( !getPeticiones().isEmpty() )
			return getPeticiones().get(0).getE();
		else
			return -1;
	}
	
	/**
	 * Nombre: darSigmaPeticionActual
	 * Descripci�n: Retorna la Volatibilidad (Sigma) de la petici�n actual que se est� atendiendo
	 * 				Si no hay peticiones pendientes, retorna -1 
	 * @param: None
	 * 
	 * **/
	
	public double darSigmaPeticionActual() {
		
		if ( !getPeticiones().isEmpty() )
			return getPeticiones().get(0).getSigma();
		else
			return -1;
	}
	
	/**
	 * Nombre: darTPeticionActual
	 * Descripci�n: Retorna el Tiempo en A�os (T) de la petici�n actual que se est� atendiendo
	 * 				Si no hay peticiones pendientes, retorna -1
	 * @param: None
	 * 
	 * **/
	
	public int darTPeticionActual() {
		
		if ( !getPeticiones().isEmpty() )
			return getPeticiones().get(0).getT();
		else
			return -1;
	}

	/**
	 * Nombre: darMPeticionActual
	 * Descripci�n: Retorna la Cantidad de Simulaciones (M) de la petici�n actual que se est� atendiendo
	 * 				Si no hay peticiones pendientes, retorna -1
	 * @param: None
	 * 
	 * **/
	
	public int darMPeticionActual() {
		
		if ( !getPeticiones().isEmpty() )
			return getPeticiones().get(0).getM();
		else
			return -1;
	}

	/**
	 * Nombre: incrementarContador
	 * Descripci�n: Aumenta en +1 el n�mero de archivos .txt que el Servidor ha generado
	 * @param: None
	 * 
	 * **/
	
	public void incrementarContador() {
		
		cantidadArchivosExportados++;
		
	}
	
}
