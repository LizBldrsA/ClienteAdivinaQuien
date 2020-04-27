package edu.ipn.escom.apcr.ClienteAdivinaQuien.JFrames;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import edu.ipn.escom.apcr.ClienteAdivinaQuien.Beans.Personaje;
import edu.ipn.escom.apcr.ClienteAdivinaQuien.GUIComponents.ImagePanel;
import edu.ipn.escom.apcr.ClienteAdivinaQuien.Juego.ClienteAdivinaQuien;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;

public class VentanaPrincipal extends JFrame {
	
	private static final long serialVersionUID = -8351105620654901291L;

	private ClienteAdivinaQuien clienteAdivinaQuien;
	
	private JPanel contentPane;
	private JPanel panelPersonajes;
	private JPanel panelInformacionPersonajes;
	private JPanel panelInformacionTiro;
	private LinkedList<JPanel> panelesPersonajes = new LinkedList<JPanel>();
	
	public VentanaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100,1024, 620);
		setTitle("Adivina quien");
		setName("Adivina quien");
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panelPersonajes = new JPanel();
		panelPersonajes.setBounds(0, 0, 683, 586);
		panelPersonajes.setBackground(Color.WHITE);
		panelPersonajes.setLayout(new GridLayout(3, 4));
		
		contentPane.add(panelPersonajes);
		
		panelInformacionPersonajes = new JPanel();
		panelInformacionPersonajes.setBounds(684, 0, 339, 360);
		panelInformacionPersonajes.setBackground(Color.WHITE);
		panelInformacionPersonajes.setLayout(new GridLayout(7,1,10,10));
		
		contentPane.add(panelInformacionPersonajes);
		
		
		panelInformacionTiro = new JPanel();
		panelInformacionTiro.setBounds(684,361,339,226);
		panelInformacionTiro.setBackground(Color.WHITE);
		panelInformacionTiro.setLayout(new GridLayout(3,1,10,10));
		
		contentPane.add(panelInformacionTiro);
	}
	
	public void llenarTableroPersonajes(LinkedList<Personaje> personajes) {
		for(final Personaje personaje:personajes) {
			JPanel panelPersonaje = new JPanel();
			panelesPersonajes.add(panelPersonaje);
			final int numeroPanel = panelesPersonajes.size()-1;
			panelPersonaje.setBackground(Color.WHITE);
			panelPersonaje.setBounds(0,0,227,144);
			panelPersonaje.setLayout(new BoxLayout(panelPersonaje,BoxLayout.Y_AXIS));
			ImagePanel imgPersonaje = new ImagePanel(personaje.getDatosImagenBase64());
			imgPersonaje.setAlignmentX(Component.CENTER_ALIGNMENT);
			panelPersonaje.add(imgPersonaje);
			JLabel lblNombrePersonaje = new JLabel(personaje.getNombre());
			lblNombrePersonaje.setFont(new Font(lblNombrePersonaje.getFont().getFontName(),Font.BOLD,lblNombrePersonaje.getFont().getSize()));
			lblNombrePersonaje.setAlignmentX(Component.CENTER_ALIGNMENT);
			panelPersonaje.add(lblNombrePersonaje);
			JButton btnOcultarPersonaje = new JButton("Ocultar");
			btnOcultarPersonaje.setAlignmentX(Component.CENTER_ALIGNMENT);
			btnOcultarPersonaje.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					panelesPersonajes.get(numeroPanel).setVisible(false);
				}
			});
			panelPersonaje.add(btnOcultarPersonaje);
			panelPersonaje.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {}				
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					mostrarInformacionPersonaje(personaje);
				}
			});
			panelPersonajes.add(panelPersonaje);
			
		}
	}
	
	private void mostrarInformacionPersonaje(Personaje personaje) {
		panelInformacionPersonajes.removeAll();
		JLabel lblNombrePersonaje = new JLabel(personaje.getNombre());
		lblNombrePersonaje.setHorizontalAlignment(JLabel.CENTER);
		lblNombrePersonaje.setFont(new Font(lblNombrePersonaje.getFont().getFontName(),Font.BOLD,lblNombrePersonaje.getFont().getSize()+3));
		panelInformacionPersonajes.add(lblNombrePersonaje);
		for(int i = 0;i<personaje.getCaracteristicas().size();i++) {
			String caracteristica = personaje.getCaracteristicas().get(i);
			JLabel lblCaracteristica = new JLabel((i+1)+".- "+caracteristica);
			lblCaracteristica.setFont(new Font(lblCaracteristica.getFont().getFontName(),Font.PLAIN,lblCaracteristica.getFont().getSize()+1));
			panelInformacionPersonajes.add(lblCaracteristica);
		}
		refrescarGUI();
	}

	public void mostrarInformacionEspera(final int numeroJugador, final int jugadoresConectados, final int totalJugadores) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				panelInformacionTiro.removeAll();
				JLabel lblNumJugador = new JLabel("Eres el jugador: "+(numeroJugador+1));
				lblNumJugador.setFont(new Font(lblNumJugador.getFont().getFontName(),Font.BOLD,lblNumJugador.getFont().getSize()+1));
				panelInformacionTiro.add(lblNumJugador);
				JLabel lblInfoJugadores = new JLabel(jugadoresConectados+" de "+totalJugadores+" conectados");
				lblInfoJugadores.setFont(new Font(lblInfoJugadores.getFont().getFontName(),Font.PLAIN,lblInfoJugadores.getFont().getSize()+1));
				panelInformacionTiro.add(lblInfoJugadores);
				refrescarGUI();	
			}
		});
	}
	
	public void mostrarInformacionJugadorEnTiro(final int numeroJugadorEnTurno,final int numeroJugador) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				panelInformacionTiro.removeAll();
				JLabel lblNumJugador = new JLabel("Eres el jugador: "+(numeroJugador+1));
				lblNumJugador.setFont(new Font(lblNumJugador.getFont().getFontName(),Font.BOLD,lblNumJugador.getFont().getSize()+1));
				panelInformacionTiro.add(lblNumJugador);
				JLabel lblNumJugadorEnTurno = new JLabel("Es turno del jugador: "+(numeroJugadorEnTurno+1));
				lblNumJugadorEnTurno.setFont(new Font(lblNumJugadorEnTurno.getFont().getFontName(),Font.BOLD,lblNumJugadorEnTurno.getFont().getSize()+1));
				panelInformacionTiro.add(lblNumJugadorEnTurno);
				refrescarGUI();
			}
		});
	}
	
	public void mostrarPanelTiro() throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				panelInformacionTiro.removeAll();
				JLabel lblInfoTiro = new JLabel("Es tu turno");
				lblInfoTiro.setFont(new Font(lblInfoTiro.getFont().getFontName(),Font.BOLD,lblInfoTiro.getFont().getSize()+1));
				panelInformacionTiro.add(lblInfoTiro);
				JLabel lblIstrucciones = new JLabel("Da click al boton para enviar tu tiro");
				lblIstrucciones.setFont(new Font(lblIstrucciones.getFont().getFontName(),Font.PLAIN,lblIstrucciones.getFont().getSize()));
				panelInformacionTiro.add(lblIstrucciones);
				final JButton btnEscucharTiro = new JButton("Tirar!");
				btnEscucharTiro.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						btnEscucharTiro.setText("Grabando y procesando tiro...");
						btnEscucharTiro.setEnabled(false);
						refrescarGUI();
						try {
							realizarTiro();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				panelInformacionTiro.add(btnEscucharTiro);
				refrescarGUI();
			}
		});
	}

	
	public void mostrarInformacionPartidaPorEmpezar() throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				panelInformacionTiro.removeAll();
				JLabel lblInfoInicio = new JLabel("La partida empezara pronto... ");
				lblInfoInicio.setFont(new Font(lblInfoInicio.getFont().getFontName(),Font.BOLD,lblInfoInicio.getFont().getSize()+1));
				panelInformacionTiro.add(lblInfoInicio);
				refrescarGUI();
			}
		});
	}
	
	private void realizarTiro() throws IOException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					clienteAdivinaQuien.enviarTiro();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void refrescarGUI() {
		contentPane.validate();
		contentPane.repaint();
	}

	public void setClienteAdivinaQuien(ClienteAdivinaQuien clienteAdivinaQuien) {
		this.clienteAdivinaQuien = clienteAdivinaQuien;
	}
}
