package Mundo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class HiloReceptor extends Thread {

	private Socket canal;
	private Calculadora estimador;
	private ClienteEmisor emisor;

	/**Nombre Constante: NEGACION 
	 * Descripci�n: Es una cadena que env�a el Servidor a un Cliente, la cual funciona como se�al para indicarle
	 * 				que no necesita ayuda para calcular n�metos S(T)
	 * **/
	
	public final static String NEGACION = "No";
	
	/**Nombre Constante: ALEATORIO 
	 * Descripci�n: Es una cadena que env�a un Randomico a un Ciente, la cual funciona como se�al para indicarle
	 * 				que ya llegaron los n�meros aleatorios que solicit� con anterioridad
	 * **/
	
	private static final String ALEATORIO = "Llegaron los Numeros";

	/**Nombre Constante: DATOS 
	 * Descripci�n: Es una cadena que env�a el Servidor a un Ciente, la cual funciona como se�al para indicarle
	 * 				que debe calcular los simulaciones de un n�mero respectivo de S(T)'s junto con los datos anexos (Sigma, T, E, S, etc...)
	 * **/
	
	private static final String DATOS = "Datos Entrada";
	
	/**Nombre Constante: SIMULACIONES 
	 * Descripci�n: Es una cadena que env�a el Cliente al Servidor, la cual funciona como se�al para indicarle
	 * 				que ya se le envi� las N Simulaciones de S(T) que habia pedido con anterioridad	
	 * **/
	
	private static final String SIMULACIONES = "Simulaciones"; //Se lo envio al Servidor cuando le voy a enviar las n simulaciones
															  //pedidas

	/**Nombre Constante: TIEMPO_ESPERA 
	 * Descripci�n: Es un entero que representa el tiempo de espera (en milisegundos) que va a esperar un Cliente para
	 * 				volver a preguntarle al Sevidor si necesita ayuda, en caso de que este ultimo le haya dicho al primero que no estaba ocupado.
	 * **/
	
	public final static int TIEMPO_ESPERA = 5000;
												
	/**
	 * Constructor de la Clase: Esta clase se encarga
	 * 							de responder las diferentes peticiones de los mensajes que le van llegando por TCP 
	 * **/
	
	public HiloReceptor(ClienteEmisor cliente, Socket canalConServidor,
			Calculadora estimadorST) {

		this.canal = canalConServidor;
		this.estimador = estimadorST;
		this.emisor = cliente;
	}

	/**
	 * Nombre: run
	 * Descripci�n: Aqu� va toda la l�gica del protocolo TCP que se aplica para responder a los mensajes que van llegando por parte del Servidor o del Randomico
	 * 
	 * **/
	
	public void run() {

		try {

			BufferedReader lectora = new BufferedReader(new InputStreamReader(
					canal.getInputStream()));

			String respuestaServidor = lectora.readLine();

			String[] partes = respuestaServidor.split(",");

			String tipoMsje = partes[0];

			if (tipoMsje.equalsIgnoreCase(DATOS)) {
				
				int cantidadSTSolicitados = Integer.parseInt(partes[1]);
				int s = Integer.parseInt(partes[2]);
				double r = Double.parseDouble(partes[3]);
				int E = Integer.parseInt(partes[4]);
				double sigma = Double.parseDouble(partes[5]);
				int T = Integer.parseInt(partes[6]);
				
				emisor.solicitarAleatorios(cantidadSTSolicitados, s , r, E, sigma, T);
				
				System.out.println("Se le pidi� al Randomico Una Serie de N�meros Aleatorios!");
				
			}

			else if (tipoMsje.equalsIgnoreCase(ALEATORIO)) {
				
				int cantidadRandomicos = Integer.parseInt(partes[1]);
				int s = Integer.parseInt(partes[2]);
				double r = Double.parseDouble(partes[3]);
				int E = Integer.parseInt(partes[4]);
				double sigma = Double.parseDouble(partes[5]);
				int T = Integer.parseInt(partes[6]);

				String cadenaSimulaciones = SIMULACIONES + ",";
				
				for (int i = 7; i < cantidadRandomicos+7; i++) {

					cadenaSimulaciones += estimador.simularST(s, E, r, sigma, T, Double.parseDouble(partes[i]) ) + ",";
				}
				
				emisor.enviarDatos(cadenaSimulaciones);
				
				
				System.out.println("Se enviaron " + cantidadRandomicos + " simluaciones al Servidor!");
				
				Thread.sleep(6);
				
				emisor.enviarSaludo();
			}

			else if (tipoMsje.equalsIgnoreCase(NEGACION)) {

				System.out
						.println("El Servidor Dice Que Est� Bien, y Que Gracias :D");
				Thread.sleep(TIEMPO_ESPERA);

				emisor.enviarSaludo();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
