package entity;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Entity {
    
    public int worldX, worldY;
    public int speed;
    
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;
    
    public int spriteCounter = 0;
    public int spriteNum = 1;
    
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    
    // Método para cargar imágenes
    public BufferedImage setup(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
} 