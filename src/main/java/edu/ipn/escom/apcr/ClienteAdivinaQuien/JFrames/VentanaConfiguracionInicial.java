package edu.ipn.escom.apcr.ClienteAdivinaQuien.JFrames;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.jdom.JDOMException;

import edu.ipn.escom.apcr.ClienteAdivinaQuien.Juego.ClienteAdivinaQuien;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JButton;

public class VentanaConfiguracionInicial extends JFrame {

	private static final long serialVersionUID = -7213304325089343630L;
	
	private JPanel contentPane;
	
	public VentanaConfiguracionInicial() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 250);
		setTitle("Cliente adivina quien - configuracion inicial");
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		JLabel lblBienvenido = new JLabel("Bienvenido a adivina quien");
		lblBienvenido.setBounds(155,10,200,15);
		contentPane.add(lblBienvenido);
		JLabel lblInstrucciones = new JLabel("Por favor ingrese la direccion IP y el puerto del servidor");
		lblInstrucciones.setBounds(55,40,400,15);
		contentPane.add(lblInstrucciones);
		JLabel lblDireccionIP = new JLabel("Direccion IP:");
		lblDireccionIP.setBounds(20, 90, 90, 15);
		contentPane.add(lblDireccionIP);
		final JTextField txtDireccionIP = new JTextField();
		txtDireccionIP.setBounds(130, 77, 350, 40);
		contentPane.add(txtDireccionIP);
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(20, 133, 90, 15);
		contentPane.add(lblPuerto);
		final JTextField txtPuerto = new JTextField();
		txtPuerto.setBounds(130, 115, 350, 40);
		contentPane.add(txtPuerto);
		JButton btnAceptar = new JButton("Conectar");
		btnAceptar.setBounds(180,170, 150, 30);
		contentPane.add(btnAceptar);
		btnAceptar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(txtDireccionIP.getText().trim().equals("") || txtPuerto.getText().trim().equals("")) {
						JOptionPane.showMessageDialog(VentanaConfiguracionInicial.this,"Ingrese los datos que se le solicitan");
					}else {
						VentanaConfiguracionInicial.this.setVisible(false);
						new ClienteAdivinaQuien().iniciarCliente(txtDireccionIP.getText(), Integer.valueOf(txtPuerto.getText()));
					}
				} catch (NumberFormatException e1) {
					VentanaConfiguracionInicial.this.setVisible(true);
					JOptionPane.showMessageDialog(VentanaConfiguracionInicial.this,"El puerto debe de ser numerico");
				} catch (ConnectException | UnknownHostException e1) {
					VentanaConfiguracionInicial.this.setVisible(true);
					JOptionPane.showMessageDialog(VentanaConfiguracionInicial.this,"No se ha podido conectar al servidor, verifique los datos e intente de nuevo");
				} catch (IOException | JDOMException | InterruptedException | BrokenBarrierException | InvocationTargetException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null,"Error general");
				}
				
			}
		});
		
	}

}
