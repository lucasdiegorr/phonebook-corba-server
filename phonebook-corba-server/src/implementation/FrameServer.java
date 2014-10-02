/**
 * 
 */
package implementation;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextArea;

/**
 * @author Lucas Diego Reboucas Rocha
 * @email lucas.diegorr@gmail.com
 * @year 2014
 */
public class FrameServer {

	private JFrame frmPhonebookServer;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textFieldIP;
	private JTextField textFieldPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameServer window = new FrameServer();
					window.frmPhonebookServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FrameServer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPhonebookServer = new JFrame();
		frmPhonebookServer.setTitle("PhoneBook Server");
		frmPhonebookServer.setBounds(100, 100, 450, 313);
		frmPhonebookServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPhonebookServer.getContentPane().setLayout(null);
		
		JPanel panelInformation = new JPanel();
		panelInformation.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Informa\u00E7\u00F5es do Servidor de Nomes", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelInformation.setBounds(4, 11, 426, 96);
		frmPhonebookServer.getContentPane().add(panelInformation);
		panelInformation.setLayout(null);
		
		JLabel lblServidorDeNomes = new JLabel("Servidor de Nomes:");
		lblServidorDeNomes.setBounds(10, 27, 111, 14);
		panelInformation.add(lblServidorDeNomes);
		
		JLabel lblPorta = new JLabel("Porta:");
		lblPorta.setBounds(285, 27, 46, 14);
		panelInformation.add(lblPorta);
		
		textFieldIP = new JTextField();
		textFieldIP.setBounds(109, 24, 136, 20);
		panelInformation.add(textFieldIP);
		textFieldIP.setColumns(10);
		
		textFieldPort = new JTextField();
		textFieldPort.setBounds(322, 24, 95, 20);
		panelInformation.add(textFieldPort);
		textFieldPort.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.setBounds(156, 62, 89, 23);
		panelInformation.add(btnConectar);
		
		JPanel panelLog = new JPanel();
		panelLog.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Log", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLog.setBounds(7, 108, 420, 156);
		frmPhonebookServer.getContentPane().add(panelLog);
		panelLog.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(6, 16, 408, 133);
		panelLog.add(textArea);
		
		
	}
}
