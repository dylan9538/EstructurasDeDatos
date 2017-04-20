package Modelo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Generador implements Runnable{

	/**Nombre Constante: PUERTO_GENERADOR 
	 * Descripci�n: Entero que representa el puerto por donde el Randomico va a escuchar los mensajes TCP que le env�an el Servidor y los Clientes
	 * **/

	public final static int PUERTO_GENERADOR = 10900;
	
	private EmisorDeMensajes emisor;
	private ServerSocket server;
	
	private Random generadorNumeros;
	
	/**
	 * Constructor de la Clase
	 * Descripci�n: Inicializa un objeto de la clase EmisorMensajes y se encarga de llamar el c�digo necesario para que el GeneradorRandomico 
	 * 				comience a escuchar los diferentes mensajes TCP (Por medio de un Hilo)
	 * **/
	
	public Generador (){
		
		try {
			
			Scanner in =new Scanner(System.in);
			
			System.out.println("Digite la Ip del Servidor:");
			String ipServidor = in.next();
			
			in.close();
			
			System.out.println("Randomico Iniciado...");

			emisor = new EmisorDeMensajes(ipServidor);
			
			emisor.enviarSaludo();
			
			server = new ServerSocket(PUERTO_GENERADOR);
			
			while (true){
				
				Socket canalConUsuario = server.accept();
								
				HiloReceptor mihilo= new HiloReceptor(this, canalConUsuario, emisor); 
				mihilo.start();
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	 
	/**
	 * Nombre: inicializarSemilla
	 * @param: semilla = Es un long que representa la semilla con la cual se va a inicializar el objeto Random de la clase Generador.
	 * Descripci�n: Inicializa un objeto Random con la semilla que se le pasa como entrada
	 * **/
	
	public void inicializarSemilla(long semilla) {

		generadorNumeros = new Random(semilla);
	}

	/**
	 * Nombre: generarNumero
	 * Descripci�n: Genera un n�mero pseudo aleatorio que cumpla con la Distribuci�n Normal, cuidando su longitud no sea mayor a 5 caracteres
	 * 				(incluyendo parte entera, decimal y el punto que los separa)
	 * **/
	
	public String generarNumero() {
		
		String numero = generadorNumeros.nextGaussian() + "";
		
		if (numero.length() > 5)
			numero.substring(0,5);
		
		return numero;
	}

	public void run() {
		new Generador();
		
	}
	
/**	public static void main (String[] args){
		new Generador();
	}**/

	
}
