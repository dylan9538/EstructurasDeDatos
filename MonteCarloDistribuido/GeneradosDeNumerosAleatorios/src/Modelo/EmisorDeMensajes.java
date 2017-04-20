package Modelo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

public class EmisorDeMensajes{
	
	/**Nombre Constante: PUERTO_CALCULADORA 
	 * Descripci�n: Entero que representa el puerto por donde se le van a enviar mensajes TCP al Cliente/Calculadora
	 * **/	
	
	public final static int PUERTO_CALCULADORA = 10845;

	public final static int PUERTO_SERVIDOR = 10800;

	private final static String AQUI_ESTA_MI_IP = "Soy el Randomico";
	
	/**
	 * Nombre: EmisorMensajes
	 * Descripci�n: Es el constructor de la Clase. Esta clase se encarga de enviar mensajes por medio de TCP
	 * 
	 * **/
	
	private String ipServidor;
	
	public EmisorDeMensajes(String ipServidor) throws UnknownHostException, SocketException{
		
		this.ipServidor = ipServidor;
		
		try {
			
			 Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			 
			 System.out.println("");
			 
		        for (NetworkInterface netint : Collections.list(nets))
		        	
		        	if ( ( netint.getDisplayName().contains("Ethernet Connection") ||  netint.getDisplayName().contains("Wireless")
		        			||  netint.getDisplayName().contains("eno" +""))
		        			&& Collections.list(netint.getInetAddresses()).size()>0 ){
		           
		        		desplegarInformacion(netint);
		        	
		        	}
			
		} catch (SocketException e) {
			e.printStackTrace();
		}

  
        
	}
	
	/**
	 * Nombre: desplegarInformacion
	 * @param: netInt = Es la interfaz que contiene la direcci�n ip de la maquina donde est� corriendo el GeneradorRandomico
	 * Descripci�n: Muestra por consola la direcci�n ip de la maquina donde est� corriendo el proyecto.
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
	 * Nombre: enviarNumerosAleatorios
	 * @param: listaNumeros = Es un cadena con los n�meros generados por el m�todo .NextGaussian() del Random.
	 * @param ipCliente = Es la direcci�n ip del Cliente al que le voy a env�ar la lista de N�meros por TCP
	 * Descripci�n: Env�a a un cliente un conjunto de N�meros Pseudo-Aleator�os por medio del protcolo TCP
	 * **/
	
	public void enviarNumerosAleatorios(String listaNumeros, String ipCliente) throws IOException {

		InetAddress inet = InetAddress.getByName(ipCliente);
		
		Socket canal = new Socket(inet, PUERTO_CALCULADORA);
		
		BufferedWriter escritora = new BufferedWriter(new OutputStreamWriter(canal.getOutputStream()));
		
		escritora.write(listaNumeros);
		
		escritora.close();
		canal.close();
		
	}

	public void enviarSaludo() throws IOException{		
		
		InetAddress inet = InetAddress.getByName(ipServidor);
				
		Socket canal = new Socket(inet, PUERTO_SERVIDOR);
		
		System.out.println("Se envió la mi ip al servidor!");
		
		BufferedWriter escritora = new BufferedWriter(new OutputStreamWriter(canal.getOutputStream()));
		

		escritora.write(AQUI_ESTA_MI_IP + ",");
		
		escritora.close();
		canal.close();
	}
}
