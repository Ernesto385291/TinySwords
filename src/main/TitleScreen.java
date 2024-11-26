package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class TitleScreen extends JFrame {
    private Game game;
    private BufferedImage backgroundImage;
    private BufferedImage overlayImage1;
    private BufferedImage overlayImage2;

    private int cloud1X = 0;
    private int cloud2X = 600;
    private int cloud3X = 1200;
    private int smallCloud1X = 100;

    private final int cloudSpeed = 2;

    private Clip backgroundMusic; // Variable para reproducir el WAV

    public TitleScreen() {
        this.setTitle("Pantalla de Inicio");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/public/Fondo/BG Image.png"));
            overlayImage1 = ImageIO.read(getClass().getResourceAsStream("/public/Fondo/Big Clouds.png"));
            overlayImage2 = ImageIO.read(getClass().getResourceAsStream("/public/Fondo/Small Cloud 1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }

                if (overlayImage1 != null) {
                    g.drawImage(overlayImage1, cloud1X, 100, 600, 150, null);
                    g.drawImage(overlayImage1, cloud2X, 100, 600, 150, null);
                    g.drawImage(overlayImage1, cloud3X, 100, 600, 150, null);
                }

                if (overlayImage2 != null) {
                    g.drawImage(overlayImage2, smallCloud1X, 50, 100, 30, null);
                }

                Graphics2D g2d = (Graphics2D) g;

                Font titleFont = new Font("Arial Black", Font.BOLD, 40);
                g2d.setFont(titleFont);
                g2d.setColor(Color.WHITE);
                g2d.drawString("Tiny Swords", 160, 170);
                g2d.drawString("Tiny Swords", 158, 168);
                g2d.setColor(new Color(44, 62, 80));
                g2d.drawString("Tiny Swords", 160, 170);

                Font instructionFont = new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 25);
                g2d.setFont(instructionFont);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Presiona 'ENTER' para jugar", 130, 231);
                g2d.drawString("Presiona 'ENTER' para jugar", 130, 229);
                g2d.setColor(new Color(20, 90, 50));
                g2d.drawString("Presiona 'ENTER' para jugar", 130, 230);
            }
        };

        panel.setBackground(Color.BLACK);
        this.add(panel);
        this.setVisible(true);

        playBackgroundMusic(); // Reproducir la música

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    stopBackgroundMusic(); // Detener la música cuando se presiona 'ENTER'
                    startGame();
                }
            }
        });

        this.setFocusable(true);
        startCloudMovement();
    }

    private void startCloudMovement() {
        new Thread(() -> {
            while (true) {
                cloud1X -= cloudSpeed;
                cloud2X -= cloudSpeed;
                cloud3X -= cloudSpeed;

                if (cloud1X + overlayImage1.getWidth() < -200) {
                    cloud1X = cloud3X + overlayImage1.getWidth() + 150;
                }

                if (cloud2X + overlayImage1.getWidth() < -200) {
                    cloud2X = cloud1X + overlayImage1.getWidth() + 150;
                }

                if (cloud3X + overlayImage1.getWidth() < -200) {
                    cloud3X = cloud2X + overlayImage1.getWidth() + 150;
                }

                smallCloud1X -= cloudSpeed;

                if (smallCloud1X + overlayImage2.getWidth() < 0) {
                    smallCloud1X = getWidth();
                }

                repaint();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startGame() {
        this.dispose();
        game = new Game();
        game.startGameThread();
    }

    private void playBackgroundMusic() {
        try (InputStream musicStream = getClass().getResourceAsStream("/public/songs/hola.wav")) {
            if (musicStream == null) {
                System.err.println("No se pudo encontrar el archivo de música.");
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicStream);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Reproducir en bucle
            backgroundMusic.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    public static void main(String[] args) {
        new TitleScreen();
    }
}
