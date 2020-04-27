package edu.ipn.escom.apcr.ClienteAdivinaQuien.Util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;

public class GrabadorAudio {
  
    private TargetDataLine lineaDatosMicrofono;
 
    /**
     * Defines an audio format
     */
    private static AudioFormat obtenerFormatoAudio() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,channels, signed, bigEndian);
        return format;
    }
 

    private void iniciarGrabacion(File archivoAudio) {
        try {
            AudioFormat formatoAudio = obtenerFormatoAudio();
            DataLine.Info informacionLineaDatosMicrofono = new DataLine.Info(TargetDataLine.class, formatoAudio);
 
            lineaDatosMicrofono = (TargetDataLine) AudioSystem.getLine(informacionLineaDatosMicrofono);
            lineaDatosMicrofono.open(formatoAudio);
            lineaDatosMicrofono.start(); 
            AudioInputStream flujoEntradaAudio = new AudioInputStream(lineaDatosMicrofono);
            AudioSystem.write(flujoEntradaAudio, AudioFileFormat.Type.WAVE, archivoAudio);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void terminarGrabacion() {
    	lineaDatosMicrofono.stop();
    	lineaDatosMicrofono.close();
    }
 
    public File grabarAudio(final int duracion) throws IOException {
    	File archivoAudio = File.createTempFile("AUDIO_TIRO_",".wav");
    	new Thread(new Runnable() {
             public void run() {
                 try {
                     Thread.sleep(duracion*1000);
                 } catch (InterruptedException ex) {
                     ex.printStackTrace();
                 }
                 terminarGrabacion();
             }
         }).start();
    	iniciarGrabacion(archivoAudio);
    	
    	return archivoAudio;
    }
    
    public static boolean comprobarMicrofonoDisponible() {
    	DataLine.Info informacionLineaDatosMicrofono = new DataLine.Info(TargetDataLine.class, obtenerFormatoAudio());
        if (AudioSystem.isLineSupported(informacionLineaDatosMicrofono)) {
        	return true;
        }else {
        	return false;
        }
    }
    
}
