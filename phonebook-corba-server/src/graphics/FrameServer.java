/**
 * 
 */
package graphics;

import implementation.Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;
import java.awt.Font;

/**
 * @author Lucas Diego Reboucas Rocha
 * @email lucas.diegorr@gmail.com
 * @year 2014
 */
public class FrameServer {

	private JFrame frmPhonebookServer;
	private JTextField textFieldIP;
	private Server server;
	private JTextArea textArea;
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
		frmPhonebookServer.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if ((server != null) && (server.destroy())) {
					JOptionPane.showMessageDialog(null, "Referencia do servidor apagada.");
				}
				System.exit(0);
			}
		});
		frmPhonebookServer.setTitle("PhoneBook Server");
		frmPhonebookServer.setBounds(100, 100, 307, 313);
		frmPhonebookServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPhonebookServer.getContentPane().setLayout(null);

		JPanel panelInformation = new JPanel();
		panelInformation.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Informa\u00E7\u00F5es do Servidor de Nomes", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelInformation.setBounds(7, 11, 276, 96);
		frmPhonebookServer.getContentPane().add(panelInformation);
		panelInformation.setLayout(null);

		JLabel lblServidorDeNomes = new JLabel("Endere\u00E7o do Servidor de Nomes:");
		lblServidorDeNomes.setFont(new Font("Consolas", Font.PLAIN, 11));
		lblServidorDeNomes.setBounds(10, 27, 187, 14);
		panelInformation.add(lblServidorDeNomes);

		textFieldIP = new JTextField();
		textFieldIP.setBounds(196, 24, 70, 20);
		panelInformation.add(textFieldIP);
		textFieldIP.setColumns(10);


		JPanel panelLog = new JPanel();
		panelLog.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Log", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLog.setBounds(7, 108, 276, 156);
		frmPhonebookServer.getContentPane().add(panelLog);
		panelLog.setLayout(null);


		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		textArea.setBounds(6, 16, 408, 133);


		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(6, 21, 262, 124);
		panelLog.add(scrollPane);

		final JButton btnConectar = new JButton("Iniciar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String ip = "localhost", port = "900";

				if ((textFieldIP.getText() == "") || (textFieldIP.getText().isEmpty())) {
					textFieldIP.setText("localhost");
				}else {
					ip = textFieldIP.getText();
				}

				server = new Server(ip, port, textArea);
				if (!server.init()) {
					JOptionPane.showMessageDialog(null, "Impossível encontrar o endereço informado.\nPor favor verifique o endereço informado ou sua conexão com a internet");
					return;
				}else {
					textFieldIP.setEditable(false);
					btnConectar.setEnabled(false);

					new Thread(server).start();
				}
			}
		});
		btnConectar.setBounds(93, 62, 89, 23);
		panelInformation.add(btnConectar);

	}
}
