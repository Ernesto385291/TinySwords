package main;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Player implements KeyListener {
    
    GamePanel gp;
    int x, y;
    int speed;
    boolean upPressed, downPressed, leftPressed, rightPressed;
    
    // Posición en el mundo
    public int worldX;
    public int worldY;
    
    // Posición en la pantalla
    public final int screenX;
    public final int screenY;
    
    BufferedImage playerImage;
    
    // Variables para la animación
    BufferedImage[] walkSprites;
    int spriteNum = 0;
    int spriteCounter = 0;
    String direction = "down";
    
    // Agregar variable para la dirección horizontal
    boolean facingRight = true;
    
    public Player(GamePanel gp) {
        this.gp = gp;
        
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        
        getPlayerImage();
        setDefaultValues();
    }
    
    public void getPlayerImage() {
        try {
            // Cargar el sprite sheet completo
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/public/Factions/Knights/Troops/Warrior/Blue/Warrior_Blue.png"));
            
            // Inicializar el array para los 6 frames de caminata
            walkSprites = new BufferedImage[6];
            
            // Obtener el tamaño de cada sprite individual
            int spriteWidth = spriteSheet.getWidth() / 6;
            int spriteHeight = spriteSheet.getHeight() / 8;
            
            // Extraer los 6 sprites de caminata de la fila 1 (y=1)
            for(int i = 0; i < 6; i++) {
                walkSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth,    // x: multiplicar la columna por el ancho del sprite
                    1 * spriteHeight,   // y: fila 1 * alto del sprite
                    spriteWidth,        // ancho del sprite individual
                    spriteHeight        // alto del sprite individual
                );
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setDefaultValues() {
        worldX = gp.tileSize * 23; // posición inicial en el mundo
        worldY = gp.tileSize * 21;
        speed = 4;
    }
    
    public void update() {
        if(upPressed || downPressed || leftPressed || rightPressed) {
            if(upPressed) {
                worldY -= speed;
            }
            if(downPressed) {
                worldY += speed;
            }
            if(leftPressed) {
                worldX -= speed;
                facingRight = false;  // Mirando a la izquierda
            }
            if(rightPressed) {
                worldX += speed;
                facingRight = true;   // Mirando a la derecha
            }
            
            // Actualizar animación
            spriteCounter++;
            if(spriteCounter > 8) {
                spriteNum++;
                if(spriteNum >= 6) {
                    spriteNum = 0;
                }
                spriteCounter = 0;
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        BufferedImage image = walkSprites[spriteNum];
        int width = (int)(gp.tileSize * 1.5);
        int height = (int)(gp.tileSize * 1.5);
        int drawX = screenX - (width - gp.tileSize)/2;
        int drawY = screenY - (height - gp.tileSize)/2;
        
        // Crear una copia volteada de la imagen si es necesario
        if (!facingRight) {
            // Guardar la transformación original
            java.awt.geom.AffineTransform originalTransform = g2.getTransform();
            
            // Voltear la imagen horizontalmente
            g2.translate(drawX + width, drawY);
            g2.scale(-1, 1);
            g2.drawImage(image, 0, 0, width, height, null);
            
            // Restaurar la transformación original
            g2.setTransform(originalTransform);
        } else {
            // Dibujar normalmente si mira a la derecha
            g2.drawImage(image, drawX, drawY, width, height, null);
        }
        
        // Para debugging (opcional)
        g2.setColor(Color.red);
        g2.drawRect(drawX, drawY, width, height);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = true;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
} 