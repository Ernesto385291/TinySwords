package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import tile.TileManager;
import entity.Enemy;

public class GamePanel extends JPanel implements Runnable {
    
    // Configuración de pantalla
    final int originalTileSize = 16; // 16x16 tiles
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale; // 48x48 tiles
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 15;
    public final int screenWidth = tileSize * maxScreenCol;  // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 720 pixels
    
    Thread gameThread;
    public Player player;
    public Enemy[] enemies;
    private final int NUMBER_OF_ENEMIES = 5;
    TileManager tileManager;
    UI ui;
    
    // Agregar estas variables para la posición de la cámara
    public int screenX;
    public int screenY;
    
    // Agregar configuración del mundo
    public final int maxWorldCol = 25;
    public final int maxWorldRow = 25;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
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
        enemies[0] = new Enemy(this, 10, 10); // El enemigo original
        enemies[1] = new Enemy(this, 15, 15); // Nuevo enemigo
        enemies[2] = new Enemy(this, 5, 20);  // Nuevo enemigo
        enemies[3] = new Enemy(this, 20, 5);  // Nuevo enemigo
        enemies[4] = new Enemy(this, 8, 18);  // Nuevo enemigo
        
        tileManager = new TileManager(this);
        ui = new UI(this);
        
        this.addKeyListener(player);
        this.setFocusable(true);
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
        
        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            
            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    
    public void update() {
        player.update();
        tileManager.update();
        for(Enemy enemy : enemies) {
            enemy.update();
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        // Dibujar el agua (fondo azul turquesa #48ABA9)
        g2.setColor(new Color(72, 171, 169)); // #48ABA9 en RGB
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        tileManager.draw(g2);
        for(Enemy enemy : enemies) {
            enemy.draw(g2);
        }
        player.draw(g2);
        ui.draw(g2);
        
        g2.dispose();
    }
} 