package main;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import entity.Enemy;

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
    
    // Variables para las animaciones
    BufferedImage[] walkSprites;
    BufferedImage[] idleSprites;
    BufferedImage[] attackSprites;
    BufferedImage[] attackUpSprites;
    BufferedImage[] attackDownSprites;
    int spriteNum = 0;
    int spriteCounter = 0;
    boolean isMoving = false;
    boolean isAttacking = false;
    int attackDuration = 30; // Duración de la animación de ataque en frames
    int attackTimer = 0;
    
    // Agregar variable para la dirección horizontal
    boolean facingRight = true;
    String direction = "right";
    
    // Variables de vida
    public int maxLife = 3;
    public int currentLife = maxLife;
    public boolean isInvincible = false;
    public int invincibleTimer = 0;
    public int invincibleDuration = 60; // 1 segundo de invencibilidad
    
    public Player(GamePanel gp) {
        this.gp = gp;
        
        screenX = gp.screenWidth/2;
        screenY = gp.screenHeight/2;
        
        // Posición inicial en la esquina inferior izquierda
        worldX = gp.tileSize * 2;
        worldY = gp.tileSize * (gp.maxWorldRow - 3);
        
        setDefaultValues();
        getPlayerImage();
    }
    
    public void getPlayerImage() {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/public/Factions/Knights/Troops/Warrior/Blue/Warrior_Blue.png"));
            
            int spriteWidth = spriteSheet.getWidth() / 6;
            int spriteHeight = spriteSheet.getHeight() / 8;
            
            // Inicializar arrays para todas las animaciones
            walkSprites = new BufferedImage[6];
            idleSprites = new BufferedImage[6];
            attackSprites = new BufferedImage[6];    // ataque horizontal
            attackUpSprites = new BufferedImage[6];  // ataque hacia arriba
            attackDownSprites = new BufferedImage[6]; // ataque hacia abajo
            
            // Extraer sprites de idle (fila 0)
            for(int i = 0; i < 6; i++) {
                idleSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 0 * spriteHeight, spriteWidth, spriteHeight
                );
            }
            
            // Extraer sprites de caminar (fila 1)
            for(int i = 0; i < 6; i++) {
                walkSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 1 * spriteHeight, spriteWidth, spriteHeight
                );
            }
            
            // Extraer sprites de ataque horizontal (fila 2)
            for(int i = 0; i < 6; i++) {
                attackSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 2 * spriteHeight, spriteWidth, spriteHeight
                );
            }
            
            // Extraer sprites de ataque hacia abajo (fila 4)
            for(int i = 0; i < 6; i++) {
                attackDownSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 4 * spriteHeight, spriteWidth, spriteHeight
                );
            }
            
            // Extraer sprites de ataque hacia arriba (fila 6)
            for(int i = 0; i < 6; i++) {
                attackUpSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 6 * spriteHeight, spriteWidth, spriteHeight
                );
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setDefaultValues() {
        worldX = gp.tileSize * 2; // Un poco separado del borde izquierdo
        worldY = gp.tileSize * (gp.maxWorldRow - 3); // Cerca del borde inferior, pero no pegado
        speed = 4;
        direction = "down";
    }
    
    public void update() {
        // Actualizar dirección basada en el último movimiento
        if (!isAttacking) {
            if(upPressed) direction = "up";
            if(downPressed) direction = "down";
            if(leftPressed) {
                direction = "left";
                facingRight = false;
            }
            if(rightPressed) {
                direction = "right";
                facingRight = true;
            }
            
            isMoving = upPressed || downPressed || leftPressed || rightPressed;
            
            // Check collision before moving
            if(isMoving) {
                // Store the current position
                int nextWorldX = worldX;
                int nextWorldY = worldY;
                
                // Calculate next position
                if(upPressed) nextWorldY -= speed;
                if(downPressed) nextWorldY += speed;
                if(leftPressed) nextWorldX -= speed;
                if(rightPressed) nextWorldX += speed;
                
                // Get player's collision box
                int playerCol = nextWorldX/gp.tileSize;
                int playerRow = nextWorldY/gp.tileSize;
                
                // Check map boundaries
                if (playerCol < 0 || playerRow < 0 || 
                    playerCol >= gp.maxWorldCol || playerRow >= gp.maxWorldRow) {
                    return;
                }
                
                // Check if the next position has a collision tile
                int tileNum = gp.tileManager.getTileNum(playerCol, playerRow);
                
                // Only move if there's no collision
                if(!gp.tileManager.getTile(tileNum).collision) {
                    worldX = nextWorldX;
                    worldY = nextWorldY;
                }
            }
        }
        
        // Manejar la animación de ataque
        if (isAttacking) {
            attackTimer++;
            if (attackTimer >= attackDuration) {
                isAttacking = false;
                attackTimer = 0;
                spriteNum = 0;
            }
        }
        
        // Actualizar animación
        spriteCounter++;
        if(spriteCounter > 8) {
            spriteNum++;
            // Si está atacando, solo usar los frames de ataque
            if (isAttacking) {
                if(spriteNum >= 6) {
                    spriteNum = 0;
                }
            } else {
                if(spriteNum >= 6) {
                    spriteNum = 0;
                }
            }
            spriteCounter = 0;
        }
        
        // Manejar invencibilidad
        if (isInvincible) {
            invincibleTimer++;
            if (invincibleTimer >= invincibleDuration) {
                isInvincible = false;
                invincibleTimer = 0;
            }
        }
        
        // Agregar esto dentro del bloque if (isAttacking)
        if (isAttacking && attackTimer == attackDuration/2) {
            // Verificar si algún enemigo está en rango
            for(Enemy enemy : gp.enemies) {
                int xDistance = Math.abs(worldX - enemy.worldX);
                int yDistance = Math.abs(worldY - enemy.worldY);
                
                // Rango de ataque basado en la dirección
                int attackRange = gp.tileSize;
                
                if (xDistance < attackRange && yDistance < attackRange) {
                    enemy.takeDamage(1);
                }
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        BufferedImage[] currentAnimation;
        
        if (isAttacking) {
            // Seleccionar la animación de ataque según la dirección
            switch(direction) {
                case "up":
                    currentAnimation = attackUpSprites;
                    break;
                case "down":
                    currentAnimation = attackDownSprites;
                    break;
                default: // "left" o "right"
                    currentAnimation = attackSprites;
                    break;
            }
        } else {
            currentAnimation = isMoving ? walkSprites : idleSprites;
        }
        
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
        // Agregar ataque con espacio
        if(code == KeyEvent.VK_SPACE && !isAttacking) {
            isAttacking = true;
            spriteNum = 0;
            attackTimer = 0;
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
    
    public void takeDamage(int damage) {
        if (!isInvincible) {
            currentLife -= damage;
            isInvincible = true;
            invincibleTimer = 0;
        }
    }
} 