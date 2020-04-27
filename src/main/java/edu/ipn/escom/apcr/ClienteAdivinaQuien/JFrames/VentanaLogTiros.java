package edu.ipn.escom.apcr.ClienteAdivinaQuien.JFrames;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class VentanaLogTiros extends JFrame {
	
	private JPanel contentPane;
	private JTextArea txtLogTiros;
	
	public VentanaLogTiros() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100,305,340);
		setTitle("Historial tiros - adivina quien");
		setName("Historial tiros - adivina quien");
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		txtLogTiros = new JTextArea();
		txtLogTiros.setText("Informacion de tiros: \n");
		txtLogTiros.setBounds(0,0, 300,300);
		txtLogTiros.setEditable(false);
		JScrollPane scrollTextArea = new JScrollPane (txtLogTiros,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollTextArea.setBounds(0, 0,300,300);
		contentPane.add(scrollTextArea);
		
		

	}

	public void mostrarLogTiros(LinkedList<String> listaDeTiros) {
		txtLogTiros.setText("Informacion de tiros: \n");
		for(String tiro:listaDeTiros) {
			txtLogTiros.append(tiro+"\n");
		}
		refrescarGUI();
	}
	
	private void refrescarGUI() {
		contentPane.validate();
		contentPane.repaint();
	}
}
