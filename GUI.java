package tryingtowork;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class GUI implements ActionListener{

	private JFrame frmFrontPanel;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_4;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextArea textArea;
	private JTextArea textArea_1;
	private JTextArea textArea_2;
	private JTextArea textArea_3;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JButton btnNewButton_3;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblIxr;
	private JButton btnNewButton_1_1;
	private JTextArea textArea_1_1;
	private JTextArea textArea_2_1;
	private JButton btnNewButton_2_1; 
	private JTextArea textArea_3_1;
	private JButton btnNewButton_3_1;
	private JLabel lblNewLabel_2;
	private JTextArea textArea_1_1_1; 
	private JButton btnNewButton_1_1_1;
	private JTextArea textArea_1_1_2;
	private JButton btnNewButton_1_1_2;
	private JTextArea textArea_1_1_3;
	private JButton btnNewButton_1_1_3; 
	private JTextArea textArea_1_1_4;
	private JButton btnNewButton_1_1_4; 
	private JTextArea textArea_1_1_4_1;
	private JTextArea textArea_1_1_4_2;
	private JLabel lblNewLabel_3;
	private JButton btnNewButton_3_2; 
	private JLabel lblNewLabel_3_1;
	private JButton btnNewButton_3_2_1; 
	public static Control control;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmFrontPanel.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		control = new Control();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFrontPanel = new JFrame();
		frmFrontPanel.getContentPane().setBackground(new Color(204, 153, 255));
		frmFrontPanel.setTitle("Front Panel");
		frmFrontPanel.setBounds(100, 100, 1041, 560);
		frmFrontPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFrontPanel.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setText("0");
		textArea.setBounds(25, 88, 19, 28);
		frmFrontPanel.getContentPane().add(textArea);
		
		textArea_1 = new JTextArea();
		textArea_1.setText("1");
		textArea_1.setBounds(25, 126, 19, 28);
		frmFrontPanel.getContentPane().add(textArea_1);
		
		textArea_2 = new JTextArea();
		textArea_2.setText("2");
		textArea_2.setBounds(25, 164, 19, 28);
		frmFrontPanel.getContentPane().add(textArea_2);
		
		textArea_3 = new JTextArea();
		textArea_3.setText("3");
		textArea_3.setBounds(25, 202, 19, 28);
		frmFrontPanel.getContentPane().add(textArea_3);
		
		textField = new JTextField();
		textField.setBounds(54, 91, 96, 19);
		frmFrontPanel.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(54, 129, 96, 19);
		frmFrontPanel.getContentPane().add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(54, 167, 96, 19);
		frmFrontPanel.getContentPane().add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(54, 205, 96, 19);
		frmFrontPanel.getContentPane().add(textField_3);
		
		btnNewButton = new JButton("");		
		btnNewButton.setForeground(Color.BLUE);
		btnNewButton.setBackground(Color.BLUE);
		btnNewButton.setBounds(153, 90, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton);
		
		btnNewButton_1 = new JButton("");
		btnNewButton_1.setForeground(Color.BLUE);
		btnNewButton_1.setBackground(Color.BLUE);
		btnNewButton_1.setBounds(153, 128, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("");
		btnNewButton_2.setForeground(Color.BLUE);
		btnNewButton_2.setBackground(Color.BLUE);
		btnNewButton_2.setBounds(153, 166, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_2);
		
		btnNewButton_3 = new JButton("");
		btnNewButton_3.setForeground(Color.BLUE);
		btnNewButton_3.setBackground(Color.BLUE);
		btnNewButton_3.setBounds(153, 204, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_3);
		
		lblNewLabel = new JLabel("GPR");
		lblNewLabel.setBounds(72, 68, 45, 13);
		frmFrontPanel.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("CSCI 6461 Machine Simulator");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(372, 10, 282, 36);
		frmFrontPanel.getContentPane().add(lblNewLabel_1);
		
		lblIxr = new JLabel("IXR");
		lblIxr.setBounds(273, 68, 45, 13);
		frmFrontPanel.getContentPane().add(lblIxr);
		
		btnNewButton_1_1 = new JButton("");
		btnNewButton_1_1.setForeground(Color.BLUE);
		btnNewButton_1_1.setBackground(Color.BLUE);
		btnNewButton_1_1.setBounds(354, 128, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_1_1);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(255, 129, 96, 19);
		frmFrontPanel.getContentPane().add(textField_5);
		
		textArea_1_1 = new JTextArea();
		textArea_1_1.setText("1");
		textArea_1_1.setBounds(226, 126, 19, 28);
		frmFrontPanel.getContentPane().add(textArea_1_1);
		
		textArea_2_1 = new JTextArea();
		textArea_2_1.setText("2");
		textArea_2_1.setBounds(226, 164, 19, 28);
		frmFrontPanel.getContentPane().add(textArea_2_1);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(255, 167, 96, 19);
		frmFrontPanel.getContentPane().add(textField_6);
		
		btnNewButton_2_1 = new JButton("");
		btnNewButton_2_1.setForeground(Color.BLUE);
		btnNewButton_2_1.setBackground(Color.BLUE);
		btnNewButton_2_1.setBounds(354, 166, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_2_1);

		textArea_3_1 = new JTextArea();
		textArea_3_1.setText("3");
		textArea_3_1.setBounds(226, 202, 19, 28);
		frmFrontPanel.getContentPane().add(textArea_3_1);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(255, 205, 96, 19);
		frmFrontPanel.getContentPane().add(textField_7);
		
		btnNewButton_3_1 = new JButton("");
		btnNewButton_3_1.setForeground(Color.BLUE);
		btnNewButton_3_1.setBackground(Color.BLUE);
		btnNewButton_3_1.setBounds(354, 204, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_3_1);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(115, 401, 490, 28);
		frmFrontPanel.getContentPane().add(textField_4);
		
		lblNewLabel_2 = new JLabel("Load File");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(54, 398, 73, 28);
		frmFrontPanel.getContentPane().add(lblNewLabel_2);
		
		textArea_1_1_1 = new JTextArea();
		textArea_1_1_1.setText("PC");
		textArea_1_1_1.setBounds(448, 88, 29, 28);
		frmFrontPanel.getContentPane().add(textArea_1_1_1);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(487, 91, 96, 19);
		frmFrontPanel.getContentPane().add(textField_8);
		
		btnNewButton_1_1_1 = new JButton("");
		btnNewButton_1_1_1.setForeground(Color.BLUE);
		btnNewButton_1_1_1.setBackground(Color.BLUE);
		btnNewButton_1_1_1.setBounds(586, 90, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_1_1_1);
		
		textArea_1_1_2 = new JTextArea();
		textArea_1_1_2.setText("MAR");
		textArea_1_1_2.setBounds(448, 126, 29, 28);
		frmFrontPanel.getContentPane().add(textArea_1_1_2);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(487, 129, 96, 19);
		frmFrontPanel.getContentPane().add(textField_9);
		
		btnNewButton_1_1_2 = new JButton("");
		btnNewButton_1_1_2.setForeground(Color.BLUE);
		btnNewButton_1_1_2.setBackground(Color.BLUE);
		btnNewButton_1_1_2.setBounds(586, 128, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_1_1_2);
		
		textArea_1_1_3 = new JTextArea();
		textArea_1_1_3.setText("MBR");
		textArea_1_1_3.setBounds(448, 164, 29, 28);
		frmFrontPanel.getContentPane().add(textArea_1_1_3);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(487, 167, 96, 19);
		frmFrontPanel.getContentPane().add(textField_10);
		
		btnNewButton_1_1_3 = new JButton("");
		btnNewButton_1_1_3.setForeground(Color.BLUE);
		btnNewButton_1_1_3.setBackground(Color.BLUE);
		btnNewButton_1_1_3.setBounds(586, 166, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_1_1_3);
		
		textArea_1_1_4 = new JTextArea();
		textArea_1_1_4.setText("IR");
		textArea_1_1_4.setBounds(448, 202, 29, 28);
		frmFrontPanel.getContentPane().add(textArea_1_1_4);
		
		textField_11 = new JTextField();
		textField_11.setColumns(10);
		textField_11.setBounds(487, 205, 96, 19);
		frmFrontPanel.getContentPane().add(textField_11);
		
		btnNewButton_1_1_4 = new JButton("");
		btnNewButton_1_1_4.setForeground(Color.BLUE);
		btnNewButton_1_1_4.setBackground(Color.BLUE);
		btnNewButton_1_1_4.setBounds(586, 204, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_1_1_4);
		
		textArea_1_1_4_1 = new JTextArea();
		textArea_1_1_4_1.setText("CC");
		textArea_1_1_4_1.setBounds(477, 235, 29, 28);
		frmFrontPanel.getContentPane().add(textArea_1_1_4_1);
		
		textField_12 = new JTextField();
		textField_12.setColumns(10);
		textField_12.setBounds(516, 238, 89, 19);
		frmFrontPanel.getContentPane().add(textField_12);
		
		textArea_1_1_4_2 = new JTextArea();
		textArea_1_1_4_2.setText("MFR");
		textArea_1_1_4_2.setBounds(477, 275, 29, 28);
		frmFrontPanel.getContentPane().add(textArea_1_1_4_2);
		
		textField_13 = new JTextField();
		textField_13.setColumns(10);
		textField_13.setBounds(516, 278, 89, 19);
		frmFrontPanel.getContentPane().add(textField_13);
		
		lblNewLabel_3 = new JLabel("Init");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(25, 338, 29, 21);
		frmFrontPanel.getContentPane().add(lblNewLabel_3);
		
		btnNewButton_3_2 = new JButton("");
		btnNewButton_3_2.setForeground(Color.BLUE);
		btnNewButton_3_2.setBackground(Color.BLUE);
		btnNewButton_3_2.setBounds(55, 338, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_3_2);
		btnNewButton_3_2.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
					control.loadLF("src//load4.txt");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
		
		lblNewLabel_3_1 = new JLabel("Run");
		lblNewLabel_3_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_3_1.setBounds(84, 338, 29, 21);
		frmFrontPanel.getContentPane().add(lblNewLabel_3_1);
		
		btnNewButton_3_2_1 = new JButton("");
		btnNewButton_3_2_1.setForeground(Color.BLUE);
		btnNewButton_3_2_1.setBackground(Color.BLUE);
		btnNewButton_3_2_1.setBounds(114, 338, 19, 21);
		frmFrontPanel.getContentPane().add(btnNewButton_3_2_1);
		btnNewButton_3_2_1.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
		        control.runSimulator();
		    }
		});
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		
	}
}
