package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class UI {
    GamePanel gp;
    BufferedImage heartFull;
    
    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            heartFull = ImageIO.read(getClass().getResourceAsStream("/public/UI/Icons/Heart.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2) {
        // Hacer los corazones más pequeños y más juntos
        int heartSize = (int)(gp.tileSize * 0.7); // 70% del tamaño de un tile
        int margin = 5; // Reducir el espacio entre corazones
        int startX = 10; // Mantener el margen desde el borde izquierdo
        int startY = 10; // Mantener el margen desde el borde superior
        
        for(int i = 0; i < 3; i++) {
            g2.drawImage(heartFull, 
                startX + (i * (heartSize + margin)), // x position
                startY,                              // y position
                heartSize,                           // width
                heartSize,                           // height
                null);
        }
    }
} 