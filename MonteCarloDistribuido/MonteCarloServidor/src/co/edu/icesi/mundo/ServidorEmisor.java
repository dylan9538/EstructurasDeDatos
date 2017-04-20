package co.edu.icesi.mundo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class ServidorEmisor {

	/**Nombre Constante: PUERTO_CALCULADORA 
	 * Descripci�n: Entero que representa el puerto por donde el Servidor va a enviar mensajes
	 * 				a los Clientes
	 * 	
	 * * **/
	
	public final static int PUERTO_CALCULADORA = 10845; // Puerto por donde puedo enviar informaci�n al cliente

	/**Nombre Constante: NEGACION 
	 * Descripci�n: Es una cadena que le env�a el Servidor a los Clientes para indicarles que no necesita ayuda
	 * * **/
	
	public final static String NEGACION = "No";
	
	/**Nombre Constante: PUERTO_GENERADOR 
	 * Descripci�n: Entero que representa el puerto por donde el Servidor va a enviar mensajes
	 * 				al Randomico
	 * 	
	 * * **/
	
	public final static int PUERTO_GENERADOR = 10900;

	/**Nombre Constante: SEMILLA 
	 * Descripci�n: Es una cadena que le env�a el Servidor al Randomico para indicarles que debe inicializar su Semilla
	 * * **/
	
	public final static String SEMILLA = "Semilla"; // Se lo envio al generador para decirle que incialice su semilla

	private String ipRandomico;
	
	/**
	 * Constructor de la Clase:
	 * Esta clase se encarga de enviar mensajes por TCP a los Clientes y al Randomico
	 * 
	 * **/
		
	public ServidorEmisor(){
		
		try {
						
			 Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			 
			 System.out.println("");
			 
		        for (NetworkInterface netint : Collections.list(nets))
		        	
		        	//if ( ( netint.getDisplayName().contains("Ethernet Connection") ||
		        		//	netint.getDisplayName().contains("Wireless")
		        			//||  netint.getDisplayName().contains("eno") )
		        			//&& Collections.list(netint.getInetAddresses()).size()>0 ){
		           
		        		desplegarInformacion(netint);
		        	
		       // 	}
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Descripci�n: Muestra la direcci�n ip de la maquina donde est� corriendo el Servidor
	 * Nombre: desplegarInformacion
	 * @param: netInt, es la inteface de red que contiene la ip de la maquina
	 * 
	 * **/
	
	 public static void desplegarInformacion(NetworkInterface netint) throws SocketException {
		 
	        System.out.printf("Nombre Conexion: %s\n", netint.getDisplayName());
	        System.out.printf("Name: %s\n", netint.getName());
	     
	        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
	        
	        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	        	
	            System.out.printf("Direccion: %s\n", inetAddress);
	        
	        }
	        
	        System.out.printf("\n");
	     }
	
	/**
	 * Nombre: enviarDatos
	 * Descripci�n: Envia los datos de un call o linea a un cliente para que este haga las simulciones respectivas
	 * @param: datos, son los datos de la peticion (M, sigma, T, etc...)
	 * @param: ipAyudante, es la ip del cliente al que se le encargaron las simulaciones
	 * **/ 
	 
	public void enviarDatos(String datos, String ipAyudante) throws IOException {
	
		InetAddress inet = InetAddress.getByName(ipAyudante);
		
		Socket canal = new Socket(inet, PUERTO_CALCULADORA);
		
		BufferedWriter escritora = new BufferedWriter(new OutputStreamWriter(canal.getOutputStream()));
		
		escritora.write(datos);
		
		escritora.close();
		canal.close();
		
	}
	
	/**
	 * Nombre: enviarNegacion
	 * Descripci�n: Envia un mensaje por TCP a algun cliente para responderle que no necesita ayuda
	 * @param: ipAyudante, es la ip del cliente al que se le va a enviar el mensaje
	 * **/ 	

	public void enviarNegacion(String ipAyudante) throws IOException {
		
		InetAddress inet = InetAddress.getByName(ipAyudante);
		
		Socket canal = new Socket(inet, PUERTO_CALCULADORA);
		
		BufferedWriter escritora = new BufferedWriter(new OutputStreamWriter(canal.getOutputStream()));
		
		escritora.write(NEGACION);
		
		escritora.close();
		canal.close();
		
	}

	/**
	 * Nombre: enviarSemilla
	 * Descripci�n: Envia una semilla al Randomico
	 * @param: semilla, es la semilla con la que se va a inicializar el objeto Random del Randomico
	 * **/ 
	
	public void enviarSemilla(long semilla) throws IOException {
		
		if (semilla != -1){
			
			InetAddress inet = InetAddress.getByName(ipRandomico);
			
			Socket canal = new Socket(inet, PUERTO_GENERADOR);
			
			BufferedWriter escritora = new BufferedWriter(new OutputStreamWriter(canal.getOutputStream()));
			
			String mensaje = SEMILLA + "," + semilla;
			
			escritora.write(mensaje);
			
			escritora.close();
			canal.close();
			
			System.out.println("Una Semilla Fue Enviada: " + semilla);

		}
		
		

			
	}

	public void guardarIpRandomico(String ipRandom) {
		
		System.out.println("Se recibió la ip del Randomico" );
		
		this.ipRandomico = ipRandom;
		
	}
	
	
}
