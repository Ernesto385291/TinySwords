package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class TileManager {
    
    GamePanel gp;
    Tile[] tile;
    int[][] mapTileNum;
    
    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[30];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/map01.txt");
    }
    
    public void getTileImage() {
        try {
            System.out.println("Cargando imágenes de tiles...");
            
            // Cargamos la imagen completa una sola vez para ser más eficientes
            BufferedImage fullTilemap = ImageIO.read(getClass().getResourceAsStream("/public/Terrain/Ground/Tilemap_Flat.png"));
            
            tile[0] = new Tile(); // grass
            tile[0].image = fullTilemap.getSubimage(48, 48, 48, 48);
            System.out.println("Tile 0 (pasto) cargado correctamente");
            
            tile[1] = new Tile(); // sand
            tile[1].image = fullTilemap.getSubimage(384, 48, 48, 48);
            System.out.println("Tile 1 (arena) cargado correctamente");
            
            tile[2] = new Tile(); // tierra
            tile[2].image = fullTilemap.getSubimage(96, 48, 48, 48);
            System.out.println("Tile 2 (tierra) cargado correctamente");
            
            tile[3] = new Tile(); // sand bottom edge
            BufferedImage originalEdge = fullTilemap.getSubimage(8 * 48, 0 * 48, 48, 48);
            
            // Rotamos la imagen 180 grados
            AffineTransform transform = AffineTransform.getRotateInstance(
                Math.PI, originalEdge.getWidth()/2, originalEdge.getHeight()/2);
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
            tile[3].image = op.filter(originalEdge, null);
            tile[3].collision = true;
            System.out.println("Tile 3 (borde inferior de arena) cargado correctamente");
            
            // Sand bottom edge (existente - tile[3])
            tile[3] = new Tile();
            BufferedImage originalBottomEdge = fullTilemap.getSubimage(8 * 48, 0 * 48, 48, 48);
            AffineTransform transformBottom = AffineTransform.getRotateInstance(
                Math.PI, originalBottomEdge.getWidth()/2, originalBottomEdge.getHeight()/2);
            AffineTransformOp opBottom = new AffineTransformOp(transformBottom, AffineTransformOp.TYPE_BILINEAR);
            tile[3].image = opBottom.filter(originalBottomEdge, null);
            tile[3].collision = true;
            
            // Sand left edge (nuevo - tile[4])
            tile[4] = new Tile();
            BufferedImage originalLeftEdge = fullTilemap.getSubimage(11 * 48, 2 * 48, 48, 48);
            AffineTransform transformLeft = AffineTransform.getRotateInstance(
                Math.PI, originalLeftEdge.getWidth()/2, originalLeftEdge.getHeight()/2);
            AffineTransformOp opLeft = new AffineTransformOp(transformLeft, AffineTransformOp.TYPE_BILINEAR);
            tile[4].image = opLeft.filter(originalLeftEdge, null);
            tile[4].collision = true;
            
            // Sand right edge (nuevo - tile[5])
            tile[5] = new Tile();
            BufferedImage originalRightEdge = fullTilemap.getSubimage(11 * 48, 2 * 48, 48, 48);
            tile[5].image = originalRightEdge;
            tile[5].collision = true;
            // Sand right bottom corner edge (nuevo - tile[6])
            tile[6] = new Tile();
            BufferedImage originalCornerEdge = fullTilemap.getSubimage(11 * 48, 4 * 48, 48, 48);
            // Primero rotamos 180 grados como estaba originalmente
            AffineTransform transformCorner = AffineTransform.getRotateInstance(
                Math.PI, originalCornerEdge.getWidth()/2, originalCornerEdge.getHeight()/2);
            // Luego aplicamos la reflexión horizontal desde el centro de la imagen
            transformCorner.translate(originalCornerEdge.getWidth(), 0);
            transformCorner.scale(-1, 1);
            AffineTransformOp opCorner = new AffineTransformOp(transformCorner, AffineTransformOp.TYPE_BILINEAR);
            tile[6].image = opCorner.filter(originalCornerEdge, null);
            tile[6].collision = true;
            System.out.println("Tile 6 (esquina derecha de arena) cargado correctamente");
            
            // Sand left bottom corner edge (nuevo - tile[7])
            tile[7] = new Tile();
            BufferedImage originalRightCornerEdge = fullTilemap.getSubimage(11 * 48, 4 * 48, 48, 48);
            AffineTransform transformRightCorner = AffineTransform.getRotateInstance(
                Math.PI, originalRightCornerEdge.getWidth()/2, originalRightCornerEdge.getHeight()/2);
            AffineTransformOp opRightCorner = new AffineTransformOp(transformRightCorner, AffineTransformOp.TYPE_BILINEAR);
            tile[7].image = opRightCorner.filter(originalRightCornerEdge, null);
            tile[7].collision = true;
            System.out.println("Tile 7 (esquina izquierda de arena) cargado correctamente");

            // Dibujamos la cuadrícula de referencia
            drawTilemapGrid(fullTilemap);
            
            // Ahora cargamos las decoraciones empezando desde el índice 8
            for(int i = 1; i <= 18; i++) {
                String fileName = String.format("%02d.png", i);
                tile[i + 7] = new Tile(); // Cambiado de i + 6 a i + 7
                tile[i + 7].image = ImageIO.read(getClass().getResourceAsStream("/public/Deco/" + fileName));
                System.out.println("Decoración " + i + " cargada correctamente");
            }
            
        } catch(IOException e) {
            System.out.println("Error cargando imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void drawTilemapGrid(BufferedImage tilemap) {
        int tileSize = 48;
        int cols = tilemap.getWidth() / tileSize;
        int rows = tilemap.getHeight() / tileSize;
        
        // Crear una nueva imagen con la cuadrícula
        BufferedImage gridImage = new BufferedImage(
            tilemap.getWidth(), 
            tilemap.getHeight(), 
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = gridImage.createGraphics();
        
        // Dibujar el tilemap original
        g2.drawImage(tilemap, 0, 0, null);
        
        // Dibujar la cuadrícula
        g2.setColor(Color.RED);
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                g2.drawRect(x * tileSize, y * tileSize, tileSize, tileSize);
                // Dibujar las coordenadas en cada celda
                g2.drawString(x + "," + y, x * tileSize + 5, y * tileSize + 15);
            }
        }
        
        // Mostrar la imagen con la cuadrícula en una ventana separada
        JFrame frame = new JFrame("Tilemap Grid Reference");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel(new ImageIcon(gridImage));
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void loadMap(String mapPath) {
        try {
            System.out.println("Intentando cargar mapa desde: " + mapPath);
            InputStream is = getClass().getResourceAsStream(mapPath);
            
            if (is == null) {
                System.out.println("¡ERROR! No se pudo encontrar el archivo del mapa");
                return;
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int col = 0;
            int row = 0;
            
            while(row < gp.maxWorldRow && col < gp.maxWorldCol) {
                String line = br.readLine();
                if (line == null) {
                    System.out.println("¡ERROR! El archivo del mapa está incompleto");
                    break;
                }
                
                String[] numbers = line.split(" ");
                
                for(col = 0; col < numbers.length && col < gp.maxWorldCol; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    System.out.println("Posición [" + col + "," + row + "] = " + num);
                }
                col = 0;
                row++;
            }
            br.close();
            
        } catch(Exception e) {
            System.out.println("Error cargando mapa: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
        
        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
            
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                
                if (tileNum >= 0 && tileNum <= 7) {  // Cambiado de 6 a 7 para incluir ambos corners
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                } else if (tileNum >= 8) {  // Cambiado de 7 a 8 para las decoraciones
                    // Dibujamos el pasto como base
                    g2.drawImage(tile[0].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    // Luego la decoración
                    int decoSize = (int)(gp.tileSize * 0.8);
                    int offsetX = (gp.tileSize - decoSize) / 2;
                    int offsetY = (gp.tileSize - decoSize) / 2;
                    g2.drawImage(tile[tileNum].image, 
                        screenX + offsetX, 
                        screenY + offsetY, 
                        decoSize, 
                        decoSize, 
                        null);
                }
            }
            
            worldCol++;
            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
} 