package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Random;

public class AnimatedTree {
    private BufferedImage[] frames;
    private int currentFrame;
    private int frameCount;
    private int frameDelay;
    private int frameTimer;
    private boolean reverseAnimation;
    private static Random random = new Random();
    private int uniqueDelay;
    private int startFrame;
    
    public AnimatedTree() {
        try {
            // Cargar sprite sheet
            BufferedImage fullTreeSheet = ImageIO.read(getClass().getResourceAsStream("/public/Resources/Trees/Tree.png"));
            
            // Configurar la animación
            frameCount = 4;
            frames = new BufferedImage[frameCount];
            frameDelay = 30;
            currentFrame = random.nextInt(frameCount); // Inicio aleatorio
            frameTimer = random.nextInt(frameDelay); // Timer aleatorio
            reverseAnimation = random.nextBoolean(); // Dirección aleatoria
            uniqueDelay = frameDelay + random.nextInt(20) - 10; // Velocidad aleatoria
            
            // Calcular dimensiones de cada frame
            int frameWidth = fullTreeSheet.getWidth() / 4;
            int frameHeight = fullTreeSheet.getHeight() / 3;
            
            // Extraer los frames en el orden correcto
            int index = 0;
            for(int row = 0; row < 2; row++) {
                for(int col = 0; col < 2; col++) {
                    frames[index] = fullTreeSheet.getSubimage(
                        col * frameWidth,
                        row * frameHeight,
                        frameWidth,
                        frameHeight
                    );
                    index++;
                }
            }
            
        } catch(Exception e) {
            System.out.println("Error loading animated tree: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void update() {
        frameTimer++;
        if(frameTimer > uniqueDelay) {
            if(reverseAnimation) {
                currentFrame--;
                if(currentFrame < 0) {
                    currentFrame = frameCount - 1;
                }
            } else {
                currentFrame = (currentFrame + 1) % frameCount;
            }
            frameTimer = 0;
        }
    }
    
    public void draw(Graphics2D g2, int screenX, int screenY, int tileSize) {
        if(frames != null && frames[currentFrame] != null) {
            int decoSize = (int)(tileSize * 2.0);
            int offsetX = (tileSize - decoSize) / 2;
            int offsetY = tileSize - decoSize;
            
            g2.drawImage(frames[currentFrame], 
                screenX + offsetX, 
                screenY + offsetY, 
                decoSize, 
                decoSize, 
                null);
        }
    }
} 