package pipeline;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.Document;

import utils.Message;
import utils.XmlUtils;

public class InputPort {

    private final Path watchDir;
    private Slot slot;

    public InputPort(Path watchDir,Slot slot) {
        this.watchDir = watchDir;
        this.slot = slot;
    }

    public void leerArchivo(String nombreArchivo) {
        Document documento = XmlUtils.parseXml(nombreArchivo);

        if (documento != null) {
            Message mensaje = new Message(null, null, 0, 0, documento);
            
            try {
                slot.enviarMensaje(mensaje);
                System.out.println("InputPort: Archivo '" + nombreArchivo + "' leído y enviado al Slot.");
            } catch (Exception e) {
                System.err.println("InputPort: Error al enviar el mensaje al slot.");
                e.printStackTrace();
            }
            
        } else {
            System.err.println("InputPort: Error al leer el archivo " + nombreArchivo);
        }
    }

    public void start() {
        new Thread(this::pollLoop, "ComandasPoll-Thread").start();
        System.out.println("   [ComandasPoll] Iniciado para leer en: " + watchDir.toAbsolutePath());
    }

    private void pollLoop() {
        while (true) {
            try {
                Files.list(watchDir.toAbsolutePath())
                     .filter(Files::isRegularFile)
                     .forEach(this::processFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException ignored) {}
        }
    }
    
    private void processFile(Path file) {
        try {
            leerArchivo(file.toString());
        } catch (Exception e) {
            System.err.println("Error procesando " + file);
        }
    }
}