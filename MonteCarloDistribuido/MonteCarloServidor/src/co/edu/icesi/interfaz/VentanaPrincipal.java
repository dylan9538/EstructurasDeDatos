package co.edu.icesi.interfaz;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import co.edu.icesi.mundo.ReceptorDePeticiones;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private static ReceptorDePeticiones principal;
	private PanelDatos panelDatos;
	private PanelArchivos panelArchivos;
	
	public final static String MARCA_INTERFAZ = "Interfaz";

	public VentanaPrincipal(ReceptorDePeticiones p) {
		principal = p;

		setSize(750, 300);
		setTitle("Simulador Monte Carlo");
		setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		panelDatos = new PanelDatos(this);
		add(panelDatos, BorderLayout.NORTH);

		panelArchivos = new PanelArchivos(this);
		add(panelArchivos, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);

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

		panelDatos.getTxtValorFuturoActivo().setText( (promedio + "" ).substring(0,6));

	}

	public void actualizarRangoPrima(double limInferior, double limSuperior) {

		String min = (limInferior + "").substring(0, 6);
		String max = (limSuperior + "").substring(0, 6);
		
		panelDatos.getTxtPrima().setText(
				"[" + min + " , " + max + "]");

	}

	public void eventoCalcular(File escogido) {

		try {
						
			principal.guardarArchivo(escogido);
			
			principal.incrementarContador();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void eventoCalcular() {
		
		principal.guardarPeticionInterfaz(darM(), darS() ,darR(), darE(), darSigma(), darT(), darSemilla());
		
	}

	public long darSemilla() {

		return Long.parseLong(panelDatos.getTxtSemilla().getText());
	}
	
	
}
