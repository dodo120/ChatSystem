import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class Test extends JFrame {

	private JPanel contentPane;
	private JTextField SendTF;
	private JTextField PseudoTF;
	private JTextPane RecieveTF;
	private Client client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test frame = new Test();
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
	public Test() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 320);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(51, 51, 51));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Received Message");
		lblNewLabel.setForeground(new Color(70, 130, 180));
		lblNewLabel.setBounds(49, 47, 117, 16);
		contentPane.add(lblNewLabel);
		
		RecieveTF = new JTextPane();
		RecieveTF.setBounds(16, 80, 192, 186);
		contentPane.add(RecieveTF);
		
		JPanel panel = new JPanel();
		panel.setBounds(220, 0, 4, 272);
		contentPane.add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(220, 131, 230, 3);
		contentPane.add(panel_1);
		
		JLabel lblNewLabel_1 = new JLabel("Pseudo");
		lblNewLabel_1.setForeground(new Color(70, 130, 180));
		lblNewLabel_1.setBounds(7, 14, 61, 16);
		contentPane.add(lblNewLabel_1);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Perrine");
		chckbxNewCheckBox.setForeground(new Color(255, 255, 255));
		chckbxNewCheckBox.setBounds(277, 43, 128, 23);
		contentPane.add(chckbxNewCheckBox);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Matthieu");
		chckbxNewCheckBox_1.setForeground(new Color(255, 255, 255));
		chckbxNewCheckBox_1.setBounds(277, 80, 128, 23);
		contentPane.add(chckbxNewCheckBox_1);
		
		JLabel lblNewLabel_2 = new JLabel("Message");
		lblNewLabel_2.setForeground(new Color(70, 130, 180));
		lblNewLabel_2.setBounds(315, 146, 61, 16);
		contentPane.add(lblNewLabel_2);
		
		SendTF = new JTextField();
		SendTF.setToolTipText("Type your message...");
		SendTF.setBounds(236, 174, 208, 60);
		contentPane.add(SendTF);
		SendTF.setColumns(10);
		
		JButton btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.sendMessage(SendTF.getText());
			}
		});
		btnSend.setForeground(new Color(70, 130, 180));
		btnSend.setBackground(new Color(204, 204, 204));
		btnSend.setBounds(288, 237, 117, 29);
		contentPane.add(btnSend);
		
		PseudoTF = new JTextField();
		PseudoTF.setBounds(49, 9, 96, 26);
		contentPane.add(PseudoTF);
		PseudoTF.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("Receivers");
		lblNewLabel_1_1.setForeground(new Color(70, 130, 180));
		lblNewLabel_1_1.setBounds(303, 14, 61, 16);
		contentPane.add(lblNewLabel_1_1);
		
		JButton btnSend_1 = new JButton("Valider");
		btnSend_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Socket socket = new Socket("localhost",1234);
					client = new Client(socket, PseudoTF.getText(), Test.this);
					client.listenForMessage();
					//client.sendMessage("");
					RecieveTF.setText("connexion réussite");
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					RecieveTF.setText(e1.toString());
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					RecieveTF.setText(e1.toString());
					e1.printStackTrace();
				}
			}
		});
		btnSend_1.setForeground(new Color(70, 130, 180));
		btnSend_1.setBackground(new Color(204, 204, 204));
		btnSend_1.setBounds(150, 8, 67, 29);
		contentPane.add(btnSend_1);
	}
	
	public void setMessage(String messageRecieved) {
		System.out.println("baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa;"+messageRecieved);
		RecieveTF.setText(RecieveTF.getText()+"\n"+messageRecieved);
	}
	
}
