package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    
    GamePanel gp;
    Tile[] tile;
    int[][] mapTileNum;
    
    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[20];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/map01.txt");
    }
    
    public void getTileImage() {
        try {
            System.out.println("Cargando imágenes de tiles...");
            
            tile[0] = new Tile(); // grass
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/public/Terrain/Ground/Tilemap_Flat.png"))
                .getSubimage(48, 48, 48, 48);
            System.out.println("Tile 0 (pasto) cargado correctamente");
            
            tile[1] = new Tile(); // sand
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/public/Terrain/Ground/Tilemap_Flat.png"))
                .getSubimage(384, 48, 48, 48);
            System.out.println("Tile 1 (arena) cargado correctamente");
            
            for(int i = 1; i <= 18; i++) {
                String fileName = String.format("%02d.png", i);
                tile[i + 1] = new Tile();
                tile[i + 1].image = ImageIO.read(getClass().getResourceAsStream("/public/Deco/" + fileName));
                System.out.println("Decoración " + i + " cargada correctamente");
            }
            
        } catch(IOException e) {
            System.out.println("Error cargando imagen: " + e.getMessage());
            e.printStackTrace();
        }
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
            
            // Debug: imprimir algunos valores para verificar
            if (worldRow == 0 && worldCol == 0) {
                System.out.println("Dibujando tile número: " + tileNum);
                if (tile[tileNum] == null) {
                    System.out.println("¡Error! tile[" + tileNum + "] es null");
                } else if (tile[tileNum].image == null) {
                    System.out.println("¡Error! tile[" + tileNum + "].image es null");
                }
            }
            
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                
                // Dibujar el tile base (pasto o arena)
                if (tileNum <= 1) {  // Para tiles base
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                } else {  // Para decoraciones
                    // Primero dibujamos el pasto como base
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