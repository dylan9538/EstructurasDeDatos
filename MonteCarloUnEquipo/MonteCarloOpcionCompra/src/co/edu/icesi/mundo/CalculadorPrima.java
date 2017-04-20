package co.edu.icesi.mundo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

import co.edu.icesi.interfaz.VentanaPrincipal;

public class CalculadorPrima implements Runnable {

	private EstimadorST simulador;

	public final static double VALOR_Z = 1.96;
	public final static String RUTA_ARCHIVOS_SALIDA = "./data/";

	public CalculadorPrima() {
		simulador = new EstimadorST();
	}

	public double[] calcularSimulaciones(int S, int E, double r, double sigma, int T, int M, long seed) {

		double[] simulaciones = new double[M];
		
		Random x = new  Random(seed);

		for (int i = 0; i < simulaciones.length; i++) {

			simulaciones[i] = simulador.simularST(S, E, r, sigma, T, x);

		}

		return simulaciones;
	}

	public double calcularSTPromedio(double[] simulacionesST) {

		double promedio = 0.0;
		double sum = 0;

		for (int i = 0; i < simulacionesST.length; i++) {

			sum = sum + simulacionesST[i];
		}

		promedio = sum / simulacionesST.length;

		return promedio;

	}

	public double calcularDesviacionEstandar(double[] simulacionesST, double promedio) {

		double desviacion = 0.0;

		double sumAcu = 0;

		for (int i = 0; i < simulacionesST.length; i++) {

			double resta = simulacionesST[i] - promedio;

			double resta2 = Math.pow(resta, 2);

			sumAcu += resta2;
		}

		double div = sumAcu / simulacionesST.length;

		desviacion = Math.pow(div, 0.5);

		return desviacion;

	}

	public double[] calcularRangoPrima(double STpromedio, double desviacionEstandar, int M) {

		double[] rangoPrima = new double[2];

		rangoPrima[0] = STpromedio - (VALOR_Z * desviacionEstandar) / (Math.pow(M, 0.5));

		rangoPrima[1] = STpromedio + (VALOR_Z * desviacionEstandar) / (Math.pow(M, 0.5));

		return rangoPrima;
	}

	public void generarArchivoConCalculos(File archivo) throws Exception {

		FileReader reader = new FileReader(archivo);
		BufferedReader lector = new BufferedReader(reader);

		String linea = lector.readLine();
		File arhivo2 = new File(RUTA_ARCHIVOS_SALIDA + "generado.txt");
		FileWriter fw = new FileWriter(arhivo2);

		while (linea != null) {

			String[] splitter = linea.split(",");

			String nombre = splitter[0];

			String S = splitter[1];
			String r = splitter[2];
			String E = splitter[3];
			String sigma = splitter[4];

			String M = splitter[5];
			String T = splitter[6];
			String seed = splitter[7];

			long TInicio = System.currentTimeMillis();

			double[] simulaciones = calcularSimulaciones(Integer.parseInt(S), Integer.parseInt(E), Double.parseDouble(r),
					Double.parseDouble(sigma), Integer.parseInt(T), Integer.parseInt(M), Long.parseLong(seed));

			double promedio = calcularSTPromedio(simulaciones);

			double desvEst = calcularDesviacionEstandar(simulaciones, promedio);

			double[] rangoPrima = calcularRangoPrima(promedio, desvEst, Integer.parseInt(M));

			long TFin = System.currentTimeMillis();

			long tiempoProcesamiento = TFin - TInicio;

			String lineaSalida = "";

			lineaSalida += nombre + ",";
			lineaSalida += seed + ",";
			lineaSalida += M + ",";
			lineaSalida += promedio + ",";
			lineaSalida += rangoPrima[0] + ",";
			lineaSalida += rangoPrima[1] + ",";
			lineaSalida += tiempoProcesamiento + "\n";

			
			fw.write(lineaSalida);
			System.out.println(lineaSalida);

			linea = lector.readLine();

		}
		fw.close();

		lector.close();
		reader.close();

	}

	@Override
	public void run() {
		
	System.out.println("Iniciando Server...!");
	 new VentanaPrincipal(this);
	 
	 
	}
	

}
