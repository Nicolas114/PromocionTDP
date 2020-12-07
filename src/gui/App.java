package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.PluginApp;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class App extends JFrame {

	//a fin de evitar números que puedan no quepar en el espacio del resultado
	protected static final double MAXIMO_VALOR = 0x0999999;
	private PluginApp pluginLogic;
	private JPanel contentPane;
	private JTextField primertextField;
	private JTextField segundotextField;
	private JTextPane textPaneResultado;
	private JButton btnCalcular;
	private JButton btnActualizar;
	private JComboBox<String> listaPlugins;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App() {

		pluginLogic = new PluginApp();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		primertextField = new JTextField();
		primertextField.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
		primertextField.setBounds(12, 60, 114, 38);
		contentPane.add(primertextField);
		primertextField.setColumns(10);

		segundotextField = new JTextField();
		segundotextField.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
		segundotextField.setBounds(138, 60, 114, 38);
		contentPane.add(segundotextField);
		segundotextField.setColumns(10);


		btnActualizar = new JButton("Actualizar");
		btnActualizar.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		btnActualizar.setBounds(284, 58, 117, 40);
		contentPane.add(btnActualizar);

		listaPlugins = new JComboBox<>();
		listaPlugins.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		listaPlugins.setBounds(12, 24, 240, 24);
		contentPane.add(listaPlugins);


		textPaneResultado = new JTextPane();
		textPaneResultado.setEditable(false);
		textPaneResultado.setBounds(182, 110, 219, 25);
		contentPane.add(textPaneResultado);

		btnCalcular = new JButton("Calcular resultado:");
		btnCalcular.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		btnCalcular.setBounds(9, 110, 173, 25);
		contentPane.add(btnCalcular);


		configListaOpciones(listaPlugins);
		configBtnCalcular(btnCalcular, primertextField, segundotextField, textPaneResultado, listaPlugins);
		configBtnActualizar(btnActualizar);
	}

	private void configBtnActualizar(JButton a) {
		a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pluginLogic.actualizarPlugins();
				configListaOpciones(listaPlugins);
			}
		});
	}

	private void configListaOpciones(JComboBox<String> lp) {
		List<String> lista = pluginLogic.getPluginsNames();

		//solucionar problema de que se repiten las opciones a elegir (porque no detecto cuales estan repetidas)
		for (String s : lista) {
			lp.addItem(s);
		}
	}

	private void configBtnCalcular(JButton b, JTextField tf1, JTextField tf2, JTextPane res, JComboBox<String> lp) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String currentOption = (String) lp.getSelectedItem();
					double n1 = Double.parseDouble(tf1.getText());
					double n2 = Double.parseDouble(tf2.getText());
					double result = pluginLogic.runPlugin(n1, n2, currentOption);
					if (result > MAXIMO_VALOR) {
						res.setText("Incalculable");
					}
					else {
						res.setText(String.format("%.8f", result));
					}
				}
				catch (NumberFormatException err) {
					String message = "¡Ingrese un número válido!";
					JOptionPane.showMessageDialog(contentPane, message);
				}
			}
		});
	}
}
