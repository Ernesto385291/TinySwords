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
        int heartSize = 32;
        int startX = 10;
        int startY = 10;
        int spacing = 40;
        
        for(int i = 0; i < gp.player.currentLife; i++) {
            g2.drawImage(heartFull, startX + (i * spacing), startY, heartSize, heartSize, null);
        }
    }
} 