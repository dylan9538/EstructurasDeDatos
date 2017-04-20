package Modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HiloReceptor extends Thread {

	/**Nombre Constante: PETICION_E 
	 * Descripci�n: Es una cadena que env�a un Cliente, la cual funciona como se�al para indicar al Randomico que debe generar
	 * 				una cantidad determinada de n�meros aleatorios
	 * **/
	
	public final static String PETICION_E = "Necesito Calculos";
	
	/**Nombre Constante: ALEATORIO 
	 * Descripci�n: Es una cadena que env�a el Randomico a alg�n Cliente,
	 * 				la cual funciona como se�al para indicarle que ya se enviaron los 
	 * 				n�meros aleatorios que solicit� con anterioridad.
	 * **/
	
	private static final String ALEATORIO = "Llegaron los Numeros";
	
	/**Nombre Constante: SEMILLA 
	 * Descripci�n: Es una cadena que env�a el Servidor al Rand�mico,
	 * 				la cual funciona como se�al para indicarle que debe resetear el objeto Random
	 * 				con una semilla determinada (Que tambien es enviada por el Servidor).
	 * **/
	public final static String SEMILLA = "Semilla";

	private Generador generador;
	private Socket canal;
	private EmisorDeMensajes emisor;

	/**
	 * Constructor de la Clase
	 * **/
	
	public HiloReceptor(Generador generador, Socket canalConUsuario,
			EmisorDeMensajes emisor) {

		this.generador = generador;
		this.canal = canalConUsuario;
		this.emisor = emisor;

	}
	
	/**
	 * Nombre : run
	 * Descripci�n: Aqu� se lleva a cabo el protcolo TCP, respondiendo las diferentes solicitudes de mensajes que se reciben
	 * 				por parte del Servidor o por parte del Cliente
	 * 
	 * **/
	public void run() {

		try {

			String ipEmisor = canal.getInetAddress().getHostAddress();

			BufferedReader lectora = new BufferedReader(new InputStreamReader(
					canal.getInputStream()));

			String mensajeRecibido = lectora.readLine();

			String[] partes = mensajeRecibido.split(",");

			String tipoMensaje = partes[0];

			if (tipoMensaje.equalsIgnoreCase(SEMILLA)) {

				long semilla = Long.parseLong(partes[1]);

				generador.inicializarSemilla(semilla);

				System.out.println("Se inicializo el Generador Con La Semila No. "	+ semilla);
			}

			else if (tipoMensaje.equalsIgnoreCase(PETICION_E)) {
				
				int cantidadRandomicos = Integer.parseInt(partes[1]);
				int s = Integer.parseInt(partes[2]);
				double r = Double.parseDouble(partes[3]);
				int E = Integer.parseInt(partes[4]);
				double sigma = Double.parseDouble(partes[5]);
				int T = Integer.parseInt(partes[6]);

				String respuesta = ALEATORIO + "," + cantidadRandomicos + "," + s + "," + r + "," + E + "," + sigma + "," + T + ",";

				for (int i = 0; i < cantidadRandomicos; i++) {

					respuesta += generador.generarNumero() + ",";
				}
				
				emisor.enviarNumerosAleatorios(respuesta , ipEmisor);
				
				System.out.println("Se enviaron numeros aleatorios al Cliente");


			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
