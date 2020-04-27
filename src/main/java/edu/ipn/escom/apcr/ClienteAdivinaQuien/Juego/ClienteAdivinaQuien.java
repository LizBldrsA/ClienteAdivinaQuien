package edu.ipn.escom.apcr.ClienteAdivinaQuien.Juego;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import edu.ipn.escom.apcr.ClienteAdivinaQuien.Beans.Personaje;
import edu.ipn.escom.apcr.ClienteAdivinaQuien.JFrames.VentanaLogTiros;
import edu.ipn.escom.apcr.ClienteAdivinaQuien.JFrames.VentanaPrincipal;
import edu.ipn.escom.apcr.ClienteAdivinaQuien.Util.GrabadorAudio;

public class ClienteAdivinaQuien {
	
	private int puerto;
	private String direccionIP;
	
	private Socket socketServidor;
	private DataInputStream flujoDatosEntradaServidor;
	private DataOutputStream flujoDatosSalidaServidor;
	
	private volatile VentanaPrincipal ventanaPrincipal;
	private volatile VentanaLogTiros ventanaLogTiros;
	
	private LinkedList<String> listaDeTiros = new LinkedList<String>();
	private LinkedList<Personaje> personajes = new LinkedList<Personaje>();
	private int numeroJugador;
	
	private boolean finDelJuego = false;
	private long horaInicio;
	private long horaFin;
	
	public void iniciarCliente(String direccionIP, int puerto) throws ConnectException, UnknownHostException, IOException, JDOMException, InterruptedException, BrokenBarrierException, InvocationTargetException{
		if(GrabadorAudio.comprobarMicrofonoDisponible()) {
			conectarConServidor(direccionIP, puerto);
			numeroJugador = Integer.parseInt(flujoDatosEntradaServidor.readUTF());
			obtenerPersonajes();
			mostrarVentanaPrincipal();
			mostrarVentanaLogTiros();
			iniciarHiloEscuchaActualizaciones();
		}else {
			JOptionPane.showMessageDialog(ventanaPrincipal,"No tiene un microfono disponible");
		}
	}
	
	private void conectarConServidor(String direccionIP, int puerto) throws ConnectException, UnknownHostException, IOException{
		this.direccionIP = direccionIP;
		this.puerto = puerto;
		socketServidor = new Socket(direccionIP,puerto);
		flujoDatosEntradaServidor = new DataInputStream(socketServidor.getInputStream());
		flujoDatosSalidaServidor = new DataOutputStream(socketServidor.getOutputStream());
	}
	
