package edu.ipn.escom.apcr.ClienteAdivinaQuien.GUIComponents;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

	private static final long serialVersionUID = 7248723047924718665L;
	
	private BufferedImage image;

    public ImagePanel(String base64ImageData) {
    	setBackground(Color.WHITE);
       try {                
    	  byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64ImageData);
          image = ImageIO.read(new ByteArrayInputStream(imageBytes));
       } catch (IOException ex) {
            ex.printStackTrace();
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);     
    }

}