package co.edu.icesi.interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PanelArchivos extends JPanel implements ActionListener {


	private static final long serialVersionUID = 1L;

	public final static String CARGAR = "Cargar";

	private VentanaPrincipal principal;
	private JButton butCargar;

	public PanelArchivos(VentanaPrincipal v) {

		principal = v;
		setLayout(new GridLayout(1, 1));
		setBorder(BorderFactory.createTitledBorder("Cargar datos para calcular"));

		butCargar = new JButton("Cargar datos para simulacion");
		butCargar.setActionCommand(CARGAR);
		butCargar.addActionListener(this);
        
		add(butCargar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(CARGAR)){
			JFileChooser directorio = new JFileChooser();
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("txt", "txt");
		    directorio.setFileFilter(filtro);
			
		    int respuesta = directorio.showOpenDialog(principal);
			if (respuesta == JFileChooser.APPROVE_OPTION) {
				File escogido = directorio.getSelectedFile();
				
				principal.eventoCalcular(escogido);
			
			}
			
		}

	}

	public JButton getButCargar() {
		return butCargar;
	}

	public void setButCargar(JButton butCargar) {
		this.butCargar = butCargar;
	}

}
