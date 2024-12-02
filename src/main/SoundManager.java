package main;
import javax.sound.sampled.*;
import java.io.InputStream;
import java.io.IOException;

public class SoundManager {

    public void playSoundEffect(String filePath) {
        try (InputStream soundStream = getClass().getResourceAsStream(filePath)) {
            if (soundStream == null) {
                System.err.println("No se pudo encontrar el archivo de sonido: " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // Reproduce el efecto de sonido una vez

            // Asegurarse de liberar recursos cuando termine el sonido
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Formato de archivo no compatible: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de sonido: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Dispositivo de audio no disponible: " + e.getMessage());
        }
    }
}