	private void iniciarHiloEscuchaActualizaciones() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					escucharActualizacionesServidor();
				} catch (IOException | InvocationTargetException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void escucharActualizacionesServidor() throws IOException, InvocationTargetException, InterruptedException {
		while(!finDelJuego) {
			String datoRecibido = flujoDatosEntradaServidor.readUTF().trim();
			if(datoRecibido.contains("JUGADOR_CONECTADO")) {
				int jugadoresConectados = Integer.valueOf(datoRecibido.split(" ")[1]);
				int totalJugadores = Integer.valueOf(datoRecibido.split(" ")[2]);
				ventanaPrincipal.mostrarInformacionEspera(numeroJugador,jugadoresConectados,totalJugadores);
			}else if(datoRecibido.contains("PARTIDA_POR_EMPEZAR")) {
				ventanaPrincipal.mostrarInformacionPartidaPorEmpezar();
			}else if(datoRecibido.contains("PARTIDA_COMIENZA")) {
				horaInicio = System.currentTimeMillis();
			}else if(datoRecibido.contains("TURNO_DE")) {
				int jugadorEnTurno = Integer.valueOf(datoRecibido.split(" ")[1]);
				if(numeroJugador == jugadorEnTurno) {
					ventanaPrincipal.mostrarPanelTiro();
				}else {
					ventanaPrincipal.mostrarInformacionJugadorEnTiro(jugadorEnTurno,numeroJugador);
				}
			}else if(datoRecibido.contains("TIRO")) {
				String informacionTiro = datoRecibido.split("-")[1];
				listaDeTiros.add(informacionTiro);
				ventanaLogTiros.mostrarLogTiros(listaDeTiros);
				if(informacionTiro.contains("GANO")) {
					finDelJuego=true;
					horaFin = System.currentTimeMillis();
					long duracionJuego = (horaFin-horaInicio)/1000;
					JOptionPane.showMessageDialog(ventanaPrincipal,"La partida ha terminado\n "+informacionTiro+" \n Duracion: "+duracionJuego+" segundos");
					JOptionPane.showMessageDialog(ventanaPrincipal,"Creado por:\n\tBalderas Aceves Lidia Lizbeth\n\tCorona Lopez Emilio Abraham\n\nMateria: Aplicaciones para comunicaciones en red\n\nSemestre: 2020-A\n\nGrupo: 3CM8\n\nGracias por jugar!");
					terminarAplicacion();
				}
			}
		}
	}
	
	public void enviarTiro() throws IOException {
		File archivoAudio = new GrabadorAudio().grabarAudio(5);
		flujoDatosSalidaServidor.writeUTF("TIRO-CLIENTE");
		enviarArchivoServidor(archivoAudio);
	}
	

	
	private void obtenerPersonajes() throws IOException, JDOMException {
		File archivoXMLTemporal = File.createTempFile("XML_PERSONAJES",".xml");
		recibirArchivoServidor(archivoXMLTemporal);
		Document documentoXMLPersonajes = (Document) new SAXBuilder().build(archivoXMLTemporal);
		Element nodoRaizXML = documentoXMLPersonajes.getRootElement();
		List<Element> listaElementosPersonajes = (List<Element>) nodoRaizXML.getChildren("personaje");
		for (Element elementoPersonaje:listaElementosPersonajes) {
			Personaje personaje = new Personaje();   
			personaje.setNombre(elementoPersonaje.getAttribute("nombre").getValue());
			personaje.setDatosImagenBase64(elementoPersonaje.getAttribute("datosImagenBase64").getValue());
			
			LinkedList<String> caracteristicasPersonaje = new LinkedList<String>();
			
			List<Element> listaElementosCaracteristicas = (List<Element>) elementoPersonaje.getChildren("caracteristica");
			for(Element elementoCaracteristicaPersonaje:listaElementosCaracteristicas) {
				caracteristicasPersonaje.add(elementoCaracteristicaPersonaje.getText());
			}
			
			personaje.setCaracteristicas(caracteristicasPersonaje);
			personajes.add(personaje); 
		}
		
	}

	private void recibirArchivoServidor(File archivo) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(archivo);
		long tamanoArchivo = flujoDatosEntradaServidor.readLong();
		int bytesLeidos = 0;
		byte[] buffer = new byte[4092];
		while (tamanoArchivo > 0 && (bytesLeidos = socketServidor.getInputStream().read(buffer, 0, (int)Math.min(buffer.length, tamanoArchivo))) != -1){
			fileOutputStream.write(buffer,0,bytesLeidos);
		  tamanoArchivo -= bytesLeidos;
		}
		fileOutputStream.close();
	}
	
	private void enviarArchivoServidor(File archivoAEnviar) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(archivoAEnviar);
		long tamanoArchivo = archivoAEnviar.length();
		flujoDatosSalidaServidor.writeLong(tamanoArchivo);
		int bytesLeidos = 0;
		byte[] buffer = new byte[4092];
		while (tamanoArchivo > 0 && (bytesLeidos = fileInputStream.read(buffer, 0, (int)Math.min(buffer.length, tamanoArchivo))) != -1){
		  socketServidor.getOutputStream().write(buffer,0,bytesLeidos);
		  tamanoArchivo -= bytesLeidos;
		}
		fileInputStream.close();
	}
	
	private void mostrarVentanaPrincipal() throws InvocationTargetException, InterruptedException {
		ventanaPrincipal = new VentanaPrincipal();
		ventanaPrincipal.llenarTableroPersonajes(personajes);
		ventanaPrincipal.setClienteAdivinaQuien(this);
		ventanaPrincipal.setVisible(true);
	}
	
	private void mostrarVentanaLogTiros() throws InvocationTargetException, InterruptedException{
		ventanaLogTiros = new VentanaLogTiros();
		ventanaLogTiros.setVisible(true);
	}
	
	private void terminarAplicacion() throws IOException {
		flujoDatosEntradaServidor.close();
		flujoDatosSalidaServidor.close();
		socketServidor.close();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.exit(0);
			}
		});
	}
}
