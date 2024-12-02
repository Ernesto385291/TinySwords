package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Entity {
    public int worldX, worldY;
    protected BufferedImage[] image = new BufferedImage[10];
    protected int speed;
    protected String direction;
    
    public abstract void update();
    public abstract void draw(Graphics2D g2);
    
    protected BufferedImage setup(String imagePath) {
        try {
            return javax.imageio.ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 