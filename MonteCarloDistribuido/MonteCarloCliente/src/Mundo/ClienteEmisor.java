package Mundo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClienteEmisor {

	/**Nombre Constante: PUERTO_SERVIDOR 
	 * Descripci�n: Entero que representa el puerto por donde se le van a enviar mensajes TCP al Servidor
	 * **/
	
	public final static int PUERTO_SERVIDOR = 10800; // Es un puerto por donde se env�a informaci�n TCP
	
	/**Nombre Constante: PUERTO_GENERADOR 
	 * Descripci�n: Entero que representa el puerto por donde se le van a enviar mensajes TCP al Randomico
	 * **/
	
	public final static int PUERTO_GENERADOR = 10900; // Puerto por donde envio mensajes
	
	/**Nombre Constante: HOLA 
	 * Descripci�n: Es una cadena que env�a un Cliente al Servidor, la cual funciona como se�al para preguntarle
	 * 				si necesita ayuda para calcular algunas simulaciones S(T)
	 * **/
	
	public final static String HOLA = "Quiero Ayudarte!";
	
	/**Nombre Constante: PETICION_E 
	 * Descripci�n: Es una cadena que env�a un Cliente al Randomico, la cual funciona como se�al para indicarle
	 * 				que debe calcular n�meros aleatorios
	 * **/
	
	public final static String PETICION_E = "Necesito Calculos";

	public String ipServidor, ipRandomico;
	
	/**
	 * Constructor de la Clase: Esta Clase se encarga de enviar diferentes tipos de Mensajes al Randomico y al Servidor
	 * **/
	
	public ClienteEmisor(String ipServidor, String ipGeneradorAleatorios) {
		this.ipServidor =  ipServidor;
		this.ipRandomico = ipGeneradorAleatorios;
	}

	/**
	 * Nombre: enviarSaludo
	 * Descripci�n: Env�a un mensaje al Servidor, para preguntarle si necesita ayuda
	 * @param: None
	 * **/
	
	public void enviarSaludo() throws IOException {
				
		InetAddress inet = InetAddress.getByName(ipServidor);
		
		Socket canal = new Socket(inet, PUERTO_SERVIDOR);
		
		BufferedWriter escritora = new BufferedWriter(new OutputStreamWriter(canal.getOutputStream()));
		
		escritora.write(HOLA + ",");
		
		escritora.close();
		canal.close();
	}
	
	/**
	 * Nombre: enviarDatos
	 * Descripci�n: Env�a varios S(T) calculados que el Servidor hab�a pedido con anterioridad
	 * @param conjuntoDatos : Cadena que representa los diferentes S(T) simulados que se van a enviar
	 * **/

	public void enviarDatos(String conjuntoDatos) throws Exception {
		
		InetAddress inet = InetAddress.getByName(ipServidor);
		
		Socket canal = new Socket(inet, PUERTO_SERVIDOR);
		
		BufferedWriter escritora = new BufferedWriter(new OutputStreamWriter(canal.getOutputStream()));
		
		escritora.write(conjuntoDatos);
		
		escritora.close();
		canal.close();
		
	}

	/**
	 * Nombre: solicitarAleatorios
	 * Descripci�n: Le Env�a un mensaje al Randomico para ordenarle que necesita generar n�meros aleatorios
	 * @param cantidadSTSolicitados : Es la cantidad de Numeros Aleatorias que se le van a pedir al Randomico
	 * @param : s, r , e , sigma, t : Son datos que necesita el Cliente para poder simular varios S(T) con ayuda de
	 * 			los n�meros aleatorios que va a generar el Randomico
	 * **/
	
	public void solicitarAleatorios(int cantidadSolicitados,
			int s, double r, int e, double sigma, int t) throws IOException {
				
		
		String cadena = PETICION_E + "," + cantidadSolicitados + "," + s + "," + r + "," + e + "," + sigma +"," + t;
		
		InetAddress inet = InetAddress.getByName(ipRandomico);
		
		Socket canal = new Socket(inet, PUERTO_GENERADOR);
		
		BufferedWriter escritora = new BufferedWriter(new OutputStreamWriter(canal.getOutputStream()));
		
		escritora.write(cadena);
		
		escritora.close();
		canal.close();
		
	}
	
	
	
}
