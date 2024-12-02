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
        int heartSize = (int)(gp.tileSize * 0.7);
        int margin = 5;
        int startX = 10;
        int startY = 10;
        
        for(int i = 0; i < gp.player.maxLife; i++) {
            if (i < gp.player.currentLife) {
                g2.drawImage(heartFull, 
                    startX + (i * (heartSize + margin)),
                    startY,
                    heartSize,
                    heartSize,
                    null);
            }
        }
    }
} 