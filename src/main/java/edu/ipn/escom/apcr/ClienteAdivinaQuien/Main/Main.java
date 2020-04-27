package edu.ipn.escom.apcr.ClienteAdivinaQuien.Main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.ipn.escom.apcr.ClienteAdivinaQuien.JFrames.VentanaConfiguracionInicial;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException  e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException  e1) {
				e1.printStackTrace();
			}
		}
		new VentanaConfiguracionInicial().setVisible(true);
	}

}
