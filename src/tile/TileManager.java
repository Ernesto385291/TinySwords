package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class TileManager {
    
    GamePanel gp;
    Tile[] tile;
    int[][] mapTileNum;
    AnimatedTree animatedTree;
    
    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[40];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        animatedTree = new AnimatedTree();
        getTileImage();
        loadMap("/maps/map01.txt");
    }
    
    public void getTileImage() {
        try {
            System.out.println("Cargando imágenes de tiles...");
            
            // Cargamos la imagen completa una sola vez para ser más eficientes
            BufferedImage fullTilemap = ImageIO.read(getClass().getResourceAsStream("/public/Terrain/Ground/Tilemap_Flat.png"));
            
            // Cargar el tile de agua
            tile[4] = new Tile(); // water
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/public/Terrain/Water/Water.png"));
            tile[4].collision = true; // El agua tiene colisión para que el jugador no pueda caminar sobre ella
            System.out.println("Tile 4 (agua) cargado correctamente");
            
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

            // Cargamos el tilemap de elevación
            BufferedImage elevationTilemap = ImageIO.read(getClass().getResourceAsStream("/public/Terrain/Ground/Tilemap_Elevation.png"));
            
            // Center elevation (tile[8])
            tile[8] = new Tile();
            tile[8].image = elevationTilemap.getSubimage(1 * 48, 4 * 48, 48, 48);
            tile[8].collision = true;
            System.out.println("Tile 8 (elevación central) cargado correctamente");
            
            // Left elevation (tile[9])
            tile[9] = new Tile();
            tile[9].image = elevationTilemap.getSubimage(0 * 48, 7 * 48, 48, 48);
            tile[9].collision = true;
            System.out.println("Tile 9 (elevación izquierda) cargado correctamente");
            
            // Right elevation (tile[10])
            tile[10] = new Tile();
            tile[10].image = elevationTilemap.getSubimage(3 * 48, 7 * 48, 48, 48);
            tile[10].collision = true;
            System.out.println("Tile 10 (elevación derecha) cargado correctamente");

            // Left edge grass (tile[11])
            tile[11] = new Tile();
            tile[11].image = fullTilemap.getSubimage(0 * 48, 1 * 48, 48, 48);
            tile[11].collision = true;
            System.out.println("Tile 11 (borde izquierdo de pasto) cargado correctamente");
            
            // Left top corner (tile[12])
            tile[12] = new Tile();
            tile[12].image = fullTilemap.getSubimage(0 * 48, 0 * 48, 48, 48);
            tile[12].collision = true;
            System.out.println("Tile 12 (esquina superior izquierda) cargado correctamente");
            
            // Left bottom edge corner (tile[13])
            tile[13] = new Tile();
            tile[13].image = fullTilemap.getSubimage(0 * 48, 3 * 48, 48, 48);
            tile[13].collision = true;
            System.out.println("Tile 13 (esquina inferior izquierda) cargado correctamente");
            
            // Right bottom edge grass (tile[14])
            tile[14] = new Tile();
            tile[14].image = fullTilemap.getSubimage(3 * 48, 3 * 48, 48, 48);
            tile[14].collision = true;
            System.out.println("Tile 14 (borde derecho de pasto) cargado correctamente");
            
            // Grass edge (tile[15])
            tile[15] = new Tile();
            tile[15].image = fullTilemap.getSubimage(1 * 48, 3 * 48, 48, 48);
            tile[15].collision = true;
            System.out.println("Tile 15 (borde inferior de pasto) cargado correctamente");

            // Right edge grass (tile[16])
            tile[16] = new Tile();
            tile[16].image = fullTilemap.getSubimage(3 * 48, 2 * 48, 48, 48);
            tile[16].collision = true;
            System.out.println("Tile 16 (borde derecho de pasto rotado) cargado correctamente");

            // Ahora cargamos las decoraciones empezando desde el índice 17
            for(int i = 1; i <= 18; i++) {
                String fileName = String.format("%02d.png", i);
                tile[i + 16] = new Tile(); // Cambiado de i + 15 a i + 16
                tile[i + 16].image = ImageIO.read(getClass().getResourceAsStream("/public/Deco/" + fileName));
                System.out.println("Decoración " + i + " cargada correctamente");
            }
            
            // Añadir el tile de agua al final
            tile[35] = new Tile(); // water
            tile[35].image = ImageIO.read(getClass().getResourceAsStream("/public/Terrain/Water/Water.png"));
            tile[35].collision = true; // El agua tiene colisión para que el jugador no pueda caminar sobre ella
            System.out.println("Tile 35 (agua) cargado correctamente");
            
            // Center stairs (tile[36])
            tile[36] = new Tile();
            tile[36].image = ImageIO.read(getClass().getResourceAsStream("/public/Terrain/Ground/stairs/center.png"));
            tile[36].collision = false; // Set to true if you want stairs to be non-walkable
            System.out.println("Tile 36 (center stairs) cargado correctamente");
            
            // Left stairs (tile[37])
            tile[37] = new Tile();
            tile[37].image = ImageIO.read(getClass().getResourceAsStream("/public/Terrain/Ground/stairs/left.png"));
            tile[37].collision = false;
            System.out.println("Tile 37 (left stairs) cargado correctamente");
            
            // Right stairs (tile[38])
            tile[38] = new Tile();
            tile[38].image = ImageIO.read(getClass().getResourceAsStream("/public/Terrain/Ground/stairs/right.png"));
            tile[38].collision = false;
            System.out.println("Tile 38 (right stairs) cargado correctamente");
            
            // Tree tile (tile[39])
            BufferedImage fullTreeSheet = ImageIO.read(getClass().getResourceAsStream("/public/Resources/Trees/Tree.png"));
            int treeWidth = fullTreeSheet.getWidth() / 4;  // 4 columnas ahora
            int treeHeight = fullTreeSheet.getHeight() / 3; // 3 filas ahora
            int col = 1; // The column of the tree you want
            int row = 2; // The row of the tree you want

            tile[39] = new Tile();
            tile[39].image = fullTreeSheet.getSubimage(
                col * treeWidth,    // x position
                row * treeHeight,   // y position
                treeWidth,          // width of single tree
                treeHeight          // height of single tree
            );
            tile[39].collision = true;
            System.out.println("Tile 39 (pine tree) cargado correctamente");
            
            // Debug: Print all available resources in the directory
            try (InputStream in = getClass().getResourceAsStream("/public/Resources/Trees");
                 BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String resource;
                System.out.println("Available resources in Trees directory:");
                while ((resource = br.readLine()) != null) {
                    System.out.println(resource);
                }
            } catch (Exception e) {
                System.out.println("Error listing resources: " + e.getMessage());
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
            
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                
                if (tileNum >= 0 && tileNum <= 16 || tileNum >= 35 && tileNum <= 38) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                } else if (tileNum >= 17 && tileNum < 35) {
                    // Decoraciones normales
                    g2.drawImage(tile[0].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    int decoSize = (int)(gp.tileSize * 0.8);
                    int offsetX = (gp.tileSize - decoSize) / 2;
                    int offsetY = (gp.tileSize - decoSize) / 2;
                    g2.drawImage(tile[tileNum].image, 
                        screenX + offsetX, 
                        screenY + offsetY, 
                        decoSize, 
                        decoSize, 
                        null);
                } else if (tileNum == 39) {
                    // Árbol animado
                    g2.drawImage(tile[0].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    animatedTree.draw(g2, screenX, screenY, gp.tileSize);
                }
            }
            
            worldCol++;
            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
    
    public int getTileNum(int col, int row) {
        return mapTileNum[col][row];
    }
    
    public Tile getTile(int index) {
        return tile[index];
    }
    
    public void update() {
        animatedTree.update();
    }
} 