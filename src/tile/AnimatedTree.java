package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class AnimatedTree {
    private BufferedImage[] frames;
    private int currentFrame;
    private int frameCount;
    private int frameDelay;
    private int frameTimer;
    
    public AnimatedTree() {
        try {
            // Cargar sprite sheet
            BufferedImage fullTreeSheet = ImageIO.read(getClass().getResourceAsStream("/public/Resources/Trees/Tree.png"));
            
            // Configurar la animación
            frameCount = 4;  // Usaremos 4 frames (0,0), (1,0), (0,1), (1,1)
            frames = new BufferedImage[frameCount];
            frameDelay = 30; // Cambiado de 15 a 30 para una animación más lenta
            currentFrame = 0;
            frameTimer = 0;
            
            // Calcular dimensiones de cada frame
            int frameWidth = fullTreeSheet.getWidth() / 4;  // 4 columnas
            int frameHeight = fullTreeSheet.getHeight() / 3; // 3 filas
            
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
        if(frameTimer > frameDelay) {
            currentFrame = (currentFrame + 1) % frameCount;
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