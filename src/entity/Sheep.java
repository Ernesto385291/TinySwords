package entity;

import main.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Random;
import java.awt.geom.AffineTransform;

public class Sheep extends Entity {
    GamePanel gp;
    public int worldX, worldY;
    private BufferedImage[] idleSprites;
    private BufferedImage[] bouncingSprites;
    private int spriteNum = 0;
    private int spriteCounter = 0;
    private int stateTimer = 0;
    private boolean isBouncing = false;
    private int speed = 2;
    private String direction;
    private Random random = new Random();
    private boolean facingRight = true;
    private int hitCount = 0;
    
    public Sheep(GamePanel gp, int col, int row) {
        this.gp = gp;
        worldX = gp.tileSize * col;
        worldY = gp.tileSize * row;
        direction = getRandomDirection();
        facingRight = true;
        getSheepImage();
    }
    
    private String getRandomDirection() {
        String[] directions = {"up", "down", "left", "right"};
        return directions[random.nextInt(directions.length)];
    }
    
    private void getSheepImage() {
        try {
            // Cargar sprites de idle
            BufferedImage idleSheet = ImageIO.read(getClass().getResourceAsStream("/public/Resources/Sheep/HappySheep_Idle.png"));
            idleSprites = new BufferedImage[8];
            int idleWidth = idleSheet.getWidth() / 8;
            int idleHeight = idleSheet.getHeight();
            for(int i = 0; i < 8; i++) {
                idleSprites[i] = idleSheet.getSubimage(i * idleWidth, 0, idleWidth, idleHeight);
            }
            
            // Cargar sprites de bouncing
            BufferedImage bounceSheet = ImageIO.read(getClass().getResourceAsStream("/public/Resources/Sheep/HappySheep_Bouncing.png"));
            bouncingSprites = new BufferedImage[6];
            int bounceWidth = bounceSheet.getWidth() / 6;
            int bounceHeight = bounceSheet.getHeight();
            for(int i = 0; i < 6; i++) {
                bouncingSprites[i] = bounceSheet.getSubimage(i * bounceWidth, 0, bounceWidth, bounceHeight);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void update() {
        spriteCounter++;
        stateTimer++;
        
        // Cambiar entre estados
        if (!isBouncing && stateTimer > 180) { // 3 segundos en idle
            isBouncing = true;
            stateTimer = 0;
            spriteNum = 0;
            direction = getRandomDirection();
            // Actualizar facingRight basado en la nueva dirección
            if (direction.equals("left")) {
                facingRight = false;
            } else if (direction.equals("right")) {
                facingRight = true;
            }
        } else if (isBouncing && stateTimer > 90) { // 1.5 segundos bouncing
            isBouncing = false;
            stateTimer = 0;
            spriteNum = 0;
        }
        
        // Actualizar animación
        if (isBouncing) {
            if (spriteCounter > 8) {
                spriteNum++;
                if (spriteNum >= 6) {
                    spriteNum = 0;
                }
                spriteCounter = 0;
                
                // Mover la oveja mientras brinca
                int nextWorldX = worldX;
                int nextWorldY = worldY;
                
                switch(direction) {
                    case "up": nextWorldY -= speed; break;
                    case "down": nextWorldY += speed; break;
                    case "left": nextWorldX -= speed; break;
                    case "right": nextWorldX += speed; break;
                }
                
                // Verificar colisiones antes de mover
                int col = nextWorldX/gp.tileSize;
                int row = nextWorldY/gp.tileSize;
                
                if (col >= 0 && row >= 0 && col < gp.maxWorldCol && row < gp.maxWorldRow) {
                    int tileNum = gp.tileManager.getTileNum(col, row);
                    if(!gp.tileManager.getTile(tileNum).collision) {
                        worldX = nextWorldX;
                        worldY = nextWorldY;
                    } else {
                        direction = getRandomDirection(); // Cambiar dirección si hay colisión
                    }
                }
            }
        } else {
            if (spriteCounter > 12) {
                spriteNum++;
                if (spriteNum >= 8) {
                    spriteNum = 0;
                }
                spriteCounter = 0;
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
           worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            
            BufferedImage[] currentAnimation = isBouncing ? bouncingSprites : idleSprites;
            int width = (int)(gp.tileSize * 1.5);
            int height = (int)(gp.tileSize * 1.5);
            
            // Guardar la transformación original
            AffineTransform originalTransform = g2.getTransform();
            
            // Solo voltear horizontalmente si va hacia la izquierda
            if (!facingRight) {
                g2.translate(screenX + width, screenY);
                g2.scale(-1, 1);
                g2.drawImage(currentAnimation[spriteNum], 0, 0, width, height, null);
            } else {
                g2.drawImage(currentAnimation[spriteNum], screenX, screenY, width, height, null);
            }
            
            // Restaurar la transformación original
            g2.setTransform(originalTransform);
        }
    }
    
    public void getHit() {
        hitCount++;
        if(hitCount >= 2) {
            gp.updateObjective(2, 1); // Actualizar objetivo de matar ovejas
            Meat meat = new Meat(gp, worldX, worldY);
            gp.addEntity(meat);
            
            // Remover esta oveja del array de ovejas
            for(int i = 0; i < gp.sheep.length; i++) {
                if(gp.sheep[i] == this) {
                    gp.sheep[i] = null;
                    break;
                }
            }
        }
    }
} 