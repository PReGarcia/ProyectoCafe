package conexiones;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.Document;

import pipeline.InputPort; // Importación clave para la conexión
import utils.Message;
import utils.XmlUtils;

public class ComandasPoll{
    private final Path watchDir;
    private final InputPort targetPort; // Nuevo campo para el InputPort destino

    // Constructor actualizado para recibir el puerto de entrada del SplitterSlot
    public ComandasPoll(Path watchDir, InputPort targetPort) {
        this.watchDir = watchDir;
        this.targetPort = targetPort;
    }

    public void start() {
        if (targetPort == null) {
            System.err.println("ERROR: ComandasPoll no puede iniciarse sin un InputPort destino.");
            return;
        }
        new Thread(this::pollLoop, "ComandasPoll-Thread").start();
        System.out.println("   [ComandasPoll] Iniciado para leer en: " + watchDir.toAbsolutePath());
    }

    private void pollLoop() {
        while (true) {
            try {
                Files.list(watchDir)
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
            Document xml = XmlUtils.parseXml(file.toString());
            sendToPort(xml);
            // Opcional: mover o borrar el archivo después de procesar
            // Files.delete(file); 
        } catch (Exception e) {
            System.err.println("Error procesando " + file);
        }
    }

    // Actualizado para enviar el mensaje al InputPort
    private void sendToPort(Document xml) throws Exception {
        Message initialMessage = new Message(xml);
        // El poller es el punto de entrada (Endpoint) del diagrama
        targetPort.receive(initialMessage); 
    }
}
