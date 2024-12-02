package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;

public class UI {
    GamePanel gp;
    BufferedImage heartFull;
    
    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            heartFull = ImageIO.read(getClass().getResourceAsStream("/public/UI/Icons/Heart.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2) {
        if(gp.gameState == GamePanel.PLAY_STATE) {
            drawPlayerLife(g2);
            drawObjectives(g2);
        }
        else if(gp.gameState == GamePanel.GAME_OVER_STATE) {
            drawGameOver(g2);
        }
        else if(gp.gameState == GamePanel.WIN_STATE) {
            drawWinScreen(g2);
        }
    }
    
    private void drawPlayerLife(Graphics2D g2) {
        int heartSize = 32;
        int startX = 10;
        int startY = 10;
        int spacing = 40;
        
        for(int i = 0; i < gp.player.currentLife; i++) {
            g2.drawImage(heartFull, startX + (i * spacing), startY, heartSize, heartSize, null);
        }
    }
    
    private void drawObjectives(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        int objectiveY = 50;
        
        for(Objective obj : gp.getObjectives()) {
            String text = obj.getDescription();
            if(obj.isCompleted()) {
                g2.setColor(Color.GREEN);
                text += " ¡Completado!";
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString(text, 10, objectiveY);
            objectiveY += 30;
        }
    }
    
    private void drawGameOver(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        String text = "¡GAME OVER!";
        g2.setFont(new Font("Arial", Font.BOLD, 80));
        
        g2.setColor(Color.BLACK);
        g2.drawString(text, gp.screenWidth/2 - 202, gp.screenHeight/2 - 2);
        
        g2.setColor(Color.RED);
        g2.drawString(text, gp.screenWidth/2 - 200, gp.screenHeight/2);
        
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        text = "Presiona ESPACIO para reiniciar";
        g2.drawString(text, gp.screenWidth/2 - 200, gp.screenHeight/2 + 60);
    }
    
    private void drawWinScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        String text = "¡VICTORIA!";
        g2.setFont(new Font("Arial", Font.BOLD, 80));
        
        g2.setColor(Color.BLACK);
        g2.drawString(text, gp.screenWidth/2 - 202, gp.screenHeight/2 - 2);
        
        g2.setColor(Color.GREEN);
        g2.drawString(text, gp.screenWidth/2 - 200, gp.screenHeight/2);
        
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        text = "¡Has completado todos los objetivos!";
        g2.drawString(text, gp.screenWidth/2 - 250, gp.screenHeight/2 + 60);
    }
} 