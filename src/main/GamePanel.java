package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.InputStream;
import java.util.ArrayList;

import tile.TileManager;
import entity.Enemy;
import entity.Sheep;
import entity.Entity;

public class GamePanel extends JPanel implements Runnable {
    
    // Configuración de pantalla
    final int originalTileSize = 16; // 16x16 tiles
    final int scale = 3;
    private Clip backgroundMusicClip;
    
    public final int tileSize = originalTileSize * scale; // 48x48 tiles
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 15;
    public final int screenWidth = tileSize * maxScreenCol;  // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 720 pixels
    
    Thread gameThread;
    public Player player;
    public Enemy[] enemies;
    private final int NUMBER_OF_ENEMIES = 5;
    public TileManager tileManager;
    UI ui;
    
    // Añadir ovejas
    public Sheep[] sheep;
    private final int NUMBER_OF_SHEEP = 4;
    
    // Agregar estas variables para la posición de la cámara
    public int screenX;
    public int screenY;
    
    // Agregar configuración del mundo
    public final int maxWorldCol = 25;
    public final int maxWorldRow = 25;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    // Agregar una lista para manejar las entidades
    private ArrayList<Entity> entities = new ArrayList<>();
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        
        // Calcular la posición central de la pantalla
        screenX = screenWidth/2 - (tileSize/2);
        screenY = screenHeight/2 - (tileSize/2);
        
        player = new Player(this);
        
        // Inicializar el array de enemigos
        enemies = new Enemy[NUMBER_OF_ENEMIES];
        // Crear los enemigos en diferentes posiciones
        enemies[0] = new Enemy(this, 10, 10);
        enemies[1] = new Enemy(this, 15, 15);
        enemies[2] = new Enemy(this, 5, 20);
        enemies[3] = new Enemy(this, 20, 5);
        enemies[4] = new Enemy(this, 8, 18);
        
        // Inicializar ovejas
        sheep = new Sheep[NUMBER_OF_SHEEP];
        sheep[0] = new Sheep(this, 3, 3);
        sheep[1] = new Sheep(this, 7, 5);
        sheep[2] = new Sheep(this, 12, 8);
        sheep[3] = new Sheep(this, 18, 12);
        
        tileManager = new TileManager(this);
        ui = new UI(this);
        
        this.addKeyListener(player);
        this.setFocusable(true);

        playBackgroundMusic();
    }
    
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public void run() {
        double drawInterval = 1000000000/60; // 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        
        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            
            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            
            // Optimización: Añadir un pequeño sleep para reducir el uso de CPU
            try {
                Thread.sleep(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void update() {
        player.update();
        tileManager.update();
        for(Enemy enemy : enemies) {
            if(enemy != null) {
                enemy.update();
            }
        }
        // Actualizar ovejas
        for(Sheep s : sheep) {
            if(s != null) {
                s.update();
            }
        }
        // Actualizar todas las entidades
        for(Entity entity : entities) {
            if(entity != null) {
                entity.update();
            }
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        // Activar optimizaciones de renderizado
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        
        // Dibujar el agua (fondo azul turquesa #48ABA9)
        g2.setColor(new Color(72, 171, 169));
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        tileManager.draw(g2);
        // Dibujar ovejas
        for(Sheep s : sheep) {
            if(s != null) {
                s.draw(g2);
            }
        }
        for(Enemy enemy : enemies) {
            if(enemy != null && isEntityVisible(enemy.worldX, enemy.worldY)) {
                enemy.draw(g2);
            }
        }
        player.draw(g2);
        ui.draw(g2);
        
        // Dibujar todas las entidades
        for(Entity entity : entities) {
            if(entity != null) {
                entity.draw(g2);
            }
        }
        
        g2.dispose();
    }
    
    // Método para verificar si una entidad está visible en pantalla
    private boolean isEntityVisible(int worldX, int worldY) {
        return worldX + tileSize > player.worldX - screenX &&
               worldX - tileSize < player.worldX + screenX &&
               worldY + tileSize > player.worldY - screenY &&
               worldY - tileSize < player.worldY + screenY;
    }

      private void playBackgroundMusic() {
        try {
            if (backgroundMusicClip == null) {
                InputStream musicStream = getClass().getResourceAsStream("/public/songs/Cancion.wav");
                if (musicStream == null) {
                    System.err.println("No se encontró el archivo de música de fondo.");
                    return;
                }
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicStream);
                backgroundMusicClip = AudioSystem.getClip();
                backgroundMusicClip.open(audioStream);
                setVolume(-10.0f); // Ajustar el volumen (más bajo que el nivel por defecto)
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            backgroundMusicClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setVolume(float volume) {
    if (backgroundMusicClip != null && backgroundMusicClip.isOpen()) {
        try {
            FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            // El volumen debe estar en un rango entre -80.0f (silencio) y 6.0f (amplificación máxima)
            gainControl.setValue(volume);
        } catch (IllegalArgumentException e) {
            System.err.println("Control de volumen no disponible: " + e.getMessage());
        }
    }
    }


    private void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }

    // Agregar estos métodos
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }
} 