package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    
    // Configuración de pantalla
    final int originalTileSize = 16; // 16x16 tiles
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale; // 48x48 tiles
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;  // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    
    Thread gameThread;
    public Player player;
    TileManager tileManager;
    UI ui;
    
    // Agregar estas variables para la posición de la cámara
    public int screenX;
    public int screenY;
    
    // Agregar configuración del mundo
    public final int maxWorldCol = 50; // Ajusta estos valores según el tamaño de tu mapa
    public final int maxWorldRow = 50;
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
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        tileManager.draw(g2);
        player.draw(g2);
        ui.draw(g2);
        
        g2.dispose();
    }
} 