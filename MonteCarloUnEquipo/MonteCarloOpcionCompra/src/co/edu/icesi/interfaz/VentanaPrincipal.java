package co.edu.icesi.interfaz;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import co.edu.icesi.mundo.CalculadorPrima;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private static CalculadorPrima principal;
	private PanelDatos panelDatos;
	private PanelArchivos panelArchivos;

	public VentanaPrincipal(CalculadorPrima p) {
		principal = p;

		setSize(750, 300);
		setTitle("Simulador Monte Carlo");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		panelDatos = new PanelDatos(this);
		add(panelDatos, BorderLayout.NORTH);

		panelArchivos = new PanelArchivos(this);
		add(panelArchivos, BorderLayout.SOUTH);

		setVisible(true);
	}

	public JButton darBotonCalcular() {

		return panelDatos.getBotonCalcular();
	}

	public JButton darBotonCargarArchivo() {

		return panelArchivos.getButCargar();
	}

	public int darS() {

		return Integer.parseInt(panelDatos.getTxtValorActualActivo().getText());
	}

	public int darE() {

		return Integer.parseInt(panelDatos.getTxtPrecioAcordado().getText());
	}

	public double darR() {

		return Double.parseDouble(panelDatos.getTxtTasaDeInteres().getText());
	}

	public int darT() {

		return Integer.parseInt(panelDatos.getTxtTiempoVenticimiento()
				.getText());
	}

	public double darSigma() {

		return Double.parseDouble(panelDatos.getTxtVolatilidad().getText());
	}

	public int darM() {

		return Integer
				.parseInt(panelDatos.getTxtNumeroSimulaciones().getText());
	}

	public File seleccionarArchivo() {

		File escogido = null;
		JFileChooser directorio = new JFileChooser();
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("txt",
				"txt");
		directorio.setFileFilter(filtro);

		int respuesta = directorio.showOpenDialog(this);
		if (respuesta == JFileChooser.APPROVE_OPTION) {
			escogido = directorio.getSelectedFile();

		}

		return escogido;
	}

	public void actualizarST(double promedio) {

		panelDatos.getTxtValorFuturoActivo().setText(promedio + "");

	}

	public void actualizarRangoPrima(double limInferior, double limSuperior) {

		panelDatos.getTxtPrima().setText(
				"[" + limInferior + " , " + limSuperior + "]");

	}

	public void eventoCalcular(File escogido) {

		try {
			principal.generarArchivoConCalculos(escogido);
			System.out.println("Archivo Generado!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void eventoCalcular() {

		int S = darS();
		int E = darE();
		double r = darR();
		int T = darT();
		double sigma = darSigma();
		int M = darM();
		long seed = darSemilla();

		double[] simulaciones = principal.calcularSimulaciones(S, E, r, sigma,
				T, M, seed);

		double promedio = principal.calcularSTPromedio(simulaciones);

		double desvEst = principal.calcularDesviacionEstandar(simulaciones,
				promedio);

		double[] rangoPrima = principal.calcularRangoPrima(promedio, desvEst,
				simulaciones.length);

		actualizarST(promedio);
		actualizarRangoPrima(rangoPrima[0], rangoPrima[1]);

		System.out.println("Datos Calculados!");

	}

	private long darSemilla() {

		return Long.parseLong(panelDatos.getTxtSemilla().getText());
	}

}
