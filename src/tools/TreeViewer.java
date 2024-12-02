package tools;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;

public class TreeViewer extends JPanel {
    private BufferedImage treeSheet;
    private static final int GRID_ROWS = 3;
    private static final int GRID_COLS = 4;
    
    public TreeViewer() {
        try {
            System.out.println("Intentando cargar imagen de árbol...");
            String path = "/public/Resources/Trees/Tree.png";
            System.out.println("Ruta de la imagen: " + path);
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                System.out.println("¡ERROR! No se pudo encontrar el archivo de imagen");
                return;
            }
            treeSheet = ImageIO.read(is);
            System.out.println("Imagen cargada correctamente");
            setPreferredSize(new Dimension(treeSheet.getWidth() + 20, treeSheet.getHeight() + 20));
        } catch (Exception e) {
            System.out.println("Error loading tree sheet: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        if (treeSheet != null) {
            // Draw the original image
            g2.drawImage(treeSheet, 10, 10, null);
            
            // Calculate cell dimensions
            int cellWidth = treeSheet.getWidth() / GRID_COLS;
            int cellHeight = treeSheet.getHeight() / GRID_ROWS;
            
            // Draw grid lines
            g2.setColor(Color.RED);
            // Vertical lines
            for (int i = 0; i <= GRID_COLS; i++) {
                int x = 10 + (i * cellWidth);
                g2.drawLine(x, 10, x, 10 + treeSheet.getHeight());
            }
            // Horizontal lines
            for (int i = 0; i <= GRID_ROWS; i++) {
                int y = 10 + (i * cellHeight);
                g2.drawLine(10, y, 10 + treeSheet.getWidth(), y);
            }
            
            // Draw cell coordinates
            g2.setColor(Color.BLACK);
            for (int row = 0; row < GRID_ROWS; row++) {
                for (int col = 0; col < GRID_COLS; col++) {
                    String coord = String.format("(%d,%d)", col, row);
                    g2.drawString(coord, 
                        15 + (col * cellWidth), 
                        25 + (row * cellHeight));
                }
            }
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tree Sprite Sheet Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TreeViewer());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
} 