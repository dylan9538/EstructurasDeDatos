package Mundo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Calculadora implements Runnable{
	
	private static ClienteEmisor cliente;
	
	private static ServerSocket server; // ServerSocket que me permite establecer una conexi�n con los diferentes mensajes
										// del servidor

	public final static int PUERTO_CALCULADORA = 10845; // Puerto por donde voy a escuchar los mensajes del servidor
	
	private Random randomico;
	
	public Calculadora (){
		
	try {
			
			Scanner in =new Scanner(System.in);
			
			System.out.println("Digite la Ip del Servidor:");
			String ipServidor = in.next();
			
			System.out.println("Digite la Ip del Generador Randomico:");
			String ipGeneradorAleatorios = in.next();
			
			cliente = new ClienteEmisor(ipServidor, ipGeneradorAleatorios);

			in.close();
			
			server = new ServerSocket(PUERTO_CALCULADORA);
			
			cliente.enviarSaludo();
						
			while (true){
				
				Socket canalConServidor = server.accept();
								
				HiloReceptor miHilo = new HiloReceptor(cliente, canalConServidor, this);
				miHilo.start();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}	
	
	public double simularST(int S, int E, double r, double sigma, int T, double NoAzar)  
		
	{
		double STGenerado = -1;
		
		double otrovalor = 0.5* (Math.pow(sigma, 2));
		
		double temp = S* (  Math.exp(  (r- otrovalor)*T + ( sigma*Math.pow(T, 0.5) ) * NoAzar ) );

		STGenerado = Math.exp(-r*T)*Math.max(temp-E,0);
		
		return STGenerado;
		
	}

	public Random getRandomico() {
		return randomico;
	}

	public void setRandomico(Random x) {

		randomico = x;
	}

	@Override
	public void run() {
		
		new Calculadora();
		
	}
	
	/**public static void main(String[] args) {
		new Calculadora();
	}**/
}
