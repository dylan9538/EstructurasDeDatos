package co.edu.icesi.mundo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HiloReceptor extends Thread {

	private ReceptorDePeticiones receptor;
	private Socket canal;
	private ServidorEmisor emisor;

	/**Nombre Constante: HOLA 
	 * Descripci�n: Es una cadena que env�a un Cliente al Servidor, la cual funciona como se�al para preguntarle
	 * 				si necesita ayuda para calcular algunas simulaciones S(T)
	 * **/	
	
	public final static String HOLA = "Quiero Ayudarte!";
	
	/**Nombre Constante: DATOS 
	 * Descripci�n: Es una cadena que env�a el Servidor a un Ciente, la cual funciona como se�al para indicarle
	 * 				que debe calcular los simulaciones de un n�mero respectivo de S(T)'s junto con los datos anexos (Sigma, T, E, S, etc...)
	 * **/	
	
	private static final String DATOS = "Datos Entrada";

	/**Nombre Constante: SIMULACIONES 
	 * Descripci�n: Es una cadena que env�a el Cliente al Servidor, la cual funciona como se�al para indicarle
	 * 				que ya se le envi� las N Simulaciones de S(T) que habia pedido con anterioridad	
	 * **/
	
	
	private static final String SIMULACIONES = "Simulaciones";

	private final static String AQUI_ESTA_MI_IP = "Soy el Randomico";
	
	
	/**
	 * Constructor de la clase:
	 * Esta se encarga de responde los diferentes mensajes que recibe el Servidor por parte de los Clientes y del Randomico 
	 * 
	 * **/
	
	public HiloReceptor(ReceptorDePeticiones receptor, Socket canalConUsuario,
			ServidorEmisor servidor) {

		this.receptor = receptor;
		this.canal = canalConUsuario;
		this.emisor = servidor;

	}

	/**
	 * Nombre: run
	 * Descripci�n: Aqu� va toda la l�gica del protocolo TCP que se aplica para
	 * 				responder a los mensajes que van llegando por parte de algun Cliente o del Randomico
	 * 
	 * **/
	
	public void run() {

		try {

			String ipAyudante = canal.getInetAddress().getHostAddress();

			BufferedReader lectora;
			lectora = new BufferedReader(new InputStreamReader(
					canal.getInputStream()));

			String mensajeDelAyudante = lectora.readLine();

			String[] partes = mensajeDelAyudante.split(",");

			String tipoMensaje = partes[0];

			if (tipoMensaje.equalsIgnoreCase(HOLA)) {

				if (receptor.estaOcupado()) {

					if (!receptor.semillaFueEnviada()) {

						receptor.cambiarSemillaEnviada(true);
						receptor.configurarFactorEnvios(receptor.darMPeticionActual());
						
						receptor.inicializarTiempoInicio();
						
						emisor.enviarSemilla(receptor.darSemillaPeticionActual());
						Thread.sleep(6);
						
					}

					int cantidadSTSolicitados = receptor.getFraccionM(); 
					
					int s = receptor.darSPeticionActual();
					double r = receptor.darRPeticionActual();
					int E = receptor.darEPeticionActual();
					double sigma = receptor.darSigmaPeticionActual();
					int T = receptor.darTPeticionActual();

					String cadena = DATOS + "," + cantidadSTSolicitados + ","
							+ s + "," + r + "," + E + "," + sigma + "," + T;

					if (s != -1 && r != -1 && E != -1 && sigma != -1 && T != -1) {
						emisor.enviarDatos(cadena, ipAyudante);
						receptor.registrarEnvio();
						
						System.out.println("Se registraron " + cantidadSTSolicitados + " peticiones de Simulacion");

					}
					
				}

				else {
					emisor.enviarNegacion(ipAyudante);
					
					System.out.println("Se le dijo al Ayudante que estoy bien");

				}

			}
			
			else if (tipoMensaje.equalsIgnoreCase(SIMULACIONES)) {
				
				for (int i=1;i<partes.length;i++){
					 receptor.registrarSimulacion(partes[i]);

				}
				
				System.out.println("Se registraron " + (partes.length-1 ) + " simulaciones recibidas");
				
			}
			
			else if (tipoMensaje.equalsIgnoreCase(AQUI_ESTA_MI_IP) ){
				
				emisor.guardarIpRandomico(ipAyudante);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
