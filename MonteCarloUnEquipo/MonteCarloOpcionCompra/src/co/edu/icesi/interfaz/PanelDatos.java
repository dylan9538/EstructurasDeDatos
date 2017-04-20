package co.edu.icesi.interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelDatos extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private VentanaPrincipal principal;

	public static final String CALCULAR = "Calcular";

	private JPanel ingresarDatos;
	private JPanel resultados;

	private JLabel labNumeroSimulaciones;
	private JLabel labPrecioAcordado;
	private JLabel labValorActualActivo;
	private JLabel labTasaDeInteres;
	private JLabel labVolatilidad;
	private JLabel labTiempoVenticimiento;
	private JLabel labSemilla;

	private JTextField txtNumeroSimulaciones;
	private JTextField txtPrecioAcordado;
	private JTextField txtValorActualActivo;
	private JTextField txtTasaDeInteres;
	private JTextField txtVolatilidad;
	private JTextField txtTiempoVenticimiento;
	private JTextField txtSemilla;

	private JTextField txtPrima;
	private JTextField txtValorFuturoActivo;

	private JButton butCalcular;

	public PanelDatos(VentanaPrincipal v) {

		principal = v;
		setBorder(BorderFactory.createTitledBorder("Datos para simulacion"));

		labValorActualActivo = new JLabel("Valor actual del activo(S):");
		txtValorActualActivo = new JTextField();

		labPrecioAcordado = new JLabel("Strike (E):");
		txtPrecioAcordado = new JTextField();

		labTasaDeInteres = new JLabel("Tasa de interes (r):");
		txtTasaDeInteres = new JTextField();

		labTiempoVenticimiento = new JLabel("Tiempo de vencimiento (T):s");
		txtTiempoVenticimiento = new JTextField();

		labVolatilidad = new JLabel("Volatilidad del activo subyacente:");
		txtVolatilidad = new JTextField();

		labNumeroSimulaciones = new JLabel("Numero de simulaciones(M):");
		txtNumeroSimulaciones = new JTextField();

		labSemilla = new JLabel("Semilla");
		txtSemilla = new JTextField();

		butCalcular = new JButton("Calcular Datos");
		butCalcular.setActionCommand(CALCULAR);
		butCalcular.addActionListener(this);

		txtPrima = new JTextField();
		txtPrima.setEditable(false);

		txtValorFuturoActivo = new JTextField();
		txtValorFuturoActivo.setEditable(false);

		ingresarDatos = new JPanel();
		ingresarDatos.setLayout(new GridLayout(7, 2));

		resultados = new JPanel();
		resultados.setLayout(new GridLayout(3, 1));

		ingresarDatos.add(labValorActualActivo);
		ingresarDatos.add(txtValorActualActivo);

		ingresarDatos.add(labPrecioAcordado);
		ingresarDatos.add(txtPrecioAcordado);

		ingresarDatos.add(labTasaDeInteres);
		ingresarDatos.add(txtTasaDeInteres);

		ingresarDatos.add(labTiempoVenticimiento);
		ingresarDatos.add(txtTiempoVenticimiento);

		ingresarDatos.add(labVolatilidad);
		ingresarDatos.add(txtVolatilidad);

		ingresarDatos.add(labNumeroSimulaciones);
		ingresarDatos.add(txtNumeroSimulaciones);

		ingresarDatos.add(labSemilla);
		ingresarDatos.add(txtSemilla);

		resultados.add(txtPrima);
		resultados.add(txtValorFuturoActivo);
		resultados.add(butCalcular);

		add(ingresarDatos, BorderLayout.WEST);
		add(resultados, BorderLayout.EAST);

	}

	public JTextField getTxtSemilla() {
		return txtSemilla;
	}

	public void setTxtSemilla(JTextField txtSemilla) {
		this.txtSemilla = txtSemilla;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(CALCULAR)) {

			principal.eventoCalcular();
		}

	}

	public JTextField getTxtNumeroSimulaciones() {
		return txtNumeroSimulaciones;
	}

	public void setTxtNumeroSimulaciones(JTextField txtNumeroSimulaciones) {
		this.txtNumeroSimulaciones = txtNumeroSimulaciones;
	}

	public JTextField getTxtPrecioAcordado() {
		return txtPrecioAcordado;
	}

	public void setTxtPrecioAcordado(JTextField txtPrecioAcordado) {
		this.txtPrecioAcordado = txtPrecioAcordado;
	}

	public JTextField getTxtValorActualActivo() {
		return txtValorActualActivo;
	}

	public void setTxtValorActualActivo(JTextField txtValorActualActivo) {
		this.txtValorActualActivo = txtValorActualActivo;
	}

	public JTextField getTxtTasaDeInteres() {
		return txtTasaDeInteres;
	}

	public void setTxtTasaDeInteres(JTextField txtTasaDeInteres) {
		this.txtTasaDeInteres = txtTasaDeInteres;
	}

	public JTextField getTxtVolatilidad() {
		return txtVolatilidad;
	}

	public void setTxtVolatilidad(JTextField txtVolatilidad) {
		this.txtVolatilidad = txtVolatilidad;
	}

	public JTextField getTxtTiempoVenticimiento() {
		return txtTiempoVenticimiento;
	}

	public void setTxtTiempoVenticimiento(JTextField txtTiempoVenticimiento) {
		this.txtTiempoVenticimiento = txtTiempoVenticimiento;
	}

	public JTextField getTxtPrima() {
		return txtPrima;
	}

	public void setTxtPrima(JTextField txtPrima) {
		this.txtPrima = txtPrima;
	}

	public JTextField getTxtValorFuturoActivo() {
		return txtValorFuturoActivo;
	}

	public void setTxtValorFuturoActivo(JTextField txtValorFuturoActivo) {
		this.txtValorFuturoActivo = txtValorFuturoActivo;
	}

	public JButton getBotonCalcular() {
		return butCalcular;
	}

}
