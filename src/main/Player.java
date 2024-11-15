package main;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player implements KeyListener {
    
    GamePanel gp;
    int x, y;
    int speed;
    boolean upPressed, downPressed, leftPressed, rightPressed;
    
    public Player(GamePanel gp) {
        this.gp = gp;
        setDefaultValues();
    }
    
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
    }
    
    public void update() {
        if(upPressed) {
            y -= speed;
        }
        if(downPressed) {
            y += speed;
        }
        if(leftPressed) {
            x -= speed;
        }
        if(rightPressed) {
            x += speed;
        }
    }
    
    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(x, y, gp.tileSize, gp.tileSize);
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