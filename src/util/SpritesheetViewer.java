package util;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SpritesheetViewer {
    
    public static void showSpritesheetGrid(String imagePath, int cols, int rows) {
        try {
            // Cargar el spritesheet
            BufferedImage spriteSheet = ImageIO.read(SpritesheetViewer.class.getResourceAsStream(imagePath));
            
            int spriteWidth = spriteSheet.getWidth() / cols;
            int spriteHeight = spriteSheet.getHeight() / rows;
            
            // Crear una nueva imagen con la cuadrícula
            BufferedImage gridImage = new BufferedImage(
                spriteSheet.getWidth(), 
                spriteSheet.getHeight(), 
                BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2 = gridImage.createGraphics();
            
            // Dibujar el spritesheet original
            g2.drawImage(spriteSheet, 0, 0, null);
            
            // Dibujar la cuadrícula
            g2.setColor(Color.RED);
            for (int x = 0; x < cols; x++) {
                for (int y = 0; y < rows; y++) {
                    g2.drawRect(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
                    // Dibujar las coordenadas en cada celda
                    g2.drawString(x + "," + y, x * spriteWidth + 5, y * spriteHeight + 15);
                }
            }
            
            // Mostrar la imagen con la cuadrícula en una ventana separada
            JFrame frame = new JFrame("Spritesheet Grid Viewer");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JLabel label = new JLabel(new ImageIcon(gridImage));
            frame.add(label);
            frame.pack();
            frame.setVisible(true);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
} 