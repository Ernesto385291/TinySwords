package main;

import javax.swing.JFrame;

public class Game extends JFrame {
    
    GamePanel gamePanel;
    
    public Game() {
        gamePanel = new GamePanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Mi Juego 2D");
        
        this.add(gamePanel);
        this.pack();
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void startGameThread() {
        gamePanel.startGameThread();
    }
} 