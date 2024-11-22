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
    
    // Variables para el ataque
    BufferedImage[] attackSprites;
    BufferedImage[] attackUpSprites;
    BufferedImage[] attackDownSprites;
    boolean isAttacking = false;
    int attackTimer = 0;
    int attackCooldown = 300; // 5 segundos (60 FPS * 5)
    int attackDuration = 30;  // Duración de la animación de ataque
    
    // Variables de vida del enemigo
    public int maxLife = 2;
    public int currentLife = maxLife;
    
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
            SpritesheetViewer.showSpritesheetGrid(
                "/public/Factions/Goblins/Troops/Torch/Blue/Torch_Blue.png",
                7, 5
            );
            
            BufferedImage spriteSheet = setup("/public/Factions/Goblins/Troops/Torch/Blue/Torch_Blue.png");
            
            int spriteWidth = spriteSheet.getWidth() / 7;
            int spriteHeight = spriteSheet.getHeight() / 5;
            
            // Inicializar arrays
            walkSprites = new BufferedImage[7];
            idleSprites = new BufferedImage[7];
            attackSprites = new BufferedImage[6];
            attackUpSprites = new BufferedImage[6];
            attackDownSprites = new BufferedImage[6];
            
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
            
            // Extraer sprites de ataque horizontal (fila 2)
            for(int i = 0; i < 6; i++) {
                attackSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 2 * spriteHeight, spriteWidth, spriteHeight
                );
            }
            
            // Extraer sprites de ataque hacia abajo (fila 3)
            for(int i = 0; i < 6; i++) {
                attackDownSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 3 * spriteHeight, spriteWidth, spriteHeight
                );
            }
            
            // Extraer sprites de ataque hacia arriba (fila 4)
            for(int i = 0; i < 6; i++) {
                attackUpSprites[i] = spriteSheet.getSubimage(
                    i * spriteWidth, 4 * spriteHeight, spriteWidth, spriteHeight
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
        
        // Distancia para atacar
        int attackRange = gp.tileSize;
        
        // Si está en rango de ataque y no está atacando
        if (xDistance < attackRange && yDistance < attackRange && !isAttacking) {
            if (attackTimer >= attackCooldown) {
                isAttacking = true;
                attackTimer = 0;
                spriteNum = 0;
                
                // Determinar dirección del ataque
                if (Math.abs(worldY - gp.player.worldY) > Math.abs(worldX - gp.player.worldX)) {
                    if (worldY > gp.player.worldY) {
                        direction = "up";
                    } else {
                        direction = "down";
                    }
                } else {
                    direction = worldX > gp.player.worldX ? "left" : "right";
                    facingRight = direction.equals("right");
                }
            }
        }
        
        // Manejar el ataque
        if (isAttacking) {
            attackTimer++;
            // Causar daño a la mitad de la animación
            if (attackTimer == attackDuration/2) {
                if (xDistance < gp.tileSize && yDistance < gp.tileSize) {
                    gp.player.takeDamage(1);
                }
            }
            if (attackTimer >= attackDuration) {
                isAttacking = false;
            }
        } else {
            attackTimer++;
            
            // Si el jugador está dentro del rango de detección
            if (xDistance < detectionRange && yDistance < detectionRange) {
                // Perseguir al jugador
                if(worldX < gp.player.worldX - gp.tileSize/4) {
                    direction = "right";
                    worldX += speed;
                    facingRight = true;
                    isMoving = true;
                }
                if(worldX > gp.player.worldX + gp.tileSize/4) {
                    direction = "left";
                    worldX -= speed;
                    facingRight = false;
                    isMoving = true;
                }
                if(worldY < gp.player.worldY - gp.tileSize/4) {
                    direction = "down";
                    worldY += speed;
                    isMoving = true;
                }
                if(worldY > gp.player.worldY + gp.tileSize/4) {
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
        }
        
        // Actualizar animación
        spriteCounter++;
        if(spriteCounter > 12) {
            spriteNum++;
            if (isAttacking) {
                if(spriteNum >= 6) {
                    spriteNum = 0;
                }
            } else {
                if(spriteNum >= 7) {
                    spriteNum = 0;
                }
            }
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
            
            BufferedImage[] currentAnimation;
            if (isAttacking) {
                switch(direction) {
                    case "up":
                        currentAnimation = attackUpSprites;
                        break;
                    case "down":
                        currentAnimation = attackDownSprites;
                        break;
                    default:
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
        }
    }
    
    // Agregar método para recibir daño
    public void takeDamage(int damage) {
        currentLife -= damage;
        if (currentLife <= 0) {
            // Aquí puedes agregar lógica para cuando el enemigo muere
            // Por ejemplo, removerlo del juego
        }
    }
} 