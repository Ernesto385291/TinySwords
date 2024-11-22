package entity;

import main.GamePanel;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import util.SpritesheetViewer;

public class Enemy extends Entity {
    
    GamePanel gp;
    public int screenX;
    public int screenY;
    
    // Variables para las animaciones
    BufferedImage[] walkSprites;
    BufferedImage[] idleSprites;
    int spriteNum = 0;
    int spriteCounter = 0;
    boolean isMoving = false;
    boolean facingRight = true;
    
    // Declarar las variables sin inicializar
    private int patrolDistance;
    private int detectionRange;
    private int startX;
    private boolean patrollingRight = true;
    
    public Enemy(GamePanel gp) {
        this.gp = gp;
        screenX = gp.screenWidth/2;
        screenY = gp.screenHeight/2;
        
        setDefaultValues();
        getEnemyImage();
    }
    
    public void setDefaultValues() {
        worldX = gp.tileSize * 10;
        worldY = gp.tileSize * 10;
        startX = worldX;
        speed = 2;
        direction = "right";
        
        // Reducir la distancia de patrulla
        patrolDistance = gp.tileSize * 2;
        detectionRange = gp.tileSize * 4;
    }
    
    public void getEnemyImage() {
        try {
            // Mostrar el visor de spritesheet con las dimensiones correctas
            SpritesheetViewer.showSpritesheetGrid(
                "/public/Factions/Goblins/Troops/Torch/Blue/Torch_Blue.png",
                7, // número de columnas
                5  // número de filas
            );
            
            BufferedImage spriteSheet = setup("/public/Factions/Goblins/Troops/Torch/Blue/Torch_Blue.png");
            
            int spriteWidth = spriteSheet.getWidth() / 7;  // Dividir entre 7 columnas
            int spriteHeight = spriteSheet.getHeight() / 5; // Dividir entre 5 filas
            
            // Inicializar arrays para las animaciones
            walkSprites = new BufferedImage[7];
            idleSprites = new BufferedImage[7];
            
            // Extraer sprites de idle (fila 0)
            for(int i = 0; i < 7; i++) {
                idleSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 0 * spriteHeight, spriteWidth, spriteHeight
                );
            }
            
            // Extraer sprites de caminar (fila 1)
            for(int i = 0; i < 7; i++) {
                walkSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 1 * spriteHeight, spriteWidth, spriteHeight
                );
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void update() {
        isMoving = false;
        
        // Calcular distancia al jugador
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        
        // Si el jugador está dentro del rango de detección
        if (xDistance < detectionRange && yDistance < detectionRange) {
            // Perseguir al jugador
            if(worldX < gp.player.worldX - gp.tileSize) {
                direction = "right";
                worldX += speed;
                facingRight = true;
                isMoving = true;
            }
            if(worldX > gp.player.worldX + gp.tileSize) {
                direction = "left";
                worldX -= speed;
                facingRight = false;
                isMoving = true;
            }
            if(worldY < gp.player.worldY - gp.tileSize) {
                direction = "down";
                worldY += speed;
                isMoving = true;
            }
            if(worldY > gp.player.worldY + gp.tileSize) {
                direction = "up";
                worldY -= speed;
                isMoving = true;
            }
        } else {
            // Patrullar de izquierda a derecha
            isMoving = true;
            
            if (patrollingRight) {
                direction = "right";
                facingRight = true;
                worldX += speed;
                if (worldX > startX + patrolDistance) {
                    patrollingRight = false;
                }
            } else {
                direction = "left";
                facingRight = false;
                worldX -= speed;
                if (worldX < startX - patrolDistance) {
                    patrollingRight = true;
                }
            }
        }
        
        // Actualizar animación
        spriteCounter++;
        if(spriteCounter > 12) {
            spriteNum++;
            spriteNum = spriteNum % 7;
            spriteCounter = 0;
        }
    }
    
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
           worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            
            BufferedImage[] currentAnimation = isMoving ? walkSprites : idleSprites;
            BufferedImage image = currentAnimation[spriteNum];
            
            int width = (int)(gp.tileSize * 1.5);
            int height = (int)(gp.tileSize * 1.5);
            int drawX = screenX - (width - gp.tileSize)/2;
            int drawY = screenY - (height - gp.tileSize)/2;
            
            if (!facingRight) {
                java.awt.geom.AffineTransform originalTransform = g2.getTransform();
                g2.translate(drawX + width, drawY);
                g2.scale(-1, 1);
                g2.drawImage(image, 0, 0, width, height, null);
                g2.setTransform(originalTransform);
            } else {
                g2.drawImage(image, drawX, drawY, width, height, null);
            }
        }
    }
} 