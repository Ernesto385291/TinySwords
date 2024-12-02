package entity;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Meat extends Entity {
    
    GamePanel gp;
    private int animationCounter = 0;
    private final int animationSpeed = 10;
    private int currentFrame = 0;
    private BufferedImage[] sprites;
    private final int TOTAL_FRAMES = 7;
    private boolean animationFinished = false;
    private boolean shouldRemove = false;
    
    public Meat(GamePanel gp, int x, int y) {
        this.gp = gp;
        this.worldX = x;
        this.worldY = y;
        sprites = new BufferedImage[TOTAL_FRAMES];
        loadSprites();
    }
    
    private void loadSprites() {
        try {
            String path = "/public/Resources/Resources/M_Spawn.png";
            java.io.InputStream is = getClass().getResourceAsStream(path);
            if(is == null) {
                System.err.println("No se pudo encontrar la imagen en: " + path);
                return;
            }
            
            BufferedImage spriteSheet = ImageIO.read(is);
            int spriteWidth = spriteSheet.getWidth() / TOTAL_FRAMES;
            int spriteHeight = spriteSheet.getHeight();
            
            // Extraer cada sprite del spritesheet
            for(int i = 0; i < TOTAL_FRAMES; i++) {
                sprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 
                    0, 
                    spriteWidth, 
                    spriteHeight
                );
            }
        } catch(IOException e) {
            System.err.println("Error cargando spritesheet de carne: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void update() {
        if (!animationFinished) {
            animationCounter++;
            if(animationCounter > animationSpeed) {
                currentFrame++;
                if(currentFrame >= TOTAL_FRAMES) {
                    currentFrame = TOTAL_FRAMES - 1; // Mantener el último frame
                    animationFinished = true;
                }
                animationCounter = 0;
            }
        }
        
        // Verificar colisión con el jugador si la animación terminó
        if (animationFinished) {
            checkPlayerCollision();
        }
    }
    
    private void checkPlayerCollision() {
        // Crear rectángulos de colisión
        int meatArea = gp.tileSize;
        
        // Verificar si el jugador está tocando la carne
        if (Math.abs(worldX - gp.player.worldX) < meatArea && 
            Math.abs(worldY - gp.player.worldY) < meatArea) {
            
            // Aumentar la vida del jugador
            if (gp.player.currentLife < gp.player.maxLife) {
                gp.player.currentLife++;
            }
            shouldRemove = true;  // Marcar para eliminación en lugar de eliminar directamente
        }
    }
    
    public boolean shouldBeRemoved() {
        return shouldRemove;
    }
    
    public void draw(java.awt.Graphics2D g2) {
        if(sprites[currentFrame] != null) {
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                
                g2.drawImage(sprites[currentFrame], screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }
} 